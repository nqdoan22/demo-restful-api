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
            
            // Log to file
            log.info("API Request - Method: {}, URI: {}, Status: {}, Execution Time: {}ms", 
                    method, uri, statusCode, executionTime);
            
            // Save to database
            LogEntry logEntry = LogEntry.builder()
                    .timestamp(LocalDateTime.now())
                    .method(method)
                    .uri(uri)
                    .requestBody("")  // For simplicity, skip body capture
                    .responseStatus(statusCode)
                    .responseBody("") // For simplicity, skip body capture
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

