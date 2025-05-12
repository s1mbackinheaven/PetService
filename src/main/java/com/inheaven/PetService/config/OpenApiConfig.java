package com.inheaven.PetService.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@OpenAPIDefinition() // Đánh dấu đây là cấu hình OpenAPI
@Configuration // Đánh dấu đây là class cấu hình Spring
@SecurityScheme( // Cấu hình security scheme cho JWT
        name = "Bearer Authentication", // Tên của security scheme
        type = SecuritySchemeType.HTTP, // Loại security scheme là HTTP
        bearerFormat = "JWT", // Format của token là JWT
        scheme = "bearer", // Scheme là bearer
        in = SecuritySchemeIn.HEADER // Token sẽ được gửi trong header
)
public class OpenApiConfig {

    @Bean // Tạo bean OpenAPI để cấu hình Swagger
    public OpenAPI myOpenAPI() {
        // Tạo server dev
        Server devServer = new Server()
                .url("http://localhost:8080") // URL của server development
                .description("Development server"); // Mô tả server

        // Tạo thông tin liên hệ
        Contact contact = new Contact()
                .name("Pet Service Team") // Tên team
                .email("support@petservice.com"); // Email liên hệ

        // Tạo thông tin về license
        License license = new License()
                .name("Apache 2.0") // Tên license
                .url("https://www.apache.org/licenses/LICENSE-2.0"); // URL của license

        // Tạo thông tin chung về API
        Info info = new Info()
                .title("Pet Service API") // Tiêu đề API
                .version("1.0.0") // Phiên bản API
                .contact(contact) // Thông tin liên hệ
                .description("API quản lý dịch vụ thú cưng") // Mô tả API
                .license(license); // Thông tin license

        // Tạo và trả về đối tượng OpenAPI với các cấu hình trên
        return new OpenAPI()
                .info(info)
                .servers(List.of(devServer));
    }
}