package com.nhnacademy.heukbaekfrontend.cart.client;

import com.nhnacademy.heukbaekfrontend.cart.dto.CartCreateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(value = "cartClient", url = "http://localhost:8082/api/carts")
public interface CartClient {

    @PostMapping
    Long synchronizeCartToDb(List<CartCreateRequest> cartCreateRequests);
}
