package com.inheaven.PetService.service;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.inheaven.PetService.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import java.util.Collections;

@Service // Đánh dấu là service để Spring quản lý
@RequiredArgsConstructor // Tự động inject constructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository; // Repository để tương tác với database

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Tìm user theo username
        return userRepository.findByUsername(username)
                .map(user -> new User(
                        user.getUsername(),
                        user.getPassword(),
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole()))))
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy user với username: " + username));
    }
}