package com.nhnacademy.heukbaekfrontend.order.client;

import com.nhnacademy.heukbaekfrontend.order.dto.request.DeliveryFeeCreateRequest;
import com.nhnacademy.heukbaekfrontend.order.dto.request.DeliveryFeeUpdateRequest;
import com.nhnacademy.heukbaekfrontend.order.dto.response.DeliveryFeeCreateResponse;
import com.nhnacademy.heukbaekfrontend.order.dto.response.DeliveryFeeDeleteResponse;
import com.nhnacademy.heukbaekfrontend.order.dto.response.DeliveryFeeDetailResponse;
import com.nhnacademy.heukbaekfrontend.order.dto.response.DeliveryFeeUpdateResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@FeignClient(name = "deliveryFeeClient", url = "http://localhost:8082/api")
public interface DeliveryFeeClient {

    @PostMapping("/admin/delivery-fee")
    ResponseEntity<DeliveryFeeCreateResponse> createDeliveryFee(@RequestBody DeliveryFeeCreateRequest request);

    @GetMapping("/admin/delivery-fee/{delivery-fee-id}")
    ResponseEntity<DeliveryFeeDetailResponse> getDeliveryFee(@PathVariable("delivery-fee-id") Long deliveryFeeId);

    @PutMapping("/admin/delivery-fee/{delivery-fee-id}")
    ResponseEntity<DeliveryFeeUpdateResponse> updateDeliveryFee(
            @PathVariable("delivery-fee-id") Long deliveryFeeId,
            @RequestBody DeliveryFeeUpdateRequest request);

    @DeleteMapping("/admin/delivery-fee/{delivery-fee-id}")
    ResponseEntity<DeliveryFeeDeleteResponse> deleteDeliveryFee(@PathVariable("delivery-fee-id") Long deliveryFeeId);

    @GetMapping("/admin/delivery-fee")
    ResponseEntity<Page<DeliveryFeeDetailResponse>> getDeliveryFees(Pageable pageable);

    @GetMapping("/delivery-fees")
    BigDecimal getDeliveryFeeByMinimumOrderAmount(@RequestParam BigDecimal minimumOrderAmount);

}
