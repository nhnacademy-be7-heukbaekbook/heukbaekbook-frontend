package com.nhnacademy.heukbaekfrontend.contributor.client;

import com.nhnacademy.heukbaekfrontend.contributor.dto.request.PublisherCreateRequest;
import com.nhnacademy.heukbaekfrontend.contributor.dto.request.PublisherUpdateRequest;
import com.nhnacademy.heukbaekfrontend.contributor.dto.response.PublisherCreateResponse;
import com.nhnacademy.heukbaekfrontend.contributor.dto.response.PublisherDeleteResponse;
import com.nhnacademy.heukbaekfrontend.contributor.dto.response.PublisherDetailResponse;
import com.nhnacademy.heukbaekfrontend.contributor.dto.response.PublisherUpdateResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "PublisherAdmin", url = "http://localhost:8082")
public interface PublisherAdmin {

    @PostMapping("/api/admins/publishers")
    ResponseEntity<PublisherCreateResponse> registerPublisher(@RequestBody PublisherCreateRequest request);

    @GetMapping("/api/admins/publishers")
    Page<PublisherDetailResponse> getPublishers(Pageable pageable);

    @PutMapping("/api/admins/publishers/{publisher-id}")
    ResponseEntity<PublisherUpdateResponse> updatePublisher(
            @PathVariable(name = "publisher-id") Long publisherId,
            @RequestBody PublisherUpdateRequest request);

    @DeleteMapping("/api/admins/publishers/{publisher-id}")
    ResponseEntity<PublisherDeleteResponse> deletePublisher(@PathVariable(name = "publisher-id") Long publisherId);

    @GetMapping("/api/admins/publishers/{publisher-id}")
    ResponseEntity<PublisherDetailResponse> getPublisher(@PathVariable(name = "publisher-id") Long publisherId);
}
