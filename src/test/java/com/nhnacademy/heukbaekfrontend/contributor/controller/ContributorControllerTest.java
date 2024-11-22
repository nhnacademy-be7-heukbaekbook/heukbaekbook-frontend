package com.nhnacademy.heukbaekfrontend.contributor.controller;

import com.nhnacademy.heukbaekfrontend.contributor.dto.request.ContributorCreateRequest;
import com.nhnacademy.heukbaekfrontend.contributor.dto.request.ContributorUpdateRequest;
import com.nhnacademy.heukbaekfrontend.contributor.dto.response.*;
import com.nhnacademy.heukbaekfrontend.contributor.service.ContributorService;
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

@WebMvcTest(ContributorController.class)
class ContributorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContributorService contributorService;

    @MockBean
    private CookieUtil cookieUtil;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(new ContributorController(contributorService))
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    void testRegisterContributorForm() throws Exception {
        mockMvc.perform(get("/admin/contributors/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("contributor/admin/registerContributor"))
                .andExpect(model().attributeExists("contributorCreateRequest"));
    }

    @Test
    void testRegisterContributor_Success() throws Exception {
        when(contributorService.registerContributor(any(ContributorCreateRequest.class)))
                .thenReturn(ResponseEntity.ok(new ContributorCreateResponse("1", "New Contributor")));

        mockMvc.perform(post("/admin/contributors")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "New Contributor")
                        .param("description", "Description"))
                .andExpect(status().isOk())
                .andExpect(view().name("contributor/admin/registerContributor"))
                .andExpect(model().attributeExists("success"));
    }

    @Test
    void testGetContributors() throws Exception {
        Page<ContributorDetailResponse> contributors = new PageImpl<>(List.of(
                new ContributorDetailResponse(1L, "John Doe", "Description")
        ));
        when(contributorService.getContributors(any(PageRequest.class))).thenReturn(contributors);

        mockMvc.perform(get("/admin/contributors")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(view().name("contributor/admin/viewAllContributor"))
                .andExpect(model().attributeExists("contributors"));
    }

    @Test
    void testUpdateContributorForm() throws Exception {
        ContributorDetailResponse contributorDetail = new ContributorDetailResponse(1L, "John Doe", "Description");
        when(contributorService.getContributor(1L)).thenReturn(contributorDetail);

        mockMvc.perform(get("/admin/contributors/{contributor-id}/update", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("contributor/admin/updateContributor"))
                .andExpect(model().attributeExists("contributorUpdateRequest"))
                .andExpect(model().attributeExists("contributorId"));
    }

    @Test
    void testUpdateContributor_Success() throws Exception {
        when(contributorService.updateContributor(anyLong(), any(ContributorUpdateRequest.class)))
                .thenReturn(ResponseEntity.ok(new ContributorUpdateResponse("1", "Updated Contributor")));

        mockMvc.perform(put("/admin/contributors/{contributor-id}", 1L)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "Updated Contributor")
                        .param("description", "Updated Description"))
                .andExpect(status().isOk())
                .andExpect(view().name("contributor/admin/updateContributor"))
                .andExpect(model().attributeExists("success"));
    }

    @Test
    void testDeleteContributor_Success() throws Exception {
        when(contributorService.deleteContributor(1L))
                .thenReturn(ResponseEntity.ok(new ContributorDeleteResponse("Contributor deleted successfully")));

        mockMvc.perform(delete("/admin/contributors/{contributor-id}", 1L))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/admin/contributors"));
    }
}
