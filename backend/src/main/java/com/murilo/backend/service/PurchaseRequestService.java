package com.murilo.backend.service;

import com.murilo.backend.domain.enums.ApprovalLevel;
import com.murilo.backend.domain.enums.ApproverLevel;
import com.murilo.backend.domain.enums.HistoryAction;
import com.murilo.backend.domain.enums.RequestStatus;
import com.murilo.backend.domain.enums.Role;
import com.murilo.backend.domain.model.PurchaseRequest;
import com.murilo.backend.domain.model.RequestHistory;
import com.murilo.backend.domain.model.User;
import com.murilo.backend.dto.request.CreatePurchaseRequestRequest;
import com.murilo.backend.dto.request.PurchaseRequestResponse;
import com.murilo.backend.dto.request.RequestHistoryResponse;
import com.murilo.backend.exception.BusinessRuleException;
import com.murilo.backend.exception.ForbiddenException;
import com.murilo.backend.exception.ResourceNotFoundException;
import com.murilo.backend.repository.PurchaseRequestRepository;
import com.murilo.backend.repository.RequestHistoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PurchaseRequestService {

    private static final BigDecimal LEVEL_1_LIMIT =
            new BigDecimal("1000.00");

    private static final BigDecimal LEVEL_2_LIMIT =
            new BigDecimal("10000.00");

    private final PurchaseRequestRepository purchaseRequestRepository;
    private final RequestHistoryRepository requestHistoryRepository;

    public PurchaseRequestService(
            PurchaseRequestRepository purchaseRequestRepository,
            RequestHistoryRepository requestHistoryRepository
    ) {
        this.purchaseRequestRepository = purchaseRequestRepository;
        this.requestHistoryRepository = requestHistoryRepository;
    }

    @Transactional
    public PurchaseRequestResponse create(
            CreatePurchaseRequestRequest input,
            User currentUser
    ) {

        if (currentUser.getRole() != Role.SOLICITANTE) {
            throw new ForbiddenException(
                    "Only requesters can create purchase requests."
            );
        }

        PurchaseRequest request = new PurchaseRequest();

        request.setTitle(input.title().trim());
        request.setDescription(input.description().trim());
        request.setAmount(input.amount());
        request.setCategory(input.category());
        request.setStatus(RequestStatus.PENDING);

        request.setRequiredApprovalLevel(
                calculateRequiredLevel(input.amount())
        );

        request.setRequester(currentUser);

        PurchaseRequest saved =
                purchaseRequestRepository.save(request);

        recordHistory(
                saved,
                currentUser,
                HistoryAction.CREATED,
                null
        );

        return PurchaseRequestResponse.from(saved);
    }

    @Transactional(readOnly = true)
    public Page<PurchaseRequestResponse> list(
            User currentUser,
            RequestStatus status,
            Pageable pageable
    ) {

        Page<PurchaseRequest> page;

        if (currentUser.getRole() == Role.SOLICITANTE) {

            if (status == null) {
                page = purchaseRequestRepository
                        .findByRequester(currentUser, pageable);
            } else {
                page = purchaseRequestRepository
                        .findByRequesterAndStatus(
                                currentUser,
                                status,
                                pageable
                        );
            }

        } else {

            if (status == null) {
                page = purchaseRequestRepository.findAll(pageable);
            } else {
                page = purchaseRequestRepository
                        .findByStatus(status, pageable);
            }
        }

        return page.map(PurchaseRequestResponse::from);
    }

    @Transactional(readOnly = true)
    public PurchaseRequestResponse findById(
            Long id,
            User currentUser
    ) {

        PurchaseRequest request =
                findVisibleRequest(id, currentUser);

        return PurchaseRequestResponse.from(request);
    }

    @Transactional
    public PurchaseRequestResponse approve(
            Long id,
            String comment,
            User currentUser
    ) {

        PurchaseRequest request =
                findVisibleRequest(id, currentUser);

        validateApprover(currentUser);

        ensurePending(request);

        ensureApprovalLevelCompatible(
                request,
                currentUser
        );

        request.setStatus(RequestStatus.APPROVED);

        PurchaseRequest saved =
                purchaseRequestRepository.save(request);

        recordHistory(
                saved,
                currentUser,
                HistoryAction.APPROVED,
                normalizeComment(comment)
        );

        return PurchaseRequestResponse.from(saved);
    }

    @Transactional
    public PurchaseRequestResponse reject(
            Long id,
            String comment,
            User currentUser
    ) {

        PurchaseRequest request =
                findVisibleRequest(id, currentUser);

        validateApprover(currentUser);

        ensurePending(request);

        ensureApprovalLevelCompatible(
                request,
                currentUser
        );

        request.setStatus(RequestStatus.REJECTED);

        PurchaseRequest saved =
                purchaseRequestRepository.save(request);

        recordHistory(
                saved,
                currentUser,
                HistoryAction.REJECTED,
                normalizeComment(comment)
        );

        return PurchaseRequestResponse.from(saved);
    }

    @Transactional
    public PurchaseRequestResponse cancel(
            Long id,
            User currentUser
    ) {

        PurchaseRequest request =
                purchaseRequestRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Purchase request not found."
                                )
                        );

        boolean admin =
                currentUser.getRole() == Role.ADMIN;

        boolean owner =
                currentUser.getRole() == Role.SOLICITANTE
                        && request.getRequester()
                        .getId()
                        .equals(currentUser.getId());

        if (!admin && !owner) {
            throw new ForbiddenException(
                    "You are not allowed to cancel this request."
            );
        }

        ensurePending(request);

        request.setStatus(RequestStatus.CANCELLED);

        PurchaseRequest saved =
                purchaseRequestRepository.save(request);

        recordHistory(
                saved,
                currentUser,
                HistoryAction.CANCELLED,
                null
        );

        return PurchaseRequestResponse.from(saved);
    }

    @Transactional(readOnly = true)
    public List<RequestHistoryResponse> history(
            Long requestId,
            User currentUser
    ) {

        findVisibleRequest(requestId, currentUser);

        return requestHistoryRepository
                .findByRequestIdOrderByCreatedAtAsc(requestId)
                .stream()
                .map(RequestHistoryResponse::from)
                .toList();
    }

    private PurchaseRequest findVisibleRequest(
            Long id,
            User currentUser
    ) {

        PurchaseRequest request =
                purchaseRequestRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Purchase request not found."
                                )
                        );

        if (currentUser.getRole() == Role.SOLICITANTE
                && !request.getRequester()
                .getId()
                .equals(currentUser.getId())) {

            throw new ForbiddenException(
                    "You cannot access another requester's request."
            );
        }

        return request;
    }

    private ApprovalLevel calculateRequiredLevel(
            BigDecimal amount
    ) {

        if (amount.compareTo(LEVEL_1_LIMIT) <= 0) {
            return ApprovalLevel.NIVEL_1;
        }

        if (amount.compareTo(LEVEL_2_LIMIT) <= 0) {
            return ApprovalLevel.NIVEL_2;
        }

        return ApprovalLevel.NIVEL_3;
    }

    private void validateApprover(User currentUser) {

        if (currentUser.getRole() != Role.APROVADOR
                && currentUser.getRole() != Role.ADMIN) {

            throw new ForbiddenException(
                    "Only approvers or administrators can perform this action."
            );
        }
    }

    private void ensurePending(PurchaseRequest request) {

        if (request.getStatus() != RequestStatus.PENDING) {
            throw new BusinessRuleException(
                    "Only pending requests can change state."
            );
        }
    }

    private void ensureApprovalLevelCompatible(
            PurchaseRequest request,
            User currentUser
    ) {

        if (currentUser.getRole() == Role.ADMIN) {
            return;
        }

        ApproverLevel userLevel =
                currentUser.getApproverLevel();

        ApprovalLevel requiredLevel =
                request.getRequiredApprovalLevel();

        boolean allowed = switch (requiredLevel) {

            case NIVEL_1 ->
                    userLevel == ApproverLevel.NIVEL_1
                            || userLevel == ApproverLevel.NIVEL_2;

            case NIVEL_2 ->
                    userLevel == ApproverLevel.NIVEL_2;

            case NIVEL_3 ->
                    false;
        };

        if (!allowed) {
            throw new BusinessRuleException(
                    "Insufficient approval level for this request."
            );
        }
    }

    private void recordHistory(
            PurchaseRequest request,
            User actor,
            HistoryAction action,
            String comment
    ) {

        RequestHistory history = new RequestHistory();

        history.setRequest(request);
        history.setActor(actor);
        history.setAction(action);
        history.setComment(comment);

        requestHistoryRepository.save(history);
    }

    private String normalizeComment(String comment) {

        if (comment == null || comment.isBlank()) {
            return null;
        }

        return comment.trim();
    }
}
