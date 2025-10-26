package com.web.restapidemo.repository;

import com.web.restapidemo.entity.ApiClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApiClientRepository extends JpaRepository<ApiClient, Long> {
    
    Optional<ApiClient> findByApiKey(String apiKey);
    
    Optional<ApiClient> findByClientName(String clientName);
    
    List<ApiClient> findByStatus(String status);
    
    List<ApiClient> findByClientType(String clientType);
}

