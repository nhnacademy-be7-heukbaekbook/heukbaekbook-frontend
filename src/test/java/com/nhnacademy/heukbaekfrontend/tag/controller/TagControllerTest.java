package com.nhnacademy.heukbaekfrontend.tag.controller;

import com.nhnacademy.heukbaekfrontend.common.filter.JwtAuthenticationFilter;
import com.nhnacademy.heukbaekfrontend.common.util.CookieUtil;
import com.nhnacademy.heukbaekfrontend.tag.dto.request.TagCreateRequest;
import com.nhnacademy.heukbaekfrontend.tag.dto.request.TagUpdateRequest;
import com.nhnacademy.heukbaekfrontend.tag.dto.response.*;
import com.nhnacademy.heukbaekfrontend.tag.service.TagService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TagController.class)
class TagControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TagService tagService;

    @MockBean
    private CookieUtil cookieUtil;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(new TagController(tagService))
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    void testRegisterTagForm() throws Exception {
        mockMvc.perform(get("/admin/tags/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/registerTag"))
                .andExpect(model().attributeExists("tagCreateRequest"));
    }

    @Test
    void testRegisterTag_Success() throws Exception {
        when(tagService.registerTag(any(TagCreateRequest.class)))
                .thenReturn(ResponseEntity.ok(new TagCreateResponse("New Tag")));

        mockMvc.perform(post("/admin/tags")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "New Tag"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/registerTag"))
                .andExpect(model().attributeExists("success"));
    }

    @Test
    void testGetTags() throws Exception {
        Page<TagDetailResponse> tags = new PageImpl<>(List.of(
                new TagDetailResponse(1L, "Tag1")
        ));
        when(tagService.getTags(any(PageRequest.class))).thenReturn(tags);

        mockMvc.perform(get("/admin/tags")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/viewAllTags"))
                .andExpect(model().attributeExists("tags"));
    }

    @Test
    void testUpdateTagForm() throws Exception {
        TagDetailResponse tagDetail = new TagDetailResponse(1L, "Tag1");
        when(tagService.getTag(anyLong())).thenReturn(tagDetail);

        mockMvc.perform(get("/admin/tags/{tag-id}/update", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/updateTag"))
                .andExpect(model().attributeExists("tagUpdateRequest"))
                .andExpect(model().attributeExists("tagId"));
    }

    @Test
    void testUpdateTag_Success() throws Exception {
        when(tagService.updateTag(anyLong(), any(TagUpdateRequest.class)))
                .thenReturn(ResponseEntity.ok(new TagUpdateResponse("Updated Tag")));

        mockMvc.perform(put("/admin/tags/{tag-id}", 1L)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "Updated Tag"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/updateTag"))
                .andExpect(model().attributeExists("success"));
    }

    @Test
    void testDeleteTag_Success() throws Exception {
        when(tagService.deleteTag(anyLong()))
                .thenReturn(ResponseEntity.ok(new TagDeleteResponse(1L, "Tag1")));

        mockMvc.perform(delete("/admin/tags/{tag-id}", 1L))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/admin/tags**"));
    }
}
