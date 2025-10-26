package com.web.restapidemo.interceptor;

import com.web.restapidemo.entity.ApiClient;
import com.web.restapidemo.service.ApiClientService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Optional;

@Component
@Slf4j
public class ApiKeyInterceptor implements HandlerInterceptor {
    
    @Autowired
    private ApiClientService apiClientService;
    
    private static final String API_KEY_HEADER = "X-API-Key";
    private static final String CLIENT_ID_HEADER = "X-Client-ID";
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        
        // Skip validation for admin endpoints
        String path = request.getRequestURI();
        if (path.startsWith("/api/admin/clients") || 
            path.startsWith("/swagger-ui") || 
            path.startsWith("/api-docs") ||
            path.startsWith("/actuator")) {
            return true;
        }
        
        // Extract API key from header
        String apiKey = request.getHeader(API_KEY_HEADER);
        String clientName = request.getHeader(CLIENT_ID_HEADER);
        
        // Check if API key is provided
        if (apiKey == null || apiKey.isEmpty()) {
            log.warn("API key missing for request: {} {}", request.getMethod(), path);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"Missing API key\",\"message\":\"Please provide X-API-Key header\"}");
            response.getWriter().flush();
            return false;
        }
        
        // Validate API key
        Optional<ApiClient> clientOptional = apiClientService.validateApiKey(apiKey);
        
        if (clientOptional.isEmpty()) {
            log.warn("Invalid API key attempted: {}", apiKey.substring(0, Math.min(apiKey.length(), 8)) + "...");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"Invalid API key\",\"message\":\"The provided API key is invalid or inactive\"}");
            response.getWriter().flush();
            return false;
        }
        
        ApiClient client = clientOptional.get();
        
        // Log successful authentication
        log.info("Authenticated client: {} (Type: {}) for {} {}", 
                client.getClientName(), client.getClientType(), request.getMethod(), path);
        
        // Set client info as request attribute for logging
        request.setAttribute("apiClient", client);
        
        return true;
    }
}

