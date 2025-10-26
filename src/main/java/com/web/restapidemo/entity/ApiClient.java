package com.web.restapidemo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "api_client")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiClient {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "client_name", nullable = false, unique = true, length = 100)
    private String clientName;
    
    @Column(name = "api_key", nullable = false, unique = true, length = 64)
    private String apiKey;
    
    @Column(name = "description", length = 255)
    private String description;
    
    @Column(name = "status", nullable = false, length = 20)
    private String status = "ACTIVE"; // ACTIVE, INACTIVE
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "last_used_at")
    private LocalDateTime lastUsedAt;
    
    @Column(name = "request_count", nullable = false)
    private Long requestCount = 0L;
    
    @Column(name = "contact_email", length = 100)
    private String contactEmail;
    
    @Column(name = "client_type", length = 50)
    private String clientType; // INTERNAL, EXTERNAL
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}

