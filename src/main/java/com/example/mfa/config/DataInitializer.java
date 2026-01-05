package com.example.mfa.config;

import com.example.mfa.model.User;
import com.example.mfa.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 初始化数据
 */
@Configuration
public class DataInitializer {
    
    @Bean
    public CommandLineRunner initData(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // 创建测试用户
            if (!userRepository.existsByUsername("admin")) {
                User admin = new User(
                    "admin",
                    passwordEncoder.encode("admin123"),
                    "admin@example.com",
                    "ADMIN"
                );
                userRepository.save(admin);
                System.out.println("创建管理员用户: admin/admin123");
            }
            
            if (!userRepository.existsByUsername("user")) {
                User user = new User(
                    "user",
                    passwordEncoder.encode("user123"),
                    "user@example.com",
                    "USER"
                );
                userRepository.save(user);
                System.out.println("创建普通用户: user/user123");
            }
        };
    }
}