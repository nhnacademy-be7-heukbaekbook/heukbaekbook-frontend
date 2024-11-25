package com.nhnacademy.heukbaekfrontend.tag.controller;

import com.nhnacademy.heukbaekfrontend.tag.dto.request.TagCreateRequest;
import com.nhnacademy.heukbaekfrontend.tag.dto.request.TagUpdateRequest;
import com.nhnacademy.heukbaekfrontend.tag.dto.response.TagCreateResponse;
import com.nhnacademy.heukbaekfrontend.tag.dto.response.TagDeleteResponse;
import com.nhnacademy.heukbaekfrontend.tag.dto.response.TagDetailResponse;
import com.nhnacademy.heukbaekfrontend.tag.dto.response.TagUpdateResponse;
import com.nhnacademy.heukbaekfrontend.tag.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/admin/tags")
public class TagController {

    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping("/register")
    public String registerTag(Model model) {
        model.addAttribute("tagCreateRequest", new TagCreateRequest(""));
        return "tag/admin/registerTag";
    }

    @PostMapping
    public String registerTag(@ModelAttribute TagCreateRequest request, Model model) {
        ResponseEntity<TagCreateResponse> response = tagService.registerTag(request);

        if (response.getStatusCode().is2xxSuccessful()) {
            model.addAttribute("success", true);
        } else {
            model.addAttribute("error", "태그 등록에 실패했습니다.");
            model.addAttribute("tagCreateRequest", request);
        }
        return "tag/admin/registerTag";
    }

    @GetMapping
    public String getTags(
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
            Model model
    ) {
        Page<TagDetailResponse> response = tagService.getTags(pageable);
        model.addAttribute("tags", response);
        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("size", pageable.getPageSize());
        model.addAttribute("sort", pageable.getSort().toString());
        return "tag/admin/viewAllTags";
    }

    @GetMapping("/{tag-id}/update")
    public String updateTagForm(@PathVariable(name = "tag-id") Long tagId,
                                      Model model
    ){
        TagDetailResponse tagDetail = tagService.getTag(tagId);
        TagUpdateRequest request = new TagUpdateRequest(
                tagDetail.name()
        );

        model.addAttribute("tagUpdateRequest", request);
        model.addAttribute("tagId", tagId);
        return "tag/admin/updateTag";
    }

    @PutMapping("/{tag-id}")
    public String updateTag(@PathVariable(name = "tag-id") Long tagId,
                                 @ModelAttribute TagUpdateRequest request,
                                 Model model) {
        ResponseEntity<TagUpdateResponse> response = tagService.updateTag(tagId, request);
        if (response.getStatusCode().is2xxSuccessful()) {
            model.addAttribute("success", true);
        } else {
            model.addAttribute("error", "태그 수정에 실패했습니다.");
            model.addAttribute("tagUpdateRequest", request);
        }
        return "tag/admin/updateTag";
    }

    @DeleteMapping("/{tag-id}")
    public String deleteTag(
            @PathVariable(name = "tag-id") Long tagId,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size,
            @RequestParam(value = "sort", required = false) String sort,
            RedirectAttributes redirectAttributes) {

        ResponseEntity<TagDeleteResponse> response = tagService.deleteTag(tagId);

        if (response.getStatusCode().is2xxSuccessful()) {
            redirectAttributes.addFlashAttribute("success", true);
        } else {
            redirectAttributes.addFlashAttribute("error", "태그 삭제에 실패했습니다.");
        }

        String redirectUrl = getRedirectUrl(page, size, sort, "/admin/tags");

        return "redirect:" + redirectUrl;
    }

}
