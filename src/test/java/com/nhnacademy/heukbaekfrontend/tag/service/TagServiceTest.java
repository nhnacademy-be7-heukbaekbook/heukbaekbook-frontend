package com.nhnacademy.heukbaekfrontend.tag.service;

import com.nhnacademy.heukbaekfrontend.tag.client.TagClient;
import com.nhnacademy.heukbaekfrontend.tag.dto.request.TagCreateRequest;
import com.nhnacademy.heukbaekfrontend.tag.dto.request.TagUpdateRequest;
import com.nhnacademy.heukbaekfrontend.tag.dto.response.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class TagServiceTest {

    @InjectMocks
    private TagService tagService;

    @Mock
    private TagClient tagClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterTag() {
        TagCreateRequest request = new TagCreateRequest("New Tag");
        TagCreateResponse response = new TagCreateResponse("New Tag");

        when(tagClient.registerTag(any(TagCreateRequest.class)))
                .thenReturn(ResponseEntity.ok(response));

        ResponseEntity<TagCreateResponse> result = tagService.registerTag(request);

        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().name()).isEqualTo("New Tag");
        verify(tagClient, times(1)).registerTag(request);
    }

    @Test
    void testGetTags() {
        Page<TagDetailResponse> tags = new PageImpl<>(List.of(
                new TagDetailResponse(1L, "Tag1")
        ));

        when(tagClient.getTags(any(Pageable.class)))
                .thenReturn(ResponseEntity.ok(tags));

        Page<TagDetailResponse> result = tagService.getTags(Pageable.unpaged());

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).name()).isEqualTo("Tag1");
        verify(tagClient, times(1)).getTags(any(Pageable.class));
    }

    @Test
    void testUpdateTag() {
        TagUpdateRequest request = new TagUpdateRequest("Updated Tag");
        TagUpdateResponse response = new TagUpdateResponse("Updated Tag");

        when(tagClient.updateTag(anyLong(), any(TagUpdateRequest.class)))
                .thenReturn(ResponseEntity.ok(response));

        ResponseEntity<TagUpdateResponse> result = tagService.updateTag(1L, request);

        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().name()).isEqualTo("Updated Tag");
        verify(tagClient, times(1)).updateTag(1L, request);
    }

    @Test
    void testDeleteTag() {
        TagDeleteResponse response = new TagDeleteResponse(1L, "Tag1");

        when(tagClient.deleteTag(anyLong()))
                .thenReturn(ResponseEntity.ok(response));

        ResponseEntity<TagDeleteResponse> result = tagService.deleteTag(1L);

        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().name()).isEqualTo("Tag1");
        verify(tagClient, times(1)).deleteTag(1L);
    }

    @Test
    void testGetTag() {
        TagDetailResponse response = new TagDetailResponse(1L, "Tag1");

        when(tagClient.getTag(anyLong()))
                .thenReturn(ResponseEntity.ok(response));

        TagDetailResponse result = tagService.getTag(1L);

        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo("Tag1");
        verify(tagClient, times(1)).getTag(1L);
    }
}
