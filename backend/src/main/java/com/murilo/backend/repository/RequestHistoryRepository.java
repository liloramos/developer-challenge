package com.murilo.backend.repository;

import com.murilo.backend.domain.model.RequestHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestHistoryRepository 
        extends JpaRepository<RequestHistory, Long> {
    
    List<RequestHistory> findByRequestIdOrderByCreatedAtAsc(Long requestId);
}
