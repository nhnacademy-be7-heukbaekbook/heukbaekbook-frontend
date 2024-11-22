package com.nhnacademy.heukbaekfrontend.order.service;

import com.nhnacademy.heukbaekfrontend.order.client.DeliveryFeeClient;
import com.nhnacademy.heukbaekfrontend.order.dto.request.DeliveryFeeCreateRequest;
import com.nhnacademy.heukbaekfrontend.order.dto.request.DeliveryFeeUpdateRequest;
import com.nhnacademy.heukbaekfrontend.order.dto.response.DeliveryFeeCreateResponse;
import com.nhnacademy.heukbaekfrontend.order.dto.response.DeliveryFeeDeleteResponse;
import com.nhnacademy.heukbaekfrontend.order.dto.response.DeliveryFeeDetailResponse;
import com.nhnacademy.heukbaekfrontend.order.dto.response.DeliveryFeeUpdateResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class DeliveryFeeService {

    private final DeliveryFeeClient deliveryFeeClient;

    public DeliveryFeeService(DeliveryFeeClient deliveryFeeClient) {
        this.deliveryFeeClient = deliveryFeeClient;
    }

    public ResponseEntity<DeliveryFeeCreateResponse> registerDeliveryFee(DeliveryFeeCreateRequest request) {
        return deliveryFeeClient.createDeliveryFee(request);
    }

    public Page<DeliveryFeeDetailResponse> getDeliveryFees(Pageable pageable) {
        return deliveryFeeClient.getDeliveryFees(pageable).getBody();
    }

    public DeliveryFeeDetailResponse getDeliveryFee(Long deliveryId) {
        return deliveryFeeClient.getDeliveryFee(deliveryId).getBody();
    }

    public ResponseEntity<DeliveryFeeUpdateResponse> updateDeliveryFee(Long deliveryFeeId, DeliveryFeeUpdateRequest request) {
        return deliveryFeeClient.updateDeliveryFee(deliveryFeeId, request);
    }

    public ResponseEntity<DeliveryFeeDeleteResponse> deleteDeliveryFee(Long deliveryFeeId) {
        return deliveryFeeClient.deleteDeliveryFee(deliveryFeeId);
    }
}
