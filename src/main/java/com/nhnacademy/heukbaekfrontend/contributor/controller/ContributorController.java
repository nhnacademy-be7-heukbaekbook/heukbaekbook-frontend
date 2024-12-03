package com.nhnacademy.heukbaekfrontend.contributor.controller;

import com.nhnacademy.heukbaekfrontend.contributor.dto.request.ContributorCreateRequest;
import com.nhnacademy.heukbaekfrontend.contributor.dto.request.ContributorUpdateRequest;
import com.nhnacademy.heukbaekfrontend.contributor.dto.response.ContributorCreateResponse;
import com.nhnacademy.heukbaekfrontend.contributor.dto.response.ContributorDeleteResponse;
import com.nhnacademy.heukbaekfrontend.contributor.dto.response.ContributorDetailResponse;
import com.nhnacademy.heukbaekfrontend.contributor.dto.response.ContributorUpdateResponse;
import com.nhnacademy.heukbaekfrontend.contributor.service.ContributorService;
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
@RequestMapping("/admin/contributors")
public class ContributorController {

    private final ContributorService contributorService;

    public ContributorController(ContributorService contributorService) {
        this.contributorService = contributorService;
    }

    @GetMapping("/register")
    public String registerContributor(Model model) {
        model.addAttribute("contributorCreateRequest", new ContributorCreateRequest("", ""));
        return "contributor/admin/registerContributor";
    }

    @PostMapping
    public String registerContributor(@ModelAttribute ContributorCreateRequest request, Model model) {
        ResponseEntity<ContributorCreateResponse> response = contributorService.registerContributor(request);

        if (response.getStatusCode().is2xxSuccessful()) {
            model.addAttribute("success", true);
        } else {
            model.addAttribute("error", "기여자 등록에 실패했습니다.");
            model.addAttribute("contributorCreateRequest", request);
        }
        return "contributor/admin/registerContributor";
    }

    @GetMapping
    public String getContributors(
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
            Model model
    ) {
        Page<ContributorDetailResponse> response = contributorService.getContributors(pageable);
        model.addAttribute("contributors", response);
        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("size", pageable.getPageSize());
        model.addAttribute("sort", pageable.getSort().toString());
        return "contributor/admin/viewAllContributor";
    }

    @GetMapping("/{contributor-id}/update")
    public String updateContributorForm(@PathVariable(name = "contributor-id") Long contributorId,
                                     Model model
    ){
        ContributorDetailResponse contributorDetail = contributorService.getContributor(contributorId);
        ContributorUpdateRequest request = new ContributorUpdateRequest(
                contributorDetail.name(),
                contributorDetail.description()
        );

        model.addAttribute("contributorUpdateRequest", request);
        model.addAttribute("contributorId", contributorId);
        return "contributor/admin/updateContributor";
    }

    @PutMapping("/{contributor-id}")
    public String updateContributor(@PathVariable(name = "contributor-id") Long contributorId,
                                 @ModelAttribute ContributorUpdateRequest request,
                                 Model model) {
        ResponseEntity<ContributorUpdateResponse> response = contributorService.updateContributor(contributorId, request);
        if (response.getStatusCode().is2xxSuccessful()) {
            model.addAttribute("success", true);
        } else {
            model.addAttribute("error", "기여자 수정에 실패했습니다.");
            model.addAttribute("contributorUpdateRequest", request);
        }
        return "contributor/admin/updateContributor";
    }

    @DeleteMapping("/{contributor-id}")
    public String deleteContributor(
            @PathVariable(name = "contributor-id") Long contributorId,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size,
            @RequestParam(value = "sort", required = false) String sort,
            RedirectAttributes redirectAttributes) {

        ResponseEntity<ContributorDeleteResponse> response = contributorService.deleteContributor(contributorId);

        if (response.getStatusCode().is2xxSuccessful()) {
            redirectAttributes.addFlashAttribute("success", true);
        } else {
            redirectAttributes.addFlashAttribute("error", "기여자 삭제에 실패했습니다.");
        }

        String redirectUrl = getRedirectUrl(page, size, sort, "/admin/contributors");

        return "redirect:" + redirectUrl;
    }

}
