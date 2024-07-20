package com.ercandoygun.todoapp.config;

import com.ercandoygun.todoapp.model.User;
import com.ercandoygun.todoapp.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@AllArgsConstructor
@Slf4j
public class DataInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner initializeDatabase() {
        return args -> {
            if (userRepository.findByName("admin") == null) {
                User adminUser = new User();
                adminUser.setId("1");
                adminUser.setName("admin");
                adminUser.setPassword(passwordEncoder.encode("admin123"));
                adminUser.setEmail("admin@example.com");
                userRepository.save(adminUser);
            }
        };
    }
}