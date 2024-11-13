package com.nhnacademy.heukbaekfrontend.point.client;

import com.nhnacademy.heukbaekfrontend.point.dto.PointEarnStandardRequest;
import com.nhnacademy.heukbaekfrontend.point.dto.PointEarnStandardResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "point-earn-standard-client", url = "http://localhost:8082/api/admin/points/earn-standards")
public interface PointEarnStandardClient {
    @GetMapping
    ResponseEntity<List<PointEarnStandardResponse>> getPointEarnStandards();

    @PostMapping
    ResponseEntity<PointEarnStandardResponse> createPointEarnStandard(@RequestBody PointEarnStandardRequest pointEarnStandardRequest);

    @DeleteMapping("/{id}")
    ResponseEntity<String> deletePointEarnStandard(@PathVariable Long id);

    @PutMapping("/{id}")
    ResponseEntity<PointEarnStandardResponse> updatePointEarnStandard(@PathVariable Long id, @RequestBody PointEarnStandardRequest pointEarnStandardRequest);
}
