package com.allianz.raffle.config;

import com.allianz.raffle.model.User;
import com.allianz.raffle.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Arrays;

@Configuration
public class TestDataInitializer {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository) {
        return args -> {
            if (userRepository.count() == 0) {
                // Create test users
                User admin = new User();
                admin.setEmail("admin@example.com");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setFirstname("Admin");
                admin.setLastname("User");
                admin.setApointsBalance(1000);
                admin.setCreatedAt(LocalDateTime.now());

                User user1 = new User();
                user1.setEmail("john.doe@example.com");
                user1.setPassword(passwordEncoder.encode("user123"));
                user1.setFirstname("John");
                user1.setLastname("Doe");
                user1.setApointsBalance(500);
                user1.setCreatedAt(LocalDateTime.now());

                User user2 = new User();
                user2.setEmail("jane.doe@example.com");
                user2.setPassword(passwordEncoder.encode("user123"));
                user2.setFirstname("Jane");
                user2.setLastname("Doe");
                user2.setApointsBalance(750);
                user2.setCreatedAt(LocalDateTime.now());

                // Save all users
                userRepository.saveAll(Arrays.asList(admin, user1, user2));
                
                System.out.println("Test users have been created:");
                System.out.println("- Admin: admin@example.com / admin123");
                System.out.println("- User1: john.doe@example.com / user123");
                System.out.println("- User2: jane.doe@example.com / user123");
            }
        };
    }
}