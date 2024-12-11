# Project Files Content
Generated on: Mo  9 Dez 2024 12:30:06 CET

# Backend Files

# Frontend Files

## File: src/main/java/com/allianz/raffle/RaffleApplication.java

```
// File: /backend/src/main/java/com/allianz/raffle/RaffleApplication.java

package com.allianz.raffle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RaffleApplication {
    public static void main(String[] args) {
        SpringApplication.run(RaffleApplication.class, args);
    }
}```

## File: src/main/java/com/allianz/raffle/controller/RaffleController.java

```
package com.allianz.raffle.controller;

import com.allianz.raffle.model.Raffle;
import com.allianz.raffle.service.RaffleService;
import com.allianz.raffle.exception.ValidationException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/raffles")
@Tag(name = "Raffles", description = "Endpoints for managing raffles")
public class RaffleController {

    private final RaffleService raffleService;

    @Autowired
    public RaffleController(RaffleService raffleService) {
        this.raffleService = raffleService;
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Get raffle by ID",
        description = "Retrieves a specific raffle by its ID"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the raffle")
    @ApiResponse(responseCode = "404", description = "Raffle not found")
    public ResponseEntity<?> getRaffleById(
            @Parameter(description = "ID of the raffle to retrieve")
            @PathVariable Long id) {

        try {
            Optional<Raffle> raffle = raffleService.getRaffleById(id);
            return ResponseEntity.status(HttpStatus.CREATED).body(raffle);
        } catch (ValidationException e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PostMapping
    @Operation(
        summary = "Create a new raffle",
        description = "Creates a new raffle with the provided details"
    )
    @ApiResponse(responseCode = "201", description = "Raffle created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid raffle data")
    public ResponseEntity<?> createRaffle(@RequestBody Raffle raffle) {
        try {
            Raffle createdRaffle = raffleService.createRaffle(raffle);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdRaffle);
        } catch (ValidationException e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping
    @Operation(
        summary = "Get all raffles",
        description = "Retrieves all raffles that are visible to users (preview, active, or ended)"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved raffles")
    public ResponseEntity<List<Raffle>> getAllRaffles(
            @Parameter(description = "Filter by status (optional)")
            @RequestParam(required = false) String status) {
        
        List<Raffle> raffles;
        if ("active".equalsIgnoreCase(status)) {
            raffles = raffleService.getActiveRaffles();
        } else if ("preview".equalsIgnoreCase(status)) {
            raffles = raffleService.getPreviewRaffles();
        } else {
            raffles = raffleService.getAllRaffles();
        }
        
        return ResponseEntity.ok(raffles);
    }
}```

## File: src/main/java/com/allianz/raffle/model/Raffle.java

```
package com.allianz.raffle.model;

import com.allianz.raffle.model.enums.RaffleStatus;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "raffles")
public class Raffle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private String teaserImage;
    private String detailImage;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime previewDate;
    private String question;

    @ElementCollection
    private List<String> answerOptions = new ArrayList<>();

    @OneToMany(mappedBy = "raffle", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PrizeTier> prizeTiers = new ArrayList<>();

    @Embedded
    private ApointsConfig apointsConfig;

    @Enumerated(EnumType.STRING)
    private RaffleStatus status;

    // Constructors
    public Raffle() {
        this.apointsConfig = new ApointsConfig();
    }

    // Helper method to manage bidirectional relationship
    public void addPrizeTier(PrizeTier prizeTier) {
        prizeTiers.add(prizeTier);
        prizeTier.setRaffle(this);
    }

    public void removePrizeTier(PrizeTier prizeTier) {
        prizeTiers.remove(prizeTier);
        prizeTier.setRaffle(null);
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTeaserImage() {
        return teaserImage;
    }

    public void setTeaserImage(String teaserImage) {
        this.teaserImage = teaserImage;
    }

    public String getDetailImage() {
        return detailImage;
    }

    public void setDetailImage(String detailImage) {
        this.detailImage = detailImage;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public LocalDateTime getPreviewDate() {
        return previewDate;
    }

    public void setPreviewDate(LocalDateTime previewDate) {
        this.previewDate = previewDate;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getAnswerOptions() {
        return answerOptions;
    }

    public void setAnswerOptions(List<String> answerOptions) {
        this.answerOptions = answerOptions;
    }

    public List<PrizeTier> getPrizeTiers() {
        return prizeTiers;
    }

    public void setPrizeTiers(List<PrizeTier> prizeTiers) {
        // Clear existing prize tiers
        this.prizeTiers.clear();
        if (prizeTiers != null) {
            // Add each prize tier using the helper method to maintain the relationship
            prizeTiers.forEach(this::addPrizeTier);
        }
    }

    public ApointsConfig getApointsConfig() {
        return apointsConfig;
    }

    public void setApointsConfig(ApointsConfig apointsConfig) {
        this.apointsConfig = apointsConfig;
    }

    public RaffleStatus getStatus() {
        return status;
    }

    public void setStatus(RaffleStatus status) {
        this.status = status;
    }
}```

## File: src/main/java/com/allianz/raffle/model/PrizeTier.java

```
package com.allianz.raffle.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;

@Entity
@Table(name = "prize_tiers")
public class PrizeTier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer tier;
    private String description;
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "raffle_id")
    @JsonIgnore // Prevent circular reference during JSON serialization
    private Raffle raffle;

    // Constructors
    public PrizeTier() {}

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTier() {
        return tier;
    }

    public void setTier(Integer tier) {
        this.tier = tier;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Raffle getRaffle() {
        return raffle;
    }

    public void setRaffle(Raffle raffle) {
        this.raffle = raffle;
    }
}```

## File: src/main/java/com/allianz/raffle/model/ApointsConfig.java

```
// File: /backend/src/main/java/com/allianz/raffle/model/ApointsConfig.java

package com.allianz.raffle.model;

import javax.persistence.Embeddable;

@Embeddable
public class ApointsConfig {
    private Integer costPerChance;
    private Integer maxPurchases;

    // Constructors
    public ApointsConfig() {}

    public ApointsConfig(Integer costPerChance, Integer maxPurchases) {
        this.costPerChance = costPerChance;
        this.maxPurchases = maxPurchases;
    }

    // Getters and Setters
    public Integer getCostPerChance() {
        return costPerChance;
    }

    public void setCostPerChance(Integer costPerChance) {
        this.costPerChance = costPerChance;
    }

    public Integer getMaxPurchases() {
        return maxPurchases;
    }

    public void setMaxPurchases(Integer maxPurchases) {
        this.maxPurchases = maxPurchases;
    }
}```

## File: src/main/java/com/allianz/raffle/model/enums/RaffleStatus.java

```
// File: /backend/src/main/java/com/allianz/raffle/model/enums/RaffleStatus.java

package com.allianz.raffle.model.enums;

public enum RaffleStatus {
    DRAFT,       // Initial state when created
    PREVIEW,     // Visible but not yet accepting entries
    ACTIVE,      // Accepting entries
    ENDED,       // No longer accepting entries but not drawn
    DRAWN        // Winner has been selected
}```

## File: src/main/java/com/allianz/raffle/service/RaffleService.java

```
package com.allianz.raffle.service;

import com.allianz.raffle.model.Raffle;
import com.allianz.raffle.model.PrizeTier;
import com.allianz.raffle.model.enums.RaffleStatus;
import com.allianz.raffle.repository.RaffleRepository;
import com.allianz.raffle.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RaffleService {

    private final RaffleRepository raffleRepository;

    @Autowired
    public RaffleService(RaffleRepository raffleRepository) {
        this.raffleRepository = raffleRepository;
    }

    @Transactional(readOnly = true)
    public Optional<Raffle> getRaffleById(Long id) {
        return raffleRepository.findById(id)
                .map(this::updateRaffleStatus);
    }

    @Transactional
    public Raffle createRaffle(Raffle raffle) {
        validateRaffle(raffle);
        
        // Set initial status to DRAFT
        raffle.setStatus(RaffleStatus.DRAFT);
        
        // Ensure proper relationship mapping for prize tiers
        if (raffle.getPrizeTiers() != null) {
            raffle.getPrizeTiers().forEach(prizeTier -> prizeTier.setRaffle(raffle));
        }
        
        return raffleRepository.save(raffle);
    }

    private void validateRaffle(Raffle raffle) {
        if (raffle.getName() == null || raffle.getName().trim().isEmpty()) {
            throw new ValidationException("Raffle name is required");
        }

        if (raffle.getStartDate() == null) {
            throw new ValidationException("Start date is required");
        }

        if (raffle.getEndDate() == null) {
            throw new ValidationException("End date is required");
        }

        if (raffle.getPreviewDate() == null) {
            throw new ValidationException("Preview date is required");
        }

        if (raffle.getQuestion() == null || raffle.getQuestion().trim().isEmpty()) {
            throw new ValidationException("Question is required");
        }

        if (raffle.getAnswerOptions() == null || raffle.getAnswerOptions().size() < 2) {
            throw new ValidationException("At least two answer options are required");
        }

        if (raffle.getEndDate().isBefore(raffle.getStartDate())) {
            throw new ValidationException("End date must be after start date");
        }

        if (raffle.getStartDate().isBefore(raffle.getPreviewDate())) {
            throw new ValidationException("Start date must be after preview date");
        }

        if (raffle.getPrizeTiers() == null || raffle.getPrizeTiers().isEmpty()) {
            throw new ValidationException("At least one prize tier is required");
        }
        
        validatePrizeTiers(raffle.getPrizeTiers());
    }

    private void validatePrizeTiers(List<PrizeTier> prizeTiers) {
        for (PrizeTier prizeTier : prizeTiers) {
            if (prizeTier.getTier() == null || prizeTier.getTier() < 0) {
                throw new ValidationException("Prize tier must have a valid tier number");
            }
            if (prizeTier.getDescription() == null || prizeTier.getDescription().trim().isEmpty()) {
                throw new ValidationException("Prize tier must have a description");
            }
            if (prizeTier.getQuantity() == null || prizeTier.getQuantity() < 1) {
                throw new ValidationException("Prize tier must have a valid quantity (minimum 1)");
            }
        }
    }

    @Transactional(readOnly = true)
    public List<Raffle> getAllRaffles() {
        List<Raffle> raffles = raffleRepository.findAll();
        return raffles.stream()
                .map(this::updateRaffleStatus)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Raffle> getActiveRaffles() {
        LocalDateTime now = LocalDateTime.now();
        return raffleRepository.findByStartDateBeforeAndEndDateAfter(now, now)
                .stream()
                .filter(raffle -> raffle.getStatus() == RaffleStatus.ACTIVE)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Raffle> getPreviewRaffles() {
        LocalDateTime now = LocalDateTime.now();
        return raffleRepository.findAll().stream()
                .filter(raffle -> raffle.getPreviewDate().isBefore(now) 
                        && raffle.getStartDate().isAfter(now)
                        && raffle.getStatus() == RaffleStatus.PREVIEW)
                .collect(Collectors.toList());
    }

    private Raffle updateRaffleStatus(Raffle raffle) {
        LocalDateTime now = LocalDateTime.now();
        
        if (raffle.getStatus() == RaffleStatus.DRAWN) {
            return raffle;
        }

        if (now.isBefore(raffle.getPreviewDate())) {
            raffle.setStatus(RaffleStatus.DRAFT);
        } else if (now.isBefore(raffle.getStartDate())) {
            raffle.setStatus(RaffleStatus.PREVIEW);
        } else if (now.isBefore(raffle.getEndDate())) {
            raffle.setStatus(RaffleStatus.ACTIVE);
        } else {
            raffle.setStatus(RaffleStatus.ENDED);
        }

        return raffleRepository.save(raffle);
    }
}```

## File: src/main/java/com/allianz/raffle/repository/RaffleRepository.java

```
// File: /backend/src/main/java/com/allianz/raffle/repository/RaffleRepository.java

package com.allianz.raffle.repository;

import com.allianz.raffle.model.Raffle;
import com.allianz.raffle.model.enums.RaffleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RaffleRepository extends JpaRepository<Raffle, Long> {
    
    List<Raffle> findByStatus(RaffleStatus status);
    
    List<Raffle> findByStartDateBeforeAndEndDateAfter(LocalDateTime now, LocalDateTime now2);
    
    List<Raffle> findByEndDateBeforeAndStatusNot(LocalDateTime now, RaffleStatus status);
    
    // TODO: Add more custom query methods as needed
}```

## File: src/main/java/com/allianz/raffle/config/SecurityConfig.java

```
package com.allianz.raffle.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors().and()
            .csrf().disable()
            .authorizeRequests()
                .antMatchers("/v3/api-docs/**", 
                           "/swagger-ui/**", 
                           "/swagger-ui.html").permitAll()
                .anyRequest().authenticated()
            .and()
            .httpBasic();
        
        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails admin = User.builder()
            .username("admin")
            .password(passwordEncoder().encode("admin123"))
            .roles("ADMIN")
            .build();

        return new InMemoryUserDetailsManager(admin);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}```

## File: src/main/java/com/allianz/raffle/config/OpenApiConfig.java

```
// File: /backend/src/main/java/com/allianz/raffle/config/OpenApiConfig.java

package com.allianz.raffle.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI raffleSystemOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Raffle System API")
                        .description("API for managing raffles, participants, and prizes")
                        .version("v1.0.0")
                        .license(new License().name("Private").url("https://allianz.com")));
    }
}```

## File: src/main/resources/application.properties

```
# Database Configuration
spring.datasource.url=jdbc:sqlite:raffles-dev.db
spring.datasource.driver-class-name=org.sqlite.JDBC
spring.jpa.database-platform=org.hibernate.community.dialect.SQLiteDialect

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Server Configuration
server.port=8080

# Security Configuration
spring.security.user.name=admin
spring.security.user.password=admin123
spring.security.user.roles=ADMIN

# JWT Configuration
jwt.secret=your-secret-key-here
jwt.expiration=86400000

# Email Configuration
spring.mail.host=smtp.example.com
spring.mail.port=587
spring.mail.username=your-email@example.com
spring.mail.password=your-password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true```

## File: src/main/resources/application-dev.properties

```
# Development Environment Configuration

# Database Configuration
spring.datasource.url=jdbc:sqlite:raffles-dev.db
spring.datasource.driver-class-name=org.sqlite.JDBC
spring.jpa.database-platform=org.sqlite.hibernate.dialect.SQLiteDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# Server Configuration
server.port=8080

# OpenAPI Configuration
springdoc.api-docs.path=/v3/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.swagger-ui.operationsSorter=method
springdoc.swagger-ui.tagsSorter=alpha
springdoc.show-actuator=true

# Logging Configuration
logging.level.root=INFO
logging.level.com.allianz.raffle=DEBUG
logging.pattern.console=%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n

# JWT Configuration
jwt.secret=dev-secret-key-should-be-replaced-in-production
jwt.expiration=86400000

# Email Configuration (using dummy SMTP for development)
spring.mail.host=localhost
spring.mail.port=25
spring.mail.username=test
spring.mail.password=test
spring.mail.properties.mail.smtp.auth=false
spring.mail.properties.mail.smtp.starttls.enable=false

# CORS Configuration
cors.allowed-origins=http://localhost:4200
cors.allowed-methods=GET,POST,PUT,DELETE,OPTIONS
cors.allowed-headers=*
cors.max-age=3600```

## File: src/main/resources/application-prod.properties

```
# Production Environment Configuration

# Database Configuration
spring.datasource.url=jdbc:sqlite:raffles-prod.db
spring.datasource.driver-class-name=org.sqlite.JDBC
spring.jpa.database-platform=org.sqlite.hibernate.dialect.SQLiteDialect
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false

# Server Configuration
server.port=8080

# Logging Configuration
logging.level.root=WARN
logging.level.com.allianz.raffle=INFO
logging.file.name=logs/raffle-system.log
logging.pattern.file=%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n

# JWT Configuration
jwt.secret=${JWT_SECRET:replace-this-with-your-secret-key}
jwt.expiration=86400000

# Email Configuration
spring.mail.host=${SMTP_HOST}
spring.mail.port=${SMTP_PORT}
spring.mail.username=${SMTP_USERNAME}
spring.mail.password=${SMTP_PASSWORD}
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

# CORS Configuration
cors.allowed-origins=${CORS_ALLOWED_ORIGINS:http://localhost:8080}
cors.allowed-methods=GET,POST,PUT,DELETE,OPTIONS
cors.allowed-headers=*
cors.max-age=3600

# Security Configuration
server.ssl.enabled=true
server.ssl.key-store=${SSL_KEYSTORE_PATH}
server.ssl.key-store-password=${SSL_KEYSTORE_PASSWORD}
server.ssl.key-store-type=PKCS12
server.ssl.key-alias=raffle-system```

## File: pom.xml

```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.allianz</groupId>
    <artifactId>raffle-system</artifactId>
    <version>1.0-SNAPSHOT</version>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.7.0</version>
    </parent>

    <properties>
        <java.version>11</java.version>
    </properties>

    <dependencies>
        <!-- Spring Boot -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-mail</artifactId>
        </dependency>

        <!-- OpenAPI/Swagger -->
        <dependency>
            <groupId>org.springdoc</groupId>
            <artifactId>springdoc-openapi-ui</artifactId>
            <version>1.6.9</version>
        </dependency>
        <dependency>
            <groupId>org.springdoc</groupId>
            <artifactId>springdoc-openapi-security</artifactId>
            <version>1.6.9</version>
        </dependency>

        <!-- SQLite -->
        <dependency>
            <groupId>org.xerial</groupId>
            <artifactId>sqlite-jdbc</artifactId>
            <version>3.36.0.3</version>
        </dependency>
        <dependency>
            <groupId>com.github.gwenn</groupId>
            <artifactId>sqlite-dialect</artifactId>
            <version>0.1.2</version>
        </dependency>

        <!-- JWT -->
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt</artifactId>
            <version>0.9.1</version>
        </dependency>

        <!-- Test -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

    <repositories>
        <repository>
            <id>jitpack.io</id>
            <url>https://jitpack.io</url>
        </repository>
    </repositories>
</project>```

## File: src/app/components/raffle-list/raffle-list.component.ts

```
// File: /frontend/src/app/components/raffle-list/raffle-list.component.ts

import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { RaffleService } from '../../services/raffle.service';
import { Raffle } from '../../models/raffle.model';
import { NxTableModule } from '@aposin/ng-aquila/table';
import { CommonModule } from '@angular/common';
import { NxIconModule } from '@aposin/ng-aquila/icon';
import { NxSpinnerModule } from '@aposin/ng-aquila/spinner';
import { NxMessageModule } from '@aposin/ng-aquila/message';

@Component({
  selector: 'app-raffle-list',
  templateUrl: './raffle-list.component.html',
  styleUrls: ['./raffle-list.component.scss'],
  imports: [
    CommonModule,
    NxTableModule,
    NxIconModule,
    NxSpinnerModule,
    NxMessageModule
  ]
  , standalone: true
})
export class RaffleListComponent implements OnInit {
  raffles: Raffle[] = [];
  loading = false;
  error: string | null = null;

  constructor(
    private router: Router,
    private raffleService: RaffleService
  ) {}

  ngOnInit(): void {
    this.loadRaffles();
  }

  loadRaffles(): void {
    this.loading = true;
    this.raffleService.getAllRaffles()
      .subscribe({
        next: (raffles) => {
          this.raffles = raffles;
          this.loading = false;
        },
        error: (error) => {
          this.error = 'Failed to load raffles';
          this.loading = false;
        }
      });
  }

  viewRaffleDetails(id: number): void {
    this.router.navigate(['/raffles', id]);
  }
}```

## File: src/app/components/raffle-detail/raffle-detail.component.ts

```
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { RaffleService } from '../../services/raffle.service';
import { Raffle } from '../../models/raffle.model';
import { NxIconModule } from '@aposin/ng-aquila/icon';
import { NxSpinnerModule } from '@aposin/ng-aquila/spinner';
import { NxMessageModule } from '@aposin/ng-aquila/message';
import { NxCardModule } from '@aposin/ng-aquila/card';
import { NxFormfieldModule } from '@aposin/ng-aquila/formfield';
import { NxDropdownModule } from '@aposin/ng-aquila/dropdown';
import { NxButtonModule } from '@aposin/ng-aquila/button';
import { NxInputModule } from '@aposin/ng-aquila/input';
import { NxTableModule } from '@aposin/ng-aquila/table';

@Component({
  selector: 'app-raffle-detail',
  templateUrl: './raffle-detail.component.html',
  styleUrls: ['./raffle-detail.component.scss'],
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    NxIconModule,
    NxSpinnerModule,
    NxMessageModule,
    NxCardModule,
    NxFormfieldModule,
    NxDropdownModule,
    NxButtonModule,
    NxInputModule,
    NxTableModule
  ]
})
export class RaffleDetailComponent implements OnInit {
  raffle: Raffle | null = null;
  loading = false;
  error: string | null = null;
  answerForm: FormGroup;
  chancesForm: FormGroup;

  constructor(
    private route: ActivatedRoute,
    private raffleService: RaffleService,
    private formBuilder: FormBuilder
  ) {
    this.answerForm = this.formBuilder.group({
      selectedAnswer: ['', Validators.required]
    });

    this.chancesForm = this.formBuilder.group({
      additionalChances: [0, [
        Validators.required,
        Validators.min(0),
        Validators.max(100)
      ]]
    });
  }

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.loadRaffle(Number(id));
    }
  }

  loadRaffle(id: number): void {
    this.loading = true;
    this.raffleService.getRaffleById(id)
      .subscribe({
        next: (raffle) => {
          this.raffle = raffle;
          if (raffle.apointsConfig) {
            this.chancesForm.get('additionalChances')?.setValidators([
              Validators.required,
              Validators.min(0),
              Validators.max(raffle.apointsConfig.maxPurchases)
            ]);
          }
          this.loading = false;
        },
        error: (error) => {
          this.error = 'Failed to load raffle details';
          this.loading = false;
        }
      });
  }

  submitAnswer(): void {
    if (this.answerForm.valid && this.raffle) {
      const answer = this.answerForm.get('selectedAnswer')?.value;
      console.log('Submitting answer:', answer);
    }
  }

  purchaseChances(): void {
    if (this.chancesForm.valid && this.raffle) {
      const amount = this.chancesForm.get('additionalChances')?.value;
      console.log('Purchasing chances:', amount);
    }
  }
}```

## File: src/app/components/admin/raffle-management/raffle-management.component.ts

```
// File: /frontend/src/app/components/admin/raffle-management/raffle-management.component.ts

import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { RaffleService } from '../../../services/raffle.service';
import { Raffle } from '../../../models/raffle.model';

@Component({
  selector: 'app-raffle-management',
  templateUrl: './raffle-management.component.html',
  styleUrls: ['./raffle-management.component.scss']
})
export class RaffleManagementComponent implements OnInit {
  raffles: Raffle[] = [];
  loading = false;
  error: string | null = null;

  constructor(
    private raffleService: RaffleService
  ) {}

  ngOnInit(): void {
    this.loadRaffles();
  }

  loadRaffles(): void {
    this.loading = true;
    this.raffleService.getAllRaffles()
      .subscribe({
        next: (raffles) => {
          this.raffles = raffles;
          this.loading = false;
        },
        error: (error) => {
          this.error = 'Failed to load raffles';
          this.loading = false;
        }
      });
  }

  openCreateDialog(): void {
    // TODO: Implement create dialog
    console.log('Opening create dialog');
  }

  editRaffle(raffle: Raffle): void {
    // TODO: Implement edit functionality
    console.log('Editing raffle:', raffle);
  }

  conductDraw(raffleId: number): void {
    this.loading = true;
    this.raffleService.conductDraw(raffleId)
      .subscribe({
        next: () => {
          this.loadRaffles();
          this.loading = false;
        },
        error: (error) => {
          this.error = 'Failed to conduct draw';
          this.loading = false;
        }
      });
  }
}```

## File: src/app/services/raffle.service.ts

```
// File: /frontend/src/app/services/raffle.service.ts

import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Raffle } from '../models/raffle.model';

@Injectable({
  providedIn: 'root'
})
export class RaffleService {
  private apiUrl = `${environment.apiUrl}/raffles`;

  constructor(private http: HttpClient) { }

  getAllRaffles(): Observable<Raffle[]> {
    return this.http.get<Raffle[]>(this.apiUrl);
  }

  getRaffleById(id: number): Observable<Raffle> {
    return this.http.get<Raffle>(`${this.apiUrl}/${id}`);
  }

  createRaffle(raffle: Raffle): Observable<Raffle> {
    return this.http.post<Raffle>(this.apiUrl, raffle);
  }

  updateRaffle(id: number, raffle: Raffle): Observable<Raffle> {
    return this.http.put<Raffle>(`${this.apiUrl}/${id}`, raffle);
  }

  deleteRaffle(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  conductDraw(id: number): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/${id}/draw`, {});
  }
}```

## File: src/app/services/auth.service.ts

```
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { Router } from '@angular/router';
import { environment } from '../../environments/environment';

interface User {
  id: number;
  username: string;
  token?: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private isAuthenticatedSubject = new BehaviorSubject<boolean>(true);
  private currentUserSubject = new BehaviorSubject<User | null>(null);
  public currentUser$: Observable<User | null>;

  constructor(
    private http: HttpClient,
    private router: Router
  ) {
    // Try to restore user from localStorage
    const storedUser = localStorage.getItem('currentUser');
    if (storedUser) {
      this.currentUserSubject.next(JSON.parse(storedUser));
      this.isAuthenticatedSubject.next(true);
    }
    this.currentUser$ = this.currentUserSubject.asObservable();
  }

  public get currentUserValue(): User | null {
    return this.currentUserSubject.value;
  }

  isAuthenticated(): Observable<boolean> {
    return this.isAuthenticatedSubject.asObservable();
  }

  logout() {
    // Remove user from local storage
    localStorage.removeItem('currentUser');
    // Update subjects
    this.currentUserSubject.next(null);
    this.isAuthenticatedSubject.next(false);
    // Navigate to login
    this.router.navigate(['/login']);
  }
}```

## File: src/app/models/raffle.model.ts

```
// File: /frontend/src/app/models/raffle.model.ts

export interface Raffle {
  id: number;
  name: string;
  description: string;
  teaserImage: string;
  detailImage: string;
  startDate: Date;
  endDate: Date;
  previewDate: Date;
  question: string;
  answerOptions: string[];
  prizeTiers: PrizeTier[];
  apointsConfig?: ApointsConfig;
  status: RaffleStatus;
}

export interface PrizeTier {
  tier: number;
  description: string;
  quantity: number;
}

export interface ApointsConfig {
  costPerChance: number;
  maxPurchases: number;
}

export enum RaffleStatus {
  PREVIEW = 'PREVIEW',
  ACTIVE = 'ACTIVE',
  ENDED = 'ENDED',
  DRAWN = 'DRAWN'
}```

## File: src/app/guards/auth.guard.ts

```
// File: /frontend/src/app/guards/auth.guard.ts

import { Injectable } from '@angular/core';
import { Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Injectable({ providedIn: 'root' })
export class AuthGuard implements CanActivate {
    constructor(
        private router: Router,
        private authService: AuthService
    ) { }

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const currentUser = this.authService.currentUserValue;
        if (currentUser) {
            return true;
        }

        this.router.navigate(['/login'], { queryParams: { returnUrl: state.url } });
        return false;
    }
}```

## File: src/app/guards/admin.guard.ts

```
// File: /frontend/src/app/guards/admin.guard.ts

import { Injectable } from '@angular/core';
import { Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Injectable({ providedIn: 'root' })
export class AdminGuard implements CanActivate {
    constructor(
        private router: Router,
        private authService: AuthService
    ) { }

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const currentUser = this.authService.currentUserValue;
        if (currentUser && currentUser.role === 'ADMIN') {
            return true;
        }

        this.router.navigate(['/']);
        return false;
    }
}```

## File: src/app/interceptors/jwt.interceptor.ts

```
import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable()
export class JwtInterceptor implements HttpInterceptor {
    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        const isApiUrl = request.url.startsWith(environment.apiUrl);
        
        if (isApiUrl) {
            // Add Basic Auth header
            const credentials = btoa('admin:admin123');
            request = request.clone({
                setHeaders: {
                    Authorization: `Basic ${credentials}`
                }
            });
        }

        return next.handle(request);
    }
}```

## File: src/app/interceptors/error.interceptor.ts

```
import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { AuthService } from '../services/auth.service';

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {
    constructor(private authService: AuthService) { }

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        return next.handle(request).pipe(catchError((err: HttpErrorResponse) => {
            if ([401, 403].includes(err.status) && this.authService.currentUserValue) {
                // auto logout if 401 or 403 response returned from api
                this.authService.logout();
            }

            const error = err.error?.message || err.statusText || 'An error occurred';
            return throwError(() => error);
        }));
    }
}```

## File: src/app/app.module.ts

```
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { ReactiveFormsModule } from '@angular/forms';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { JwtInterceptor } from './interceptors/jwt.interceptor';
import { ErrorInterceptor } from './interceptors/error.interceptor';

// Ng-Aquila Modules
import { NxTableModule } from '@aposin/ng-aquila/table';
import { NxIconModule } from '@aposin/ng-aquila/icon';
import { NxSpinnerModule } from '@aposin/ng-aquila/spinner';
import { NxMessageModule } from '@aposin/ng-aquila/message';
import { NxCardModule } from '@aposin/ng-aquila/card';
import { NxButtonModule } from '@aposin/ng-aquila/button';
import { NxFormfieldModule } from '@aposin/ng-aquila/formfield';
import { NxInputModule } from '@aposin/ng-aquila/input';
import { NxDropdownModule } from '@aposin/ng-aquila/dropdown';

// Components
import { RaffleListComponent } from './components/raffle-list/raffle-list.component';
import { UserListComponent } from './components/user-list/user-list.component';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    HttpClientModule,
    AppRoutingModule,
    ReactiveFormsModule,
    UserListComponent,
    RaffleListComponent,
    // Ng-Aquila Modules
    NxTableModule,
    NxIconModule,
    NxSpinnerModule,
    NxMessageModule,
    NxCardModule,
    NxButtonModule,
    NxFormfieldModule,
    NxInputModule,
    NxDropdownModule
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }```

## File: src/app/app.component.ts

```
import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  template: `
    <div class="app-container">
      <header class="nx-margin-bottom-m">
        <h1 class="nx-margin-bottom-0">Raffle System</h1>
      </header>

      <main>
        <router-outlet></router-outlet>
      </main>
    </div>
  `,
  styles: [`
    .app-container {
      padding: 2rem;
      max-width: 1200px;
      margin: 0 auto;
    }

    header {
      border-bottom: 1px solid #eee;
      padding-bottom: 1rem;
    }
  `]
})
export class AppComponent {
  title = 'Raffle System';
}```

## File: src/app/app-routing.module.ts

```
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UserListComponent } from './components/user-list/user-list.component';
import { RaffleListComponent } from './components/raffle-list/raffle-list.component';
import { RaffleDetailComponent } from './components/raffle-detail/raffle-detail.component';

const routes: Routes = [
  { path: 'users', component: UserListComponent },
  { path: 'raffles', component: RaffleListComponent },
  { path: 'raffles/:id', component: RaffleDetailComponent },
  { path: '', redirectTo: '/users', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }```
