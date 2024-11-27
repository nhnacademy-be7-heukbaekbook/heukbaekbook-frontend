package com.nhnacademy.heukbaekfrontend.point.service.impl;

import com.nhnacademy.heukbaekfrontend.point.client.PointEarnStandardClient;
import com.nhnacademy.heukbaekfrontend.point.dto.PointEarnStandardRequest;
import com.nhnacademy.heukbaekfrontend.point.dto.PointEarnStandardResponse;
import com.nhnacademy.heukbaekfrontend.point.dto.PointEarnStandardStatus;
import com.nhnacademy.heukbaekfrontend.point.dto.PointEarnType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class PointEarnStandardServiceImplTest {

    @InjectMocks
    private PointEarnStandardServiceImpl pointEarnStandardService;

    @Mock
    private PointEarnStandardClient pointEarnStandardClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetValidStandardsByEvent_Success() {
        // Given
        String eventCode = "EVENT123";
        PointEarnStandardResponse mockResponse = new PointEarnStandardResponse(
                1L,
                "Standard Name",
                BigDecimal.valueOf(100),
                PointEarnStandardStatus.ACTIVATED,
                PointEarnType.FIXED,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(30),
                null
        );

        when(pointEarnStandardClient.getValidStandardsByEvent(anyString()))
                .thenReturn(new ResponseEntity<>(List.of(mockResponse), HttpStatus.OK));

        // When
        List<PointEarnStandardResponse> result = pointEarnStandardService.getValidStandardsByEvent(eventCode);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).name()).isEqualTo("Standard Name");
        verify(pointEarnStandardClient, times(1)).getValidStandardsByEvent(anyString());
    }

    @Test
    void testCreatePointEarnStandard_Success() {
        // Given
        PointEarnStandardRequest request = new PointEarnStandardRequest(
                "Standard Name",
                BigDecimal.valueOf(100),
                PointEarnType.FIXED,
                PointEarnStandardStatus.ACTIVATED,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(30),
                1L
        );

        PointEarnStandardResponse mockResponse = new PointEarnStandardResponse(
                1L,
                "Standard Name",
                BigDecimal.valueOf(100),
                PointEarnStandardStatus.ACTIVATED,
                PointEarnType.FIXED,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(30),
                null
        );

        when(pointEarnStandardClient.createPointEarnStandard(any(PointEarnStandardRequest.class)))
                .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.CREATED));

        // When
        PointEarnStandardResponse result = pointEarnStandardService.createPointEarnStandard(request);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo("Standard Name");
        verify(pointEarnStandardClient, times(1)).createPointEarnStandard(any(PointEarnStandardRequest.class));
    }

    @Test
    void testCreatePointEarnStandard_Failure_NullRequest() {
        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> pointEarnStandardService.createPointEarnStandard(null)
        );

        assertThat(exception.getMessage()).isEqualTo("PointEarnStandardRequest Cannot be null");
    }

    @Test
    void testDeletePointEarnStandard_Success() {
        // Given
        Long id = 1L;

        when(pointEarnStandardClient.deletePointEarnStandard(anyLong()))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        // When
        pointEarnStandardService.deletePointEarnStandard(id);

        // Then
        verify(pointEarnStandardClient, times(1)).deletePointEarnStandard(anyLong());
    }

    @Test
    void testDeletePointEarnStandard_Failure_NullId() {
        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> pointEarnStandardService.deletePointEarnStandard(null)
        );

        assertThat(exception.getMessage()).isEqualTo("id Cannot be null");
    }

    @Test
    void testUpdatePointEarnStandard_Success() {
        // Given
        Long id = 1L;
        PointEarnStandardRequest request = new PointEarnStandardRequest(
                "Updated Standard Name",
                BigDecimal.valueOf(200),
                PointEarnType.FIXED,
                PointEarnStandardStatus.ACTIVATED,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(30),
                1L
        );

        PointEarnStandardResponse mockResponse = new PointEarnStandardResponse(
                id,
                "Updated Standard Name",
                BigDecimal.valueOf(200),
                PointEarnStandardStatus.ACTIVATED,
                PointEarnType.FIXED,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(30),
                null
        );

        when(pointEarnStandardClient.updatePointEarnStandard(anyLong(), any(PointEarnStandardRequest.class)))
                .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

        // When
        PointEarnStandardResponse result = pointEarnStandardService.updatePointEarnStandard(id, request);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo("Updated Standard Name");
        verify(pointEarnStandardClient, times(1)).updatePointEarnStandard(anyLong(), any(PointEarnStandardRequest.class));
    }

    @Test
    void testUpdatePointEarnStandard_Failure_NullRequest() {
        // Given
        Long id = 1L;

        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> pointEarnStandardService.updatePointEarnStandard(id, null)
        );

        assertThat(exception.getMessage()).isEqualTo("PointEarnStandardRequest Cannot be null");
    }
}
