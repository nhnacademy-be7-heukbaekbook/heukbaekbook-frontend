package com.nhnacademy.heukbaekfrontend.contributor.controller;

import com.nhnacademy.heukbaekfrontend.common.annotation.Admin;
import com.nhnacademy.heukbaekfrontend.contributor.dto.request.PublisherCreateRequest;
import com.nhnacademy.heukbaekfrontend.contributor.dto.request.PublisherUpdateRequest;
import com.nhnacademy.heukbaekfrontend.contributor.dto.response.PublisherCreateResponse;
import com.nhnacademy.heukbaekfrontend.contributor.dto.response.PublisherDeleteResponse;
import com.nhnacademy.heukbaekfrontend.contributor.dto.response.PublisherDetailResponse;
import com.nhnacademy.heukbaekfrontend.contributor.dto.response.PublisherUpdateResponse;
import com.nhnacademy.heukbaekfrontend.contributor.service.PublisherService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.nhnacademy.heukbaekfrontend.util.Utils.getRedirectUrl;

@Controller
@RequestMapping("/admin/publishers")
public class PublisherController {

    private final PublisherService publisherService;

    public PublisherController(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @Admin
    @GetMapping("/register")
    public String registerPublisher(Model model) {
        model.addAttribute("publisherCreateRequest", new PublisherCreateRequest(""));
        return "contributor/admin/registerPublisher";
    }

    @Admin
    @PostMapping
    public String registerPublisher(@ModelAttribute PublisherCreateRequest request, Model model) {
        ResponseEntity<PublisherCreateResponse> response = publisherService.registerPublisher(request);

        if (response.getStatusCode().is2xxSuccessful()) {
            model.addAttribute("success", true);
        } else {
            model.addAttribute("error", "출판사 등록에 실패했습니다.");
            model.addAttribute("publisherCreateRequest", request);
        }
        return "contributor/admin/registerPublisher";
    }

    @Admin
    @GetMapping
    public String getPublishers(
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
            Model model
    ) {
        Page<PublisherDetailResponse> response = publisherService.getPublishers(pageable);
        model.addAttribute("publishers", response);
        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("size", pageable.getPageSize());
        model.addAttribute("sort", pageable.getSort().toString());
        return "contributor/admin/viewAllPublisher";
    }

    @Admin
    @GetMapping("/{publisher-id}/update")
    public String updatePublisherForm(@PathVariable(name = "publisher-id") Long publisherId,
                                     Model model
    ){
        PublisherDetailResponse publisherDetail = publisherService.getPublisherById(publisherId);
        PublisherUpdateRequest request = new PublisherUpdateRequest(
                publisherDetail.name()
        );

        model.addAttribute("publisherUpdateRequest", request);
        model.addAttribute("publisherId", publisherId);
        return "contributor/admin/updatePublisher";
    }

    @Admin
    @PutMapping("/{publisher-id}")
    public String updatePublisher(@PathVariable(name = "publisher-id") Long publisherId,
                                 @ModelAttribute PublisherUpdateRequest request,
                                 Model model) {
        ResponseEntity<PublisherUpdateResponse> response = publisherService.updatePublisher(publisherId, request);
        if (response.getStatusCode().is2xxSuccessful()) {
            model.addAttribute("success", true);
        } else {
            model.addAttribute("error", "출판사 수정에 실패했습니다.");
            model.addAttribute("publisherUpdateRequest", request);
        }
        return "contributor/admin/updatePublisher";
    }

    @Admin
    @DeleteMapping("/{publisher-id}")
    public String deletePublisher(
            @PathVariable(name = "publisher-id") Long publisherId,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size,
            @RequestParam(value = "sort", required = false) String sort,
            RedirectAttributes redirectAttributes) {

        ResponseEntity<PublisherDeleteResponse> response = publisherService.deletePublisher(publisherId);

        if (response.getStatusCode().is2xxSuccessful()) {
            redirectAttributes.addFlashAttribute("success", true);
        } else {
            redirectAttributes.addFlashAttribute("error", "출판사 삭제에 실패했습니다.");
        }

        String redirectUrl = getRedirectUrl(page, size, sort, "/admin/publishers");

        return "redirect:" + redirectUrl;
    }

}
