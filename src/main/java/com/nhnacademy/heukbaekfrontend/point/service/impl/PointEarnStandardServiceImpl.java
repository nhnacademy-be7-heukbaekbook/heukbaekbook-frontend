package com.nhnacademy.heukbaekfrontend.point.service.impl;

import com.nhnacademy.heukbaekfrontend.point.client.PointEarnStandardClient;
import com.nhnacademy.heukbaekfrontend.point.dto.PointEarnStandardRequest;
import com.nhnacademy.heukbaekfrontend.point.dto.PointEarnStandardResponse;
import com.nhnacademy.heukbaekfrontend.point.service.PointEarnStandardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PointEarnStandardServiceImpl implements PointEarnStandardService {

    private final PointEarnStandardClient pointEarnStandardClient;

    @Override
    public List<PointEarnStandardResponse> getPointEarnStandards() {
        ResponseEntity<List<PointEarnStandardResponse>> pointEarnStandards = pointEarnStandardClient.getPointEarnStandards();

        return pointEarnStandards.getBody();
    }

    @Override
    public PointEarnStandardResponse createPointEarnStandard(PointEarnStandardRequest pointEarnStandardRequest) {
        if (pointEarnStandardRequest == null) {
            throw new IllegalArgumentException("PointEarnStandardRequest Cannot be null");
        }

        ResponseEntity<PointEarnStandardResponse> earnStandard = pointEarnStandardClient.createPointEarnStandard(pointEarnStandardRequest);

        return earnStandard.getBody();
    }

    @Override
    public void deletePointEarnStandard(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("id Cannot be null");
        }

        pointEarnStandardClient.deletePointEarnStandard(id);
    }

    @Override
    public PointEarnStandardResponse updatePointEarnStandard(Long id, PointEarnStandardRequest pointEarnStandardRequest) {
        if (pointEarnStandardRequest == null) {
            throw new IllegalArgumentException("PointEarnStandardRequest Cannot be null");
        }

        ResponseEntity<PointEarnStandardResponse> earnStandard = pointEarnStandardClient.updatePointEarnStandard(id, pointEarnStandardRequest);

        return earnStandard.getBody();
    }
}
