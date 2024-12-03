package com.nhnacademy.heukbaekfrontend.order.controller;

import com.nhnacademy.heukbaekfrontend.order.dto.request.DeliveryFeeCreateRequest;
import com.nhnacademy.heukbaekfrontend.order.dto.request.DeliveryFeeUpdateRequest;
import com.nhnacademy.heukbaekfrontend.order.dto.response.*;
import com.nhnacademy.heukbaekfrontend.order.service.DeliveryFeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DeliveryFeeController.class)
class DeliveryFeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DeliveryFeeService deliveryFeeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(new DeliveryFeeController(deliveryFeeService))
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    void testViewDeliveryFee() throws Exception {
        mockMvc.perform(get("/admin/delivery-fee/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("order/admin/registerDeliveryFee"))
                .andExpect(model().attributeExists("deliveryFeeCreateRequest"));
    }

    @Test
    void testRegisterDeliveryFee_Success() throws Exception {
        DeliveryFeeCreateRequest request = new DeliveryFeeCreateRequest("Express", BigDecimal.valueOf(5000), BigDecimal.valueOf(10000));
        DeliveryFeeCreateResponse response = new DeliveryFeeCreateResponse("Express", BigDecimal.valueOf(5000), BigDecimal.valueOf(10000));

        when(deliveryFeeService.registerDeliveryFee(any(DeliveryFeeCreateRequest.class))).thenReturn(ResponseEntity.ok(response));

        mockMvc.perform(post("/admin/delivery-fee/register")
                        .flashAttr("deliveryFeeCreateRequest", request))
                .andExpect(status().isOk())
                .andExpect(view().name("order/admin/registerDeliveryFee"))
                .andExpect(model().attributeExists("success"));
    }

    @Test
    void testRegisterDeliveryFee_Failure() throws Exception {
        DeliveryFeeCreateRequest request = new DeliveryFeeCreateRequest("Express", BigDecimal.valueOf(5000), BigDecimal.valueOf(10000));

        when(deliveryFeeService.registerDeliveryFee(any(DeliveryFeeCreateRequest.class)))
                .thenReturn(ResponseEntity.status(400).build());

        mockMvc.perform(post("/admin/delivery-fee/register")
                        .flashAttr("deliveryFeeCreateRequest", request))
                .andExpect(status().isOk())
                .andExpect(view().name("order/admin/registerDeliveryFee"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("deliveryCreateRequest", request));
    }

    @Test
    void testGetDeliveryFees() throws Exception {
        Page<DeliveryFeeDetailResponse> response = new PageImpl<>(List.of(
                new DeliveryFeeDetailResponse(1L, "Express", BigDecimal.valueOf(5000), BigDecimal.valueOf(10000))
        ));

        when(deliveryFeeService.getDeliveryFees(any(Pageable.class))).thenReturn(response);

        mockMvc.perform(get("/admin/delivery-fee")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "id,asc"))
                .andExpect(status().isOk())
                .andExpect(view().name("order/admin/viewAllDeliveryFees"))
                .andExpect(model().attributeExists("deliveryFees"))
                .andExpect(model().attributeExists("page"))
                .andExpect(model().attributeExists("size"))
                .andExpect(model().attributeExists("sort"));
    }

    @Test
    void testUpdateDeliveryFeeForm() throws Exception {
        DeliveryFeeDetailResponse detailResponse = new DeliveryFeeDetailResponse(1L, "Express", BigDecimal.valueOf(5000), BigDecimal.valueOf(10000));
        when(deliveryFeeService.getDeliveryFee(anyLong())).thenReturn(detailResponse);

        mockMvc.perform(get("/admin/delivery-fee/1/update"))
                .andExpect(status().isOk())
                .andExpect(view().name("order/admin/updateDeliveryFee"))
                .andExpect(model().attributeExists("deliveryFeeUpdateRequest"))
                .andExpect(model().attributeExists("deliveryFeeId"));
    }

    @Test
    void testUpdateDeliveryFee_Success() throws Exception {
        DeliveryFeeUpdateRequest request = new DeliveryFeeUpdateRequest("Express", BigDecimal.valueOf(5000), BigDecimal.valueOf(10000));
        DeliveryFeeUpdateResponse response = new DeliveryFeeUpdateResponse("Express", BigDecimal.valueOf(5000), BigDecimal.valueOf(10000));

        when(deliveryFeeService.updateDeliveryFee(anyLong(), any(DeliveryFeeUpdateRequest.class))).thenReturn(ResponseEntity.ok(response));

        mockMvc.perform(put("/admin/delivery-fee/1")
                        .flashAttr("deliveryFeeUpdateRequest", request))
                .andExpect(status().isOk())
                .andExpect(view().name("order/admin/updateDeliveryFee"))
                .andExpect(model().attributeExists("success"));
    }

    @Test
    void testUpdateDeliveryFee_Failure() throws Exception {
        DeliveryFeeUpdateRequest request = new DeliveryFeeUpdateRequest("Express", BigDecimal.valueOf(5000), BigDecimal.valueOf(10000));

        when(deliveryFeeService.updateDeliveryFee(anyLong(), any(DeliveryFeeUpdateRequest.class)))
                .thenReturn(ResponseEntity.status(400).build());

        mockMvc.perform(put("/admin/delivery-fee/1")
                        .flashAttr("deliveryFeeUpdateRequest", request))
                .andExpect(status().isOk())
                .andExpect(view().name("order/admin/updateDeliveryFee"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("deliveryFeeUpdateRequest", request));
    }

    @Test
    void testDeleteDeliveryFee_Success() throws Exception {
        when(deliveryFeeService.deleteDeliveryFee(anyLong()))
                .thenReturn(ResponseEntity.ok(new DeliveryFeeDeleteResponse(1L, "Express", BigDecimal.valueOf(5000), BigDecimal.valueOf(10000))));

        mockMvc.perform(delete("/admin/delivery-fee/1")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "id,asc"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/delivery-fee?page=0&size=10&sort=id,asc"));
    }

    @Test
    void testDeleteDeliveryFee_Failure() throws Exception {
        when(deliveryFeeService.deleteDeliveryFee(anyLong()))
                .thenReturn(ResponseEntity.status(400).build());

        mockMvc.perform(delete("/admin/delivery-fee/1")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "id,asc"))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("error"));
    }
}
