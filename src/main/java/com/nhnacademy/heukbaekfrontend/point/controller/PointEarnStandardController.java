package com.nhnacademy.heukbaekfrontend.point.controller;

import com.nhnacademy.heukbaekfrontend.point.dto.EventCode;
import com.nhnacademy.heukbaekfrontend.point.dto.PointEarnStandardRequest;
import com.nhnacademy.heukbaekfrontend.point.dto.PointEarnStandardResponse;
import com.nhnacademy.heukbaekfrontend.point.dto.PointEarnType;
import com.nhnacademy.heukbaekfrontend.point.service.PointEarnStandardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/point-earn-standard")
public class PointEarnStandardController {
    private static final String REDIRECT_PATH = "redirect:/admin/point-earn-standard";

    private final PointEarnStandardService pointEarnStandardService;

    @GetMapping
    public String getPointEarnStandard(Model model) {
        model.addAttribute("triggerEvents", EventCode.values());
        model.addAttribute("pointEarnTypes", PointEarnType.values());

        Map<EventCode, List<PointEarnStandardResponse>> standardsByEvent = new EnumMap<>(EventCode.class);
        for (EventCode event : EventCode.values()) {
            standardsByEvent.put(event, pointEarnStandardService.getValidStandardsByEvent(String.valueOf(event)));
        }
        model.addAttribute("standardsByEvent", standardsByEvent);

        return "point/admin/point-earn-standard";
    }

    @PostMapping
    public String savePointEarnStandard(@ModelAttribute PointEarnStandardRequest request, RedirectAttributes redirectAttributes) {
        PointEarnStandardResponse earnStandard = pointEarnStandardService.createPointEarnStandard(request);
        redirectAttributes.addFlashAttribute("newEarnStandard", earnStandard);
        return REDIRECT_PATH;
    }

    @DeleteMapping("/{id}")
    public String deletePointEarnStandard(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        pointEarnStandardService.deletePointEarnStandard(id);
        redirectAttributes.addFlashAttribute("deleteSuccess", id);
        return REDIRECT_PATH;
    }

    @PutMapping("/{id}")
    public String updatePointEarnStandard(
            @PathVariable Long id,
            @ModelAttribute PointEarnStandardRequest pointEarnStandardRequest,
            RedirectAttributes redirectAttributes) {
        PointEarnStandardResponse earnStandard = pointEarnStandardService.updatePointEarnStandard(id, pointEarnStandardRequest);
        redirectAttributes.addFlashAttribute("updateEarnStandard", earnStandard);

        return REDIRECT_PATH;
    }
}
