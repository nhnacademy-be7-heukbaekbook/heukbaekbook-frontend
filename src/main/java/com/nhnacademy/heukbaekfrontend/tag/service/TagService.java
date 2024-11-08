package com.nhnacademy.heukbaekfrontend.tag.service;

import com.nhnacademy.heukbaekfrontend.tag.client.TagClient;
import com.nhnacademy.heukbaekfrontend.tag.dto.request.TagCreateRequest;
import com.nhnacademy.heukbaekfrontend.tag.dto.request.TagUpdateRequest;
import com.nhnacademy.heukbaekfrontend.tag.dto.response.TagCreateResponse;
import com.nhnacademy.heukbaekfrontend.tag.dto.response.TagDeleteResponse;
import com.nhnacademy.heukbaekfrontend.tag.dto.response.TagDetailResponse;
import com.nhnacademy.heukbaekfrontend.tag.dto.response.TagUpdateResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class TagService {

    private final TagClient tagClient;

    public TagService(TagClient tagClient) {
        this.tagClient = tagClient;
    }

    public ResponseEntity<TagCreateResponse> registerTag(TagCreateRequest request) {
        return tagClient.registerTag(request);
    }

    public ResponseEntity<TagUpdateResponse> updateTag(Long tagId, TagUpdateRequest request) {
        return tagClient.updateTag(tagId, request);
    }

    public ResponseEntity<TagDeleteResponse> deleteTag(Long tagId) {
        return tagClient.deleteTag(tagId);
    }

    public TagDetailResponse getTag(Long tagId) {
        return tagClient.getTag(tagId).getBody();
    }

    public Page<TagDetailResponse> getTags(Pageable pageable) {
        return tagClient.getTags(pageable).getBody();
    }
}
