package com.nhnacademy.heukbaekfrontend.contributor.service;

import com.nhnacademy.heukbaekfrontend.contributor.client.ContributorAdmin;
import com.nhnacademy.heukbaekfrontend.contributor.client.ContributorAdmin;
import com.nhnacademy.heukbaekfrontend.contributor.dto.request.ContributorCreateRequest;
import com.nhnacademy.heukbaekfrontend.contributor.dto.request.ContributorUpdateRequest;
import com.nhnacademy.heukbaekfrontend.contributor.dto.response.ContributorCreateResponse;
import com.nhnacademy.heukbaekfrontend.contributor.dto.response.ContributorDeleteResponse;
import com.nhnacademy.heukbaekfrontend.contributor.dto.response.ContributorDetailResponse;
import com.nhnacademy.heukbaekfrontend.contributor.dto.response.ContributorUpdateResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ContributorService {

    private final ContributorAdmin ContributorAdmin;

    public ContributorService(ContributorAdmin ContributorAdmin) {
        this.ContributorAdmin = ContributorAdmin;
    }

    public ResponseEntity<ContributorCreateResponse> registerContributor(ContributorCreateRequest request) {
        return ContributorAdmin.registerContributor(request);
    }

    public Page<ContributorDetailResponse> getContributors(Pageable pageable) {
        return ContributorAdmin.getContributors(pageable).getBody();
    }

    public ResponseEntity<ContributorUpdateResponse> updateContributor(Long contributorId, ContributorUpdateRequest request) {
        return ContributorAdmin.updateContributor(contributorId, request);
    }

    public ResponseEntity<ContributorDeleteResponse> deleteContributor(Long contributorId) {
        return ContributorAdmin.deleteContributor(contributorId);
    }

    public ContributorDetailResponse getContributor(Long contributorId) {
        return ContributorAdmin.getContributor(contributorId).getBody();
    }
}