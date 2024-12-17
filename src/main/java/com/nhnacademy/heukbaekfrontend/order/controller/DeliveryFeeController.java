package com.nhnacademy.heukbaekfrontend.order.controller;

import com.nhnacademy.heukbaekfrontend.order.dto.request.DeliveryFeeCreateRequest;
import com.nhnacademy.heukbaekfrontend.order.dto.request.DeliveryFeeUpdateRequest;
import com.nhnacademy.heukbaekfrontend.order.dto.response.DeliveryFeeCreateResponse;
import com.nhnacademy.heukbaekfrontend.order.dto.response.DeliveryFeeDeleteResponse;
import com.nhnacademy.heukbaekfrontend.order.dto.response.DeliveryFeeDetailResponse;
import com.nhnacademy.heukbaekfrontend.order.dto.response.DeliveryFeeUpdateResponse;
import com.nhnacademy.heukbaekfrontend.order.service.DeliveryFeeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;

import static com.nhnacademy.heukbaekfrontend.util.Utils.getRedirectUrl;

@Controller
@RequestMapping("/admin/delivery-fee")
public class DeliveryFeeController {

    private final DeliveryFeeService deliveryFeeService;

    public DeliveryFeeController(DeliveryFeeService deliveryFeeService) {
        this.deliveryFeeService = deliveryFeeService;
    }

    @GetMapping("/register")
    public String viewDeliveryFee(Model model) {
        model.addAttribute("deliveryFeeCreateRequest", new DeliveryFeeCreateRequest("", BigDecimal.ZERO, BigDecimal.ZERO));
        return "order/admin/registerDeliveryFee";
    }

    @PostMapping("/register")
    public String registerDeliveryFee(@ModelAttribute DeliveryFeeCreateRequest request, Model model) {
        ResponseEntity<DeliveryFeeCreateResponse> response = deliveryFeeService.registerDeliveryFee(request);

        if (response.getStatusCode().is2xxSuccessful()) {
            model.addAttribute("success", true);
        } else {
            model.addAttribute("error", "배송비 등록에 실패했습니다.");
            model.addAttribute("deliveryCreateRequest", request);
        }
        return "order/admin/registerDeliveryFee";
    }

    @GetMapping
    public String getDeliveryFees(
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
            Model model
    ) {
        Page<DeliveryFeeDetailResponse> response = deliveryFeeService.getDeliveryFees(pageable);
        model.addAttribute("deliveryFees", response);
        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("size", pageable.getPageSize());
        model.addAttribute("sort", pageable.getSort().toString());
        return "order/admin/viewAllDeliveryFees";
    }

    @GetMapping("/{delivery-fee-id}/update")
    public String updateDeliveryFeeForm(@PathVariable(name = "delivery-fee-id") Long deliveryFeeId,
                                Model model
    ){
        DeliveryFeeDetailResponse deliveryFeeDetail = deliveryFeeService.getDeliveryFee(deliveryFeeId);
        DeliveryFeeUpdateRequest request = new DeliveryFeeUpdateRequest(
                deliveryFeeDetail.name(),
                deliveryFeeDetail.fee(),
                deliveryFeeDetail.minimumOrderAmount()
        );

        model.addAttribute("deliveryFeeUpdateRequest", request);
        model.addAttribute("deliveryFeeId", deliveryFeeId);
        return "order/admin/updateDeliveryFee";
    }

    @PutMapping("/{delivery-fee-id}")
    public String updateDeliveryFee(@PathVariable(name = "delivery-fee-id") Long deliveryFeeId,
                            @ModelAttribute DeliveryFeeUpdateRequest request,
                            Model model) {
        ResponseEntity<DeliveryFeeUpdateResponse> response = deliveryFeeService.updateDeliveryFee(deliveryFeeId, request);
        if (response.getStatusCode().is2xxSuccessful()) {
            model.addAttribute("success", true);
        } else {
            model.addAttribute("error", "배송비 수정에 실패했습니다.");
            model.addAttribute("deliveryFeeUpdateRequest", request);
        }
        return "order/admin/updateDeliveryFee";
    }

    @DeleteMapping("/{delivery-fee-id}")
    public String deleteDeliveryFee(
            @PathVariable(name = "delivery-fee-id") Long deliveryFeeId,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size,
            @RequestParam(value = "sort", required = false) String sort,
            RedirectAttributes redirectAttributes) {

        ResponseEntity<DeliveryFeeDeleteResponse> response = deliveryFeeService.deleteDeliveryFee(deliveryFeeId);

        if (response.getStatusCode().is2xxSuccessful()) {
            redirectAttributes.addFlashAttribute("success", true);
        } else {
            redirectAttributes.addFlashAttribute("error", "배송비 삭제에 실패했습니다.");
        }

        String redirectUrl = getRedirectUrl(page, size, sort, "/admin/delivery-fee");

        return "redirect:" + redirectUrl;
    }
}
