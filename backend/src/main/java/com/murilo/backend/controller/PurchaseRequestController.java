package com.murilo.backend.controller;

import com.murilo.backend.domain.enums.RequestStatus;
import com.murilo.backend.domain.model.User;
import com.murilo.backend.dto.common.ApiResponse;
import com.murilo.backend.dto.request.CreatePurchaseRequestRequest;
import com.murilo.backend.dto.request.PurchaseRequestResponse;
import com.murilo.backend.dto.request.RequestActionRequest;
import com.murilo.backend.dto.request.RequestHistoryResponse;
import com.murilo.backend.service.PurchaseRequestService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/requests")
public class PurchaseRequestController {

    private final PurchaseRequestService purchaseRequestService;

    public PurchaseRequestController(
            PurchaseRequestService purchaseRequestService
    ) {
        this.purchaseRequestService =
                purchaseRequestService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PurchaseRequestResponse>> create(
            @Valid @RequestBody CreatePurchaseRequestRequest request,
            @AuthenticationPrincipal User currentUser
    ) {

        PurchaseRequestResponse response =
                purchaseRequestService.create(
                        request,
                        currentUser
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        new ApiResponse<>(
                                "Purchase request created successfully.",
                                response
                        )
                );
    }

    @GetMapping
    public ApiResponse<Page<PurchaseRequestResponse>> list(
            @RequestParam(required = false)
            RequestStatus status,

            @PageableDefault(
                    size = 10,
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
            )
            Pageable pageable,

            @AuthenticationPrincipal
            User currentUser
    ) {

        return new ApiResponse<>(
                "Purchase requests retrieved successfully.",
                purchaseRequestService.list(
                        currentUser,
                        status,
                        pageable
                )
        );
    }

    @GetMapping("/{id}")
    public ApiResponse<PurchaseRequestResponse> findById(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser
    ) {

        return new ApiResponse<>(
                "Purchase request retrieved successfully.",
                purchaseRequestService.findById(
                        id,
                        currentUser
                )
        );
    }

    @PostMapping("/{id}/approve")
    public ApiResponse<PurchaseRequestResponse> approve(
            @PathVariable Long id,
            @Valid @RequestBody RequestActionRequest request,
            @AuthenticationPrincipal User currentUser
    ) {

        return new ApiResponse<>(
                "Purchase request approved successfully.",
                purchaseRequestService.approve(
                        id,
                        request.comment(),
                        currentUser
                )
        );
    }

    @PostMapping("/{id}/reject")
    public ApiResponse<PurchaseRequestResponse> reject(
            @PathVariable Long id,
            @Valid @RequestBody RequestActionRequest request,
            @AuthenticationPrincipal User currentUser
    ) {

        return new ApiResponse<>(
                "Purchase request rejected successfully.",
                purchaseRequestService.reject(
                        id,
                        request.comment(),
                        currentUser
                )
        );
    }

    @PostMapping("/{id}/cancel")
    public ApiResponse<PurchaseRequestResponse> cancel(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser
    ) {

        return new ApiResponse<>(
                "Purchase request cancelled successfully.",
                purchaseRequestService.cancel(
                        id,
                        currentUser
                )
        );
    }

    @GetMapping("/{id}/history")
    public ApiResponse<List<RequestHistoryResponse>> history(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser
    ) {

        return new ApiResponse<>(
                "Request history retrieved successfully.",
                purchaseRequestService.history(
                        id,
                        currentUser
                )
        );
    }
}
