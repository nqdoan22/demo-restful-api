package com.web.restapidemo.repository;

import com.web.restapidemo.entity.LogEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LogEntryRepository extends JpaRepository<LogEntry, Long> {
    
    List<LogEntry> findByUriContaining(String uri);
    
    List<LogEntry> findByMethod(String method);
    
    List<LogEntry> findByResponseStatus(Integer status);
    
    @Query("SELECT l FROM LogEntry l WHERE l.timestamp BETWEEN :startDate AND :endDate")
    List<LogEntry> findLogsByDateRange(@Param("startDate") LocalDateTime startDate, 
                                       @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT l FROM LogEntry l WHERE l.executionTimeMs > :threshold")
    List<LogEntry> findSlowRequests(@Param("threshold") Long threshold);
    
    @Query("SELECT l FROM LogEntry l WHERE l.uri LIKE %:keyword% OR l.requestBody LIKE %:keyword%")
    List<LogEntry> searchLogs(@Param("keyword") String keyword);
}

