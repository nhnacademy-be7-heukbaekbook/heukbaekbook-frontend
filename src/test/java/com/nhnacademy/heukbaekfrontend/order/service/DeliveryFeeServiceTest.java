package com.nhnacademy.heukbaekfrontend.order.service;

import com.nhnacademy.heukbaekfrontend.order.client.DeliveryFeeClient;
import com.nhnacademy.heukbaekfrontend.order.dto.request.DeliveryFeeCreateRequest;
import com.nhnacademy.heukbaekfrontend.order.dto.request.DeliveryFeeUpdateRequest;
import com.nhnacademy.heukbaekfrontend.order.dto.response.DeliveryFeeCreateResponse;
import com.nhnacademy.heukbaekfrontend.order.dto.response.DeliveryFeeDeleteResponse;
import com.nhnacademy.heukbaekfrontend.order.dto.response.DeliveryFeeDetailResponse;
import com.nhnacademy.heukbaekfrontend.order.dto.response.DeliveryFeeUpdateResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

class DeliveryFeeServiceTest {

    @Mock
    private DeliveryFeeClient deliveryFeeClient;

    @InjectMocks
    private DeliveryFeeService deliveryFeeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterDeliveryFee() {
        // Mock Data
        DeliveryFeeCreateRequest request = new DeliveryFeeCreateRequest("Standard", BigDecimal.valueOf(5000), BigDecimal.valueOf(30000));
        DeliveryFeeCreateResponse response = new DeliveryFeeCreateResponse("Standard", BigDecimal.valueOf(5000), BigDecimal.valueOf(30000));

        when(deliveryFeeClient.createDeliveryFee(any(DeliveryFeeCreateRequest.class)))
                .thenReturn(ResponseEntity.ok(response));

        // Test
        ResponseEntity<DeliveryFeeCreateResponse> result = deliveryFeeService.registerDeliveryFee(request);

        // Assert
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().name()).isEqualTo("Standard");
        assertThat(result.getBody().fee()).isEqualTo(BigDecimal.valueOf(5000));
    }

    @Test
    void testGetDeliveryFees() {
        // Mock Data
        DeliveryFeeDetailResponse detail = new DeliveryFeeDetailResponse(1L, "Standard", BigDecimal.valueOf(5000), BigDecimal.valueOf(30000));
        Page<DeliveryFeeDetailResponse> page = new PageImpl<>(List.of(detail));

        when(deliveryFeeClient.getDeliveryFees(any(Pageable.class)))
                .thenReturn(ResponseEntity.ok(page));

        // Test
        Page<DeliveryFeeDetailResponse> result = deliveryFeeService.getDeliveryFees(Pageable.unpaged());

        // Assert
        assertThat(result.getContent()).isNotEmpty();
        assertThat(result.getContent().get(0).name()).isEqualTo("Standard");
    }

    @Test
    void testGetDeliveryFee() {
        // Mock Data
        DeliveryFeeDetailResponse detail = new DeliveryFeeDetailResponse(1L, "Standard", BigDecimal.valueOf(5000), BigDecimal.valueOf(30000));

        when(deliveryFeeClient.getDeliveryFee(anyLong()))
                .thenReturn(ResponseEntity.ok(detail));

        // Test
        DeliveryFeeDetailResponse result = deliveryFeeService.getDeliveryFee(1L);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo("Standard");
        assertThat(result.fee()).isEqualTo(BigDecimal.valueOf(5000));
    }

    @Test
    void testUpdateDeliveryFee() {
        // Mock Data
        DeliveryFeeUpdateRequest request = new DeliveryFeeUpdateRequest("Express", BigDecimal.valueOf(7000), BigDecimal.valueOf(50000));
        DeliveryFeeUpdateResponse response = new DeliveryFeeUpdateResponse("Express", BigDecimal.valueOf(7000), BigDecimal.valueOf(50000));

        when(deliveryFeeClient.updateDeliveryFee(anyLong(), any(DeliveryFeeUpdateRequest.class)))
                .thenReturn(ResponseEntity.ok(response));

        // Test
        ResponseEntity<DeliveryFeeUpdateResponse> result = deliveryFeeService.updateDeliveryFee(1L, request);

        // Assert
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().name()).isEqualTo("Express");
        assertThat(result.getBody().fee()).isEqualTo(BigDecimal.valueOf(7000));
    }

    @Test
    void testDeleteDeliveryFee() {
        // Mock Data
        DeliveryFeeDeleteResponse response = new DeliveryFeeDeleteResponse(1L, "Standard", BigDecimal.valueOf(5000), BigDecimal.valueOf(30000));

        when(deliveryFeeClient.deleteDeliveryFee(anyLong()))
                .thenReturn(ResponseEntity.ok(response));

        // Test
        ResponseEntity<DeliveryFeeDeleteResponse> result = deliveryFeeService.deleteDeliveryFee(1L);

        // Assert
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().name()).isEqualTo("Standard");
        assertThat(result.getBody().fee()).isEqualTo(BigDecimal.valueOf(5000));
    }
}
