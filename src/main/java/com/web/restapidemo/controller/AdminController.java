package com.web.restapidemo.controller;

import com.web.restapidemo.entity.ApiClient;
import com.web.restapidemo.service.ApiClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/clients")
@Tag(name = "Client Management (Admin)", description = "API for managing API clients - NO AUTHENTICATION REQUIRED")
public class AdminController {
    
    @Autowired
    private ApiClientService apiClientService;
    
    @Operation(summary = "Get all clients", description = "Retrieve list of all registered API clients")
    @GetMapping
    public List<ApiClient> getAllClients() {
        return apiClientService.getAllClients();
    }
    
    @Operation(summary = "Get client by ID", description = "Retrieve a specific client by its ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiClient> getClientById(
            @Parameter(description = "Client ID") @PathVariable Long id) {
        Optional<ApiClient> client = apiClientService.getClientById(id);
        return client.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @Operation(summary = "Create new client", description = "Register a new API client and generate API key")
    @PostMapping
    public ResponseEntity<ApiClient> createClient(@RequestBody ApiClient client) {
        // Generate API key for the new client
        ApiClient createdClient = apiClientService.createClient(client);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdClient);
    }
    
    @Operation(summary = "Update client", description = "Update client information (API key is not changed)")
    @PutMapping("/{id}")
    public ResponseEntity<ApiClient> updateClient(
            @Parameter(description = "Client ID") @PathVariable Long id,
            @RequestBody ApiClient clientDetails) {
        Optional<ApiClient> updatedClient = apiClientService.updateClient(id, clientDetails);
        return updatedClient.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @Operation(summary = "Delete client", description = "Remove a client from the system")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(
            @Parameter(description = "Client ID") @PathVariable Long id) {
        if (apiClientService.getClientById(id).isPresent()) {
            apiClientService.deleteClient(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
    
    @Operation(summary = "Get active clients", description = "Retrieve only active clients")
    @GetMapping("/active")
    public List<ApiClient> getActiveClients() {
        return apiClientService.getActiveClients();
    }
    
    @Operation(summary = "Get clients by type", description = "Filter clients by type (INTERNAL or EXTERNAL)")
    @GetMapping("/type/{type}")
    public List<ApiClient> getClientsByType(
            @Parameter(description = "Client type (INTERNAL or EXTERNAL)", example = "INTERNAL")
            @PathVariable String type) {
        return apiClientService.getClientsByType(type);
    }
    
    @Operation(summary = "Rotate API key", description = "Generate a new API key for an existing client")
    @PostMapping("/{id}/rotate-key")
    public ResponseEntity<ApiClient> rotateApiKey(
            @Parameter(description = "Client ID") @PathVariable Long id) {
        Optional<ApiClient> client = apiClientService.rotateApiKey(id);
        return client.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

