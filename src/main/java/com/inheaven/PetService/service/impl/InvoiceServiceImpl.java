package com.inheaven.PetService.service.impl;

import com.inheaven.PetService.dto.InvoiceItemDto;
import com.inheaven.PetService.dto.InvoiceRequestDto;
import com.inheaven.PetService.dto.InvoiceResponseDto;
import com.inheaven.PetService.dto.UpdateInvoiceStatusDto;
import com.inheaven.PetService.entity.Invoice;
import com.inheaven.PetService.entity.InvoiceItem;
import com.inheaven.PetService.entity.Item;
import com.inheaven.PetService.entity.User;
import com.inheaven.PetService.enums.InvoiceStatus;
import com.inheaven.PetService.exception.ResourceNotFoundException;
import com.inheaven.PetService.repository.InvoiceRepository;
import com.inheaven.PetService.repository.ItemRepository;
import com.inheaven.PetService.repository.UserRepository;
import com.inheaven.PetService.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service // Đánh dấu đây là một service
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository; // Repository để thao tác với invoice
    private final UserRepository userRepository; // Repository để thao tác với user
    private final ItemRepository itemRepository; // Repository để thao tác với item

    @Autowired // Tiêm phụ thuộc qua constructor
    public InvoiceServiceImpl(InvoiceRepository invoiceRepository, UserRepository userRepository,
            ItemRepository itemRepository) {
        this.invoiceRepository = invoiceRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    // Chuyển đổi từ Entity sang DTO để trả về cho client
    private InvoiceResponseDto convertToDto(Invoice invoice) {
        List<InvoiceItemDto> itemDtos = invoice.getInvoiceItems().stream()
                .map(this::convertToItemDto)
                .collect(Collectors.toList());

        return InvoiceResponseDto.builder()
                .id(invoice.getId())
                .userId(invoice.getUser().getId())
                .userName(invoice.getUser().getFullname())
                .items(itemDtos)
                .totalItems(invoice.getTotalItems())
                .totalAmount(invoice.getTotalAmount())
                .status(invoice.getStatus())
                .statusDisplayValue(invoice.getStatus().getDisplayValue())
                .shippingAddress(invoice.getShippingAddress())
                .paymentMethod(invoice.getPaymentMethod())
                .createdAt(invoice.getCreatedAt())
                .updatedAt(invoice.getUpdatedAt())
                .paidAt(invoice.getPaidAt())
                .completedAt(invoice.getCompletedAt())
                .build();
    }

    // Chuyển đổi từ InvoiceItem Entity sang DTO
    private InvoiceItemDto convertToItemDto(InvoiceItem invoiceItem) {
        return InvoiceItemDto.builder()
                .id(invoiceItem.getId())
                .itemId(invoiceItem.getItem().getId())
                .itemName(invoiceItem.getItem().getName())
                .quantity(invoiceItem.getQuantity())
                .price(invoiceItem.getPrice())
                .discount(invoiceItem.getDiscount())
                .subtotal(invoiceItem.getSubtotal())
                .build();
    }

    @Override
    @Transactional // Đảm bảo tính toàn vẹn của giao dịch
    public InvoiceResponseDto createInvoice(InvoiceRequestDto requestDto) {
        // Tìm user theo id
        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Không tìm thấy người dùng với ID: " + requestDto.getUserId()));

        // Tạo invoice mới
        Invoice invoice = Invoice.builder()
                .user(user)
                .shippingAddress(requestDto.getShippingAddress())
                .paymentMethod(requestDto.getPaymentMethod())
                .status(InvoiceStatus.PENDING) // Mặc định là đang chờ thanh toán
                .build();

        // Lưu invoice trước để có id
        invoice = invoiceRepository.save(invoice);

        // Thêm các items vào invoice
        for (InvoiceItemDto itemDto : requestDto.getItems()) {
            Item item = itemRepository.findById(itemDto.getItemId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Không tìm thấy sản phẩm với ID: " + itemDto.getItemId()));

            // Kiểm tra số lượng tồn kho
            if (item.getQuantity() < itemDto.getQuantity()) {
                throw new IllegalArgumentException("Sản phẩm " + item.getName()
                        + " không đủ số lượng trong kho. Hiện chỉ còn " + item.getQuantity());
            }

            // Cập nhật số lượng trong kho
            item.setQuantity(item.getQuantity() - itemDto.getQuantity());

            // Đảm bảo soldCount không null trước khi cộng thêm
            if (item.getSoldCount() == null) {
                item.setSoldCount(itemDto.getQuantity());
            } else {
                item.setSoldCount(item.getSoldCount() + itemDto.getQuantity());
            }

            itemRepository.save(item);

            // Tạo invoice item
            InvoiceItem invoiceItem = InvoiceItem.builder()
                    .invoice(invoice)
                    .item(item)
                    .quantity(itemDto.getQuantity())
                    .price(item.getDiscountedPrice()) // Sử dụng giá đã giảm
                    .discount(item.getDiscount())
                    .build();

            // Thêm vào invoice
            invoice.addInvoiceItem(invoiceItem);
        }

        // Đảm bảo tổng tiền và số lượng được tính toán
        invoice.recalculateTotals();

        // Lưu lại invoice sau khi đã thêm items
        invoice = invoiceRepository.save(invoice);

        // Chuyển về DTO để trả về
        return convertToDto(invoice);
    }

    @Override
    @Transactional
    public InvoiceResponseDto updateInvoice(Long id, InvoiceRequestDto requestDto) {
        // Tìm invoice theo id
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy hóa đơn với ID: " + id));

        // Kiểm tra xem invoice có đang ở trạng thái cho phép cập nhật không
        if (invoice.getStatus() != InvoiceStatus.PENDING) {
            throw new IllegalStateException("Chỉ có thể cập nhật hóa đơn ở trạng thái đang chờ thanh toán");
        }

        // Tìm user theo id
        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Không tìm thấy người dùng với ID: " + requestDto.getUserId()));

        // Cập nhật thông tin
        invoice.setUser(user);
        invoice.setShippingAddress(requestDto.getShippingAddress());
        invoice.setPaymentMethod(requestDto.getPaymentMethod());

        // Hoàn trả số lượng sản phẩm về kho
        for (InvoiceItem item : invoice.getInvoiceItems()) {
            Item product = item.getItem();
            product.setQuantity(product.getQuantity() + item.getQuantity());

            // Đảm bảo soldCount không null trước khi trừ đi
            if (product.getSoldCount() != null) {
                product.setSoldCount(Math.max(0, product.getSoldCount() - item.getQuantity()));
            }

            itemRepository.save(product);
        }

        // Xóa tất cả items cũ
        invoice.getInvoiceItems().clear();

        // Thêm các items mới
        for (InvoiceItemDto itemDto : requestDto.getItems()) {
            Item item = itemRepository.findById(itemDto.getItemId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Không tìm thấy sản phẩm với ID: " + itemDto.getItemId()));

            // Kiểm tra số lượng tồn kho
            if (item.getQuantity() < itemDto.getQuantity()) {
                throw new IllegalArgumentException("Sản phẩm " + item.getName()
                        + " không đủ số lượng trong kho. Hiện chỉ còn " + item.getQuantity());
            }

            // Cập nhật số lượng trong kho
            item.setQuantity(item.getQuantity() - itemDto.getQuantity());

            // Đảm bảo soldCount không null trước khi cộng thêm
            if (item.getSoldCount() == null) {
                item.setSoldCount(itemDto.getQuantity());
            } else {
                item.setSoldCount(item.getSoldCount() + itemDto.getQuantity());
            }

            itemRepository.save(item);

            // Tạo invoice item
            InvoiceItem invoiceItem = InvoiceItem.builder()
                    .invoice(invoice)
                    .item(item)
                    .quantity(itemDto.getQuantity())
                    .price(item.getDiscountedPrice())
                    .discount(item.getDiscount())
                    .build();

            // Thêm vào invoice
            invoice.addInvoiceItem(invoiceItem);
        }

        // Đảm bảo tổng tiền và số lượng được tính toán
        invoice.recalculateTotals();

        // Lưu lại invoice sau khi đã cập nhật
        invoice = invoiceRepository.save(invoice);

        // Chuyển về DTO để trả về
        return convertToDto(invoice);
    }

    @Override
    @Transactional
    public InvoiceResponseDto updateInvoiceStatus(Long id, UpdateInvoiceStatusDto requestDto) {
        // Tìm invoice theo id
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy hóa đơn với ID: " + id));

        // Cập nhật trạng thái
        invoice.setStatus(requestDto.getStatus());

        // Lưu lại invoice
        invoice = invoiceRepository.save(invoice);

        // Chuyển về DTO để trả về
        return convertToDto(invoice);
    }

    @Override
    @Transactional
    public InvoiceResponseDto confirmDelivery(Long id) {
        // Tìm invoice theo id
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy hóa đơn với ID: " + id));

        // Kiểm tra xem invoice có đang ở trạng thái đã thanh toán không
        if (invoice.getStatus() != InvoiceStatus.PAID) {
            throw new IllegalStateException("Chỉ có thể xác nhận giao hàng đối với hóa đơn đã thanh toán");
        }

        // Cập nhật trạng thái thành COMPLETED
        invoice.setStatus(InvoiceStatus.COMPLETED);
        invoice.setCompletedAt(new Date());

        // Lưu lại invoice
        invoice = invoiceRepository.save(invoice);

        // Chuyển về DTO để trả về
        return convertToDto(invoice);
    }

    @Override
    @Transactional
    public InvoiceResponseDto cancelInvoice(Long id) {
        // Tìm invoice theo id
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy hóa đơn với ID: " + id));

        // Kiểm tra xem invoice có đang ở trạng thái cho phép hủy không
        if (invoice.getStatus() == InvoiceStatus.COMPLETED) {
            throw new IllegalStateException("Không thể hủy hóa đơn đã hoàn thành");
        }

        // Hoàn trả số lượng sản phẩm về kho
        for (InvoiceItem item : invoice.getInvoiceItems()) {
            Item product = item.getItem();
            product.setQuantity(product.getQuantity() + item.getQuantity());

            // Đảm bảo soldCount không null trước khi trừ đi
            if (product.getSoldCount() != null) {
                product.setSoldCount(Math.max(0, product.getSoldCount() - item.getQuantity()));
            }

            itemRepository.save(product);
        }

        // Cập nhật trạng thái thành CANCELED
        invoice.setStatus(InvoiceStatus.CANCELED);

        // Lưu lại invoice
        invoice = invoiceRepository.save(invoice);

        // Chuyển về DTO để trả về
        return convertToDto(invoice);
    }

    @Override
    @Transactional
    public void deleteInvoice(Long id) {
        // Tìm invoice theo id
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy hóa đơn với ID: " + id));

        // Trước khi xóa, nếu không phải trạng thái CANCELED, hoàn trả số lượng sản phẩm
        // về kho
        if (invoice.getStatus() != InvoiceStatus.CANCELED) {
            for (InvoiceItem item : invoice.getInvoiceItems()) {
                Item product = item.getItem();
                product.setQuantity(product.getQuantity() + item.getQuantity());

                // Đảm bảo soldCount không null trước khi trừ đi
                if (product.getSoldCount() != null) {
                    product.setSoldCount(Math.max(0, product.getSoldCount() - item.getQuantity()));
                }

                itemRepository.save(product);
            }
        }

        // Xóa invoice
        invoiceRepository.delete(invoice);
    }

    @Override
    public List<InvoiceResponseDto> getAllInvoices() {
        // Lấy tất cả invoices và chuyển đổi sang DTO
        return invoiceRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public InvoiceResponseDto getInvoiceById(Long id) {
        // Tìm invoice theo id
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy hóa đơn với ID: " + id));

        // Chuyển về DTO để trả về
        return convertToDto(invoice);
    }

    @Override
    public List<InvoiceResponseDto> getInvoicesByUserId(Long userId) {
        // Kiểm tra xem user có tồn tại không
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("Không tìm thấy người dùng với ID: " + userId);
        }

        // Lấy tất cả invoices của user và chuyển đổi sang DTO
        return invoiceRepository.findByUserId(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<InvoiceResponseDto> getInvoicesByUserIdAndStatus(Long userId, InvoiceStatus status) {
        // Kiểm tra xem user có tồn tại không
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("Không tìm thấy người dùng với ID: " + userId);
        }

        // Lấy tất cả invoices của user theo trạng thái và chuyển đổi sang DTO
        return invoiceRepository.findByUserIdAndStatus(userId, status).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<InvoiceResponseDto> getPaidOrCompletedInvoicesByUserId(Long userId) {
        // Kiểm tra xem user có tồn tại không
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("Không tìm thấy người dùng với ID: " + userId);
        }

        // Lấy tất cả invoices của user có trạng thái PAID hoặc COMPLETED và chuyển đổi
        // sang DTO
        return invoiceRepository
                .findByUserIdAndStatusIn(userId, Arrays.asList(InvoiceStatus.PAID, InvoiceStatus.COMPLETED)).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<InvoiceResponseDto> getInvoicesByStatus(InvoiceStatus status) {
        // Lấy tất cả invoices theo trạng thái và chuyển đổi sang DTO
        return invoiceRepository.findByStatus(status).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<InvoiceResponseDto> getInvoicesByDateRange(Date startDate, Date endDate) {
        // Lấy tất cả invoices trong khoảng thời gian và chuyển đổi sang DTO
        return invoiceRepository.findByCreatedAtBetween(startDate, endDate).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public long countInvoicesByStatus(InvoiceStatus status) {
        // Đếm số lượng invoices theo trạng thái
        return invoiceRepository.countByStatus(status);
    }
}