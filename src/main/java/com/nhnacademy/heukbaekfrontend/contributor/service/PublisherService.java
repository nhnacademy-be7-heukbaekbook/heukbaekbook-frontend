package com.nhnacademy.heukbaekfrontend.contributor.service;

import com.nhnacademy.heukbaekfrontend.contributor.client.PublisherAdmin;
import com.nhnacademy.heukbaekfrontend.contributor.dto.request.PublisherCreateRequest;
import com.nhnacademy.heukbaekfrontend.contributor.dto.request.PublisherUpdateRequest;
import com.nhnacademy.heukbaekfrontend.contributor.dto.response.PublisherCreateResponse;
import com.nhnacademy.heukbaekfrontend.contributor.dto.response.PublisherDeleteResponse;
import com.nhnacademy.heukbaekfrontend.contributor.dto.response.PublisherDetailResponse;
import com.nhnacademy.heukbaekfrontend.contributor.dto.response.PublisherUpdateResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PublisherService {

    private final PublisherAdmin publisherAdmin;

    public PublisherService(PublisherAdmin publisherAdmin) {
        this.publisherAdmin = publisherAdmin;
    }

    public ResponseEntity<PublisherCreateResponse> registerPublisher(PublisherCreateRequest request) {
        return publisherAdmin.registerPublisher(request);
    }

    public Page<PublisherDetailResponse> getPublishers(Pageable pageable) {
        return publisherAdmin.getPublishers(pageable);
    }

    public ResponseEntity<PublisherUpdateResponse> updatePublisher(Long publisherId, PublisherUpdateRequest request) {
        return publisherAdmin.updatePublisher(publisherId, request);
    }

    public ResponseEntity<PublisherDeleteResponse> deletePublisher(Long publisherId) {
        return publisherAdmin.deletePublisher(publisherId);
    }

    public PublisherDetailResponse getPublisherById(Long publisherId) {
        return publisherAdmin.getPublisher(publisherId).getBody();
    }
}
