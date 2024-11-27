package com.nhnacademy.heukbaekfrontend.order.client;

import com.nhnacademy.heukbaekfrontend.order.dto.response.WrappingPaperResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(value = "orderClient", url = "http://localhost:8082/api/wrapping-papers")
public interface WrappingPaperClient {

    @GetMapping
    List<WrappingPaperResponse> getAllWrappingPapers();
}
