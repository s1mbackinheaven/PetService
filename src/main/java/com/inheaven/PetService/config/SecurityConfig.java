package com.inheaven.PetService.config;

import com.inheaven.PetService.security.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.inheaven.PetService.service.CustomUserDetailsService;

import java.util.Arrays;

@Configuration // Đánh dấu là lớp cấu hình Spring
@EnableWebSecurity // Bật bảo mật web
// @EnableMethodSecurity(prePostEnabled = true) // Bật tính năng bảo mật method
// (cho phép sử dụng @PreAuthorize)
@RequiredArgsConstructor // Tự động inject các dependency
public class SecurityConfig {
    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    private final JwtFilter jwtFilter; // Filter kiểm tra JWT trong request
    private final CustomUserDetailsService userDetailsService; // Service để load user

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        SecurityFilterChain chain = http
                .csrf(csrf -> {
                    csrf.disable();
                }) // Tắt CSRF vì chúng ta sử dụng JWT
                .cors(cors -> {
                    cors.configurationSource(corsConfigurationSource());
                }) // Cấu hình CORS
                .authorizeHttpRequests(auth -> {
                    auth.anyRequest().permitAll(); // Cho phép truy cập tất cả các endpoint mà không cần xác thực
                })
                .sessionManagement(sess -> {
                    sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                }) // Không sử dụng session
                .authenticationProvider(authenticationProvider()) // Cấu hình authentication provider
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class) // Thêm JWT filter trước filter
                                                                                        // mặc định
                .build();

        return chain;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*")); // Cho phép tất cả các origin
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Cho phép các
                                                                                                   // phương thức HTTP
        configuration.setAllowedHeaders(Arrays.asList("*")); // Cho phép tất cả các header
        configuration.setExposedHeaders(Arrays.asList("Authorization")); // Cho phép client đọc header Authorization
        configuration.setMaxAge(3600L); // Cache CORS config trong 1 giờ

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Áp dụng cho tất cả các URL
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Sử dụng BCrypt để mã hóa mật khẩu
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        builder.userDetailsService(userDetailsService) // Dùng CustomUserDetailsService để load user
                .passwordEncoder(passwordEncoder()); // Mã hóa mật khẩu với BCrypt
        return builder.build();
    }
}
