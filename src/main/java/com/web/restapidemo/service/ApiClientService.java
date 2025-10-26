package com.web.restapidemo.service;

import com.web.restapidemo.entity.ApiClient;
import com.web.restapidemo.repository.ApiClientRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ApiClientService {
    
    @Autowired
    private ApiClientRepository apiClientRepository;
    
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int API_KEY_LENGTH = 32;
    
    /**
     * Generate a random API key
     */
    public String generateApiKey() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < API_KEY_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }
    
    /**
     * Validate API key
     */
    public Optional<ApiClient> validateApiKey(String apiKey) {
        if (apiKey == null || apiKey.isEmpty()) {
            return Optional.empty();
        }
        
        Optional<ApiClient> client = apiClientRepository.findByApiKey(apiKey);
        if (client.isPresent()) {
            ApiClient c = client.get();
            
            // Check if client is active
            if (!"ACTIVE".equals(c.getStatus())) {
                log.warn("Inactive client attempted to access: {}", c.getClientName());
                return Optional.empty();
            }
            
            // Update last used time and increment request count
            c.setLastUsedAt(LocalDateTime.now());
            c.setRequestCount(c.getRequestCount() + 1);
            apiClientRepository.save(c);
            
            return Optional.of(c);
        }
        
        return Optional.empty();
    }
    
    /**
     * Get all clients
     */
    public List<ApiClient> getAllClients() {
        return apiClientRepository.findAll();
    }
    
    /**
     * Get client by ID
     */
    public Optional<ApiClient> getClientById(Long id) {
        return apiClientRepository.findById(id);
    }
    
    /**
     * Create new client
     */
    public ApiClient createClient(ApiClient client) {
        String apiKey = generateApiKey();
        client.setApiKey(apiKey);
        client.setCreatedAt(LocalDateTime.now());
        client.setRequestCount(0L);
        client.setStatus("ACTIVE");
        return apiClientRepository.save(client);
    }
    
    /**
     * Update client
     */
    public Optional<ApiClient> updateClient(Long id, ApiClient clientDetails) {
        return apiClientRepository.findById(id).map(existingClient -> {
            existingClient.setClientName(clientDetails.getClientName());
            existingClient.setDescription(clientDetails.getDescription());
            existingClient.setStatus(clientDetails.getStatus());
            existingClient.setContactEmail(clientDetails.getContactEmail());
            existingClient.setClientType(clientDetails.getClientType());
            return apiClientRepository.save(existingClient);
        });
    }
    
    /**
     * Delete client
     */
    public void deleteClient(Long id) {
        apiClientRepository.deleteById(id);
    }
    
    /**
     * Get active clients only
     */
    public List<ApiClient> getActiveClients() {
        return apiClientRepository.findByStatus("ACTIVE");
    }
    
    /**
     * Get clients by type
     */
    public List<ApiClient> getClientsByType(String type) {
        return apiClientRepository.findByClientType(type);
    }
    
    /**
     * Rotate API key for a client
     */
    public Optional<ApiClient> rotateApiKey(Long id) {
        return apiClientRepository.findById(id).map(client -> {
            String newApiKey = generateApiKey();
            client.setApiKey(newApiKey);
            log.info("API key rotated for client: {}", client.getClientName());
            return apiClientRepository.save(client);
        });
    }
}

