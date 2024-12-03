package com.nhnacademy.heukbaekfrontend.point.service;


import com.nhnacademy.heukbaekfrontend.point.dto.PointEarnStandardRequest;
import com.nhnacademy.heukbaekfrontend.point.dto.PointEarnStandardResponse;

import java.util.List;

public interface PointEarnStandardService {
    List<PointEarnStandardResponse> getValidStandardsByEvent(String eventCode);

    PointEarnStandardResponse createPointEarnStandard(PointEarnStandardRequest pointEarnStandardRequest);

    void deletePointEarnStandard(Long id);

    PointEarnStandardResponse
    updatePointEarnStandard(Long id, PointEarnStandardRequest pointEarnStandardRequest);
}
