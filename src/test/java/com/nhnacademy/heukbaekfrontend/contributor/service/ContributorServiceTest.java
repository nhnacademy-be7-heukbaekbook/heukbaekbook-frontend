package com.nhnacademy.heukbaekfrontend.contributor.service;

import com.nhnacademy.heukbaekfrontend.contributor.client.ContributorAdmin;
import com.nhnacademy.heukbaekfrontend.contributor.dto.request.ContributorCreateRequest;
import com.nhnacademy.heukbaekfrontend.contributor.dto.request.ContributorUpdateRequest;
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

class ContributorServiceTest {

    @InjectMocks
    private ContributorService contributorService;

    @Mock
    private ContributorAdmin contributorAdmin;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterContributor() {
        ContributorCreateRequest request = new ContributorCreateRequest("New Contributor", "Description");
        ContributorCreateResponse response = new ContributorCreateResponse("1", "New Contributor");

        when(contributorAdmin.registerContributor(any(ContributorCreateRequest.class)))
                .thenReturn(ResponseEntity.ok(response));

        ResponseEntity<ContributorCreateResponse> result = contributorService.registerContributor(request);

        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().name()).isEqualTo("1");
        verify(contributorAdmin, times(1)).registerContributor(request);
    }

    @Test
    void testGetContributors() {
        Pageable pageable = mock(Pageable.class);
        Page<ContributorDetailResponse> responsePage = new PageImpl<>(List.of(
                new ContributorDetailResponse(1L, "Contributor A", "Description")
        ));

        when(contributorAdmin.getContributors(any(Pageable.class)))
                .thenReturn(ResponseEntity.ok(responsePage));

        Page<ContributorDetailResponse> result = contributorService.getContributors(pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).name()).isEqualTo("Contributor A");
        verify(contributorAdmin, times(1)).getContributors(pageable);
    }

    @Test
    void testUpdateContributor() {
        Long contributorId = 1L;
        ContributorUpdateRequest request = new ContributorUpdateRequest("Updated Contributor", "Updated Description");
        ContributorUpdateResponse response = new ContributorUpdateResponse("1", "Updated Contributor");

        when(contributorAdmin.updateContributor(anyLong(), any(ContributorUpdateRequest.class)))
                .thenReturn(ResponseEntity.ok(response));

        ResponseEntity<ContributorUpdateResponse> result = contributorService.updateContributor(contributorId, request);

        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().name()).isEqualTo("1");
        verify(contributorAdmin, times(1)).updateContributor(contributorId, request);
    }

    @Test
    void testDeleteContributor() {
        Long contributorId = 1L;
        ContributorDeleteResponse response = new ContributorDeleteResponse("Contributor deleted successfully");

        when(contributorAdmin.deleteContributor(anyLong()))
                .thenReturn(ResponseEntity.ok(response));

        ResponseEntity<ContributorDeleteResponse> result = contributorService.deleteContributor(contributorId);

        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().name()).isEqualTo("Contributor deleted successfully");
        verify(contributorAdmin, times(1)).deleteContributor(contributorId);
    }

    @Test
    void testGetContributor() {
        Long contributorId = 1L;
        ContributorDetailResponse response = new ContributorDetailResponse(1L, "Contributor A", "Description");

        when(contributorAdmin.getContributor(anyLong()))
                .thenReturn(ResponseEntity.ok(response));

        ContributorDetailResponse result = contributorService.getContributor(contributorId);

        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo("Contributor A");
        verify(contributorAdmin, times(1)).getContributor(contributorId);
    }
}
