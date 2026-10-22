package com.murilo.backend.repository;

import com.murilo.backend.domain.enums.RequestStatus;
import com.murilo.backend.domain.model.PurchaseRequest;
import com.murilo.backend.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PurchaseRequestRepository
        extends JpaRepository<PurchaseRequest, Long> {

    Page<PurchaseRequest> findByRequester(
            User requester, 
            Pageable pageable
    );

    Page<PurchaseRequest> findByRequesterAndStatus(
        User requester,
        RequestStatus status,
        Pageable pageable
    );

    Page<PurchaseRequest> findByStatus(
        RequestStatus status,
        Pageable pageable
    );
    
}
