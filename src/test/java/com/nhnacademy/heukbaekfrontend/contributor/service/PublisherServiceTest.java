package com.nhnacademy.heukbaekfrontend.contributor.service;

import com.nhnacademy.heukbaekfrontend.contributor.client.PublisherAdmin;
import com.nhnacademy.heukbaekfrontend.contributor.dto.request.PublisherCreateRequest;
import com.nhnacademy.heukbaekfrontend.contributor.dto.request.PublisherUpdateRequest;
import com.nhnacademy.heukbaekfrontend.contributor.dto.response.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class PublisherServiceTest {

    @InjectMocks
    private PublisherService publisherService;

    @Mock
    private PublisherAdmin publisherAdmin;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterPublisher() {
        PublisherCreateRequest request = new PublisherCreateRequest("New Publisher");
        PublisherCreateResponse response = new PublisherCreateResponse("New Publisher");

        when(publisherAdmin.registerPublisher(any(PublisherCreateRequest.class)))
                .thenReturn(ResponseEntity.ok(response));

        ResponseEntity<PublisherCreateResponse> result = publisherService.registerPublisher(request);

        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().name()).isEqualTo("New Publisher");
        verify(publisherAdmin, times(1)).registerPublisher(request);
    }

    @Test
    void testGetPublishers() {
        Pageable pageable = mock(Pageable.class);
        Page<PublisherDetailResponse> responsePage = new PageImpl<>(List.of(
                new PublisherDetailResponse(1L, "Publisher A")
        ));

        when(publisherAdmin.getPublishers(any(Pageable.class))).thenReturn(responsePage);

        Page<PublisherDetailResponse> result = publisherService.getPublishers(pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).name()).isEqualTo("Publisher A");
        verify(publisherAdmin, times(1)).getPublishers(pageable);
    }

    @Test
    void testUpdatePublisher() {
        Long publisherId = 1L;
        PublisherUpdateRequest request = new PublisherUpdateRequest("Updated Publisher");
        PublisherUpdateResponse response = new PublisherUpdateResponse("Updated Publisher");

        when(publisherAdmin.updatePublisher(anyLong(), any(PublisherUpdateRequest.class)))
                .thenReturn(ResponseEntity.ok(response));

        ResponseEntity<PublisherUpdateResponse> result = publisherService.updatePublisher(publisherId, request);

        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().name()).isEqualTo("Updated Publisher");
        verify(publisherAdmin, times(1)).updatePublisher(publisherId, request);
    }

    @Test
    void testDeletePublisher() {
        Long publisherId = 1L;
        PublisherDeleteResponse response = new PublisherDeleteResponse("Publisher deleted successfully");

        when(publisherAdmin.deletePublisher(anyLong())).thenReturn(ResponseEntity.ok(response));

        ResponseEntity<PublisherDeleteResponse> result = publisherService.deletePublisher(publisherId);

        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().name()).isEqualTo("Publisher deleted successfully");
        verify(publisherAdmin, times(1)).deletePublisher(publisherId);
    }

    @Test
    void testGetPublisherById() {
        Long publisherId = 1L;
        PublisherDetailResponse response = new PublisherDetailResponse(1L, "Publisher A");

        when(publisherAdmin.getPublisher(anyLong())).thenReturn(ResponseEntity.ok(response));

        PublisherDetailResponse result = publisherService.getPublisherById(publisherId);

        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo("Publisher A");
        verify(publisherAdmin, times(1)).getPublisher(publisherId);
    }
}
