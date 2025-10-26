package com.web.restapidemo.controller;

import com.web.restapidemo.entity.LogEntry;
import com.web.restapidemo.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/logs")
public class LogController {
    
    @Autowired
    private LogService logService;
    
    @GetMapping("/search")
    public List<LogEntry> searchLogs(@RequestParam String keyword) {
        return logService.searchLogs(keyword);
    }
    
    @GetMapping("/date-range")
    public List<LogEntry> getLogsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return logService.findLogsByDateRange(startDate, endDate);
    }
    
    @GetMapping("/slow-requests")
    public List<LogEntry> getSlowRequests(@RequestParam(defaultValue = "1000") Long thresholdMs) {
        return logService.findSlowRequests(thresholdMs);
    }
    
    @GetMapping("/method/{method}")
    public List<LogEntry> getLogsByMethod(@PathVariable String method) {
        return logService.findByMethod(method);
    }
    
    @GetMapping("/status/{status}")
    public List<LogEntry> getLogsByStatus(@PathVariable Integer status) {
        return logService.findByResponseStatus(status);
    }
}

