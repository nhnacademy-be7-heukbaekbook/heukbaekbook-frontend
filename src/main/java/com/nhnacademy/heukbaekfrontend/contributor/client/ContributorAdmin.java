package com.nhnacademy.heukbaekfrontend.contributor.client;

import com.nhnacademy.heukbaekfrontend.contributor.dto.request.ContributorCreateRequest;
import com.nhnacademy.heukbaekfrontend.contributor.dto.request.ContributorUpdateRequest;
import com.nhnacademy.heukbaekfrontend.contributor.dto.request.PublisherCreateRequest;
import com.nhnacademy.heukbaekfrontend.contributor.dto.request.PublisherUpdateRequest;
import com.nhnacademy.heukbaekfrontend.contributor.dto.response.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "ContributorAdmin", url = "http://localhost:8082")
public interface ContributorAdmin {

    @PostMapping("/api/admins/contributors")
    ResponseEntity<ContributorCreateResponse> registerContributor(@RequestBody ContributorCreateRequest request);

    @GetMapping("/api/admins/contributors")
    ResponseEntity<Page<ContributorDetailResponse>> getContributors(Pageable pageable);

    @GetMapping("/api/admins/contributors/{contributor-id}")
    ResponseEntity<ContributorDetailResponse> getContributor(@PathVariable(name = "contributor-id") Long contributorId);

    @PutMapping("/api/admins/contributors/{contributor-id}")
    ResponseEntity<ContributorUpdateResponse> updateContributor(
            @PathVariable(name = "contributor-id") Long contributorId,
            @RequestBody ContributorUpdateRequest request);

    @DeleteMapping("/api/admins/contributors/{contributor-id}")
    ResponseEntity<ContributorDeleteResponse> deleteContributor(@PathVariable(name = "contributor-id") Long contributorId);

}