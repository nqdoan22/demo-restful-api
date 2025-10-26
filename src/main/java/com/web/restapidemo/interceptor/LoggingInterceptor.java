package com.web.restapidemo.interceptor;

import com.web.restapidemo.entity.LogEntry;
import com.web.restapidemo.service.LogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalDateTime;

@Slf4j
@Component
public class LoggingInterceptor implements HandlerInterceptor {
    
    @Autowired
    private LogService logService;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        request.setAttribute("startTime", System.currentTimeMillis());
        return true;
    }
    
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        try {
            long startTime = (Long) request.getAttribute("startTime");
            long executionTime = System.currentTimeMillis() - startTime;
            
            String method = request.getMethod();
            String uri = request.getRequestURI();
            String queryString = request.getQueryString();
            if (queryString != null) {
                uri += "?" + queryString;
            }
            
            int statusCode = response.getStatus();
            String clientIp = getClientIp(request);
            String userAgent = request.getHeader("User-Agent");
            
            // Get API key and client info if available
            String apiKey = request.getHeader("X-API-Key");
            Object apiClientObj = request.getAttribute("apiClient");
            String clientName = "UNKNOWN";
            if (apiClientObj instanceof com.web.restapidemo.entity.ApiClient) {
                com.web.restapidemo.entity.ApiClient apiClient = (com.web.restapidemo.entity.ApiClient) apiClientObj;
                clientName = apiClient.getClientName();
            }
            
            // Log to file with client info
            log.info("API Request - Client: {}, Method: {}, URI: {}, Status: {}, Execution Time: {}ms", 
                    clientName, method, uri, statusCode, executionTime);
            
            // Save to database
            LogEntry logEntry = LogEntry.builder()
                    .timestamp(LocalDateTime.now())
                    .method(method)
                    .uri(uri)
                    .requestBody(apiKey != null ? "API Key: " + apiKey.substring(0, Math.min(8, apiKey.length())) + "..." : "")
                    .responseStatus(statusCode)
                    .responseBody("")
                    .executionTimeMs(executionTime)
                    .clientIp(clientIp)
                    .userAgent(userAgent)
                    .build();
            
            logService.saveLog(logEntry);
            
        } catch (Exception e) {
            log.error("Error in logging interceptor", e);
        }
    }
    
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}

