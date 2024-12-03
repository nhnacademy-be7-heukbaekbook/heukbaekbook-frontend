package com.nhnacademy.heukbaekfrontend.tag.client;

import com.nhnacademy.heukbaekfrontend.tag.dto.request.TagCreateRequest;
import com.nhnacademy.heukbaekfrontend.tag.dto.request.TagUpdateRequest;
import com.nhnacademy.heukbaekfrontend.tag.dto.response.TagCreateResponse;
import com.nhnacademy.heukbaekfrontend.tag.dto.response.TagDeleteResponse;
import com.nhnacademy.heukbaekfrontend.tag.dto.response.TagDetailResponse;
import com.nhnacademy.heukbaekfrontend.tag.dto.response.TagUpdateResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "tag", url = "http://localhost:8082")
public interface TagClient {

    @PostMapping("/api/admin/tags")
    ResponseEntity<TagCreateResponse> registerTag(@RequestBody TagCreateRequest request);

    @PutMapping("/api/admin/tags/{tagId}")
    ResponseEntity<TagUpdateResponse> updateTag(@PathVariable Long tagId, @RequestBody TagUpdateRequest request);

    @DeleteMapping("/api/admin/tags/{tagId}")
    ResponseEntity<TagDeleteResponse> deleteTag(@PathVariable Long tagId);

    @GetMapping("/api/admin/tags/{tagId}")
    ResponseEntity<TagDetailResponse> getTag(@PathVariable Long tagId);

    @GetMapping("/api/admin/tags")
    ResponseEntity<Page<TagDetailResponse>> getTags(@RequestParam Pageable pageable);

    @GetMapping("/api/admin/tags/list")
    List<String> getTagList();
}
