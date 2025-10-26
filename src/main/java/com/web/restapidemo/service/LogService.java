package com.web.restapidemo.service;

import com.web.restapidemo.entity.LogEntry;
import com.web.restapidemo.repository.LogEntryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class LogService {
    
    @Autowired
    private LogEntryRepository logEntryRepository;
    
    public void saveLog(LogEntry logEntry) {
        try {
            logEntryRepository.save(logEntry);
            log.info("Log saved to database: {} {}", logEntry.getMethod(), logEntry.getUri());
        } catch (Exception e) {
            log.error("Error saving log to database", e);
        }
    }
    
    public List<LogEntry> searchLogs(String keyword) {
        return logEntryRepository.searchLogs(keyword);
    }
    
    public List<LogEntry> findLogsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return logEntryRepository.findLogsByDateRange(startDate, endDate);
    }
    
    public List<LogEntry> findSlowRequests(Long thresholdMs) {
        return logEntryRepository.findSlowRequests(thresholdMs);
    }
    
    public List<LogEntry> findByMethod(String method) {
        return logEntryRepository.findByMethod(method);
    }
    
    public List<LogEntry> findByResponseStatus(Integer status) {
        return logEntryRepository.findByResponseStatus(status);
    }
}

