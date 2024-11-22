package com.nhnacademy.heukbaekfrontend.contributor.controller;

import com.nhnacademy.heukbaekfrontend.contributor.dto.request.PublisherCreateRequest;
import com.nhnacademy.heukbaekfrontend.contributor.dto.request.PublisherUpdateRequest;
import com.nhnacademy.heukbaekfrontend.contributor.dto.response.*;
import com.nhnacademy.heukbaekfrontend.contributor.service.PublisherService;
import com.nhnacademy.heukbaekfrontend.common.filter.JwtAuthenticationFilter;
import com.nhnacademy.heukbaekfrontend.common.util.CookieUtil;
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

@WebMvcTest(PublisherController.class)
class PublisherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PublisherService publisherService;

    @MockBean
    private CookieUtil cookieUtil;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(new PublisherController(publisherService))
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    void testRegisterPublisherForm() throws Exception {
        mockMvc.perform(get("/admin/publishers/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("contributor/admin/registerPublisher"))
                .andExpect(model().attributeExists("publisherCreateRequest"));
    }

    @Test
    void testRegisterPublisher_Success() throws Exception {
        when(publisherService.registerPublisher(any(PublisherCreateRequest.class)))
                .thenReturn(ResponseEntity.ok(new PublisherCreateResponse("New Publisher")));

        mockMvc.perform(post("/admin/publishers")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "New Publisher"))
                .andExpect(status().isOk())
                .andExpect(view().name("contributor/admin/registerPublisher"))
                .andExpect(model().attributeExists("success"));
    }

    @Test
    void testGetPublishers() throws Exception {
        Page<PublisherDetailResponse> publishers = new PageImpl<>(List.of(
                new PublisherDetailResponse(1L, "Publisher A")
        ));
        when(publisherService.getPublishers(any(PageRequest.class))).thenReturn(publishers);

        mockMvc.perform(get("/admin/publishers")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(view().name("contributor/admin/viewAllPublisher"))
                .andExpect(model().attributeExists("publishers"));
    }

    @Test
    void testUpdatePublisherForm() throws Exception {
        PublisherDetailResponse publisherDetail = new PublisherDetailResponse(1L, "Publisher A");
        when(publisherService.getPublisherById(1L)).thenReturn(publisherDetail);

        mockMvc.perform(get("/admin/publishers/{publisher-id}/update", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("contributor/admin/updatePublisher"))
                .andExpect(model().attributeExists("publisherUpdateRequest"))
                .andExpect(model().attributeExists("publisherId"));
    }

    @Test
    void testUpdatePublisher_Success() throws Exception {
        when(publisherService.updatePublisher(anyLong(), any(PublisherUpdateRequest.class)))
                .thenReturn(ResponseEntity.ok(new PublisherUpdateResponse("Updated Publisher")));

        mockMvc.perform(put("/admin/publishers/{publisher-id}", 1L)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "Updated Publisher"))
                .andExpect(status().isOk())
                .andExpect(view().name("contributor/admin/updatePublisher"))
                .andExpect(model().attributeExists("success"));
    }

    @Test
    void testDeletePublisher_Success() throws Exception {
        when(publisherService.deletePublisher(1L))
                .thenReturn(ResponseEntity.ok(new PublisherDeleteResponse("Publisher deleted successfully")));

        mockMvc.perform(delete("/admin/publishers/{publisher-id}", 1L))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/admin/publishers**"));
    }
}
