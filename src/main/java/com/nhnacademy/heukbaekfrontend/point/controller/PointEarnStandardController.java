package com.nhnacademy.heukbaekfrontend.point.controller;

import com.nhnacademy.heukbaekfrontend.common.annotation.Admin;
import com.nhnacademy.heukbaekfrontend.point.dto.PointEarnStandardRequest;
import com.nhnacademy.heukbaekfrontend.point.dto.PointEarnStandardResponse;
import com.nhnacademy.heukbaekfrontend.point.dto.PointEarnTriggerEvent;
import com.nhnacademy.heukbaekfrontend.point.service.PointEarnStandardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/point-earn-standard")
public class PointEarnStandardController {
    private static final String REDIRECT_PATH = "redirect:/admin/point-earn-standard";

    private final PointEarnStandardService pointEarnStandardService;

    @Admin
    @GetMapping
    public String getPointEarnStandardList(Model model) {
        model.addAttribute("triggerEvents", PointEarnTriggerEvent.values());
        model.addAttribute("pointEarnStandards", pointEarnStandardService.getPointEarnStandards());
        return "point/admin/point-earn-standard";
    }

    @Admin
    @PostMapping
    public String savePointEarnStandard(@ModelAttribute PointEarnStandardRequest request, RedirectAttributes redirectAttributes) {
        PointEarnStandardResponse earnStandard = pointEarnStandardService.createPointEarnStandard(request);
        redirectAttributes.addFlashAttribute("newEarnStandard", earnStandard);
        return REDIRECT_PATH;
    }

    @Admin
    @DeleteMapping("/{id}")
    public String deletePointEarnStandard(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        pointEarnStandardService.deletePointEarnStandard(id);
        redirectAttributes.addFlashAttribute("deleteSuccess", id);
        return REDIRECT_PATH;
    }

    @Admin
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
