package com.nhnacademy.heukbaekfrontend.memberset.address.controller;

import com.nhnacademy.heukbaekfrontend.memberset.address.dto.MemberAddressRequest;
import com.nhnacademy.heukbaekfrontend.memberset.address.dto.MemberAddressResponse;
import com.nhnacademy.heukbaekfrontend.memberset.address.service.MemberAddressService;
import com.nhnacademy.heukbaekfrontend.memberset.grade.dto.GradeDto;
import com.nhnacademy.heukbaekfrontend.memberset.member.service.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MemberAddressController.class)
@AutoConfigureMockMvc(addFilters = false) // Spring Security 필터 비활성화
class MemberAddressControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberAddressService memberAddressService;

    @MockBean
    private MemberService memberService;

    @Test
    void testGetMyPageAddresses() throws Exception {
        // Given
        List<MemberAddressResponse> mockAddresses = List.of(
                new MemberAddressResponse(1L, 12345L, "서울특별시 강남구", "101호", "집"),
                new MemberAddressResponse(2L, 67890L, "서울특별시 서초구", "102호", "회사")
        );
        GradeDto mockGrade = new GradeDto("VIP", BigDecimal.valueOf(0.15), BigDecimal.valueOf(2000));

        when(memberAddressService.getMemberAddressesList()).thenReturn(mockAddresses);
        when(memberService.getMembersGrade()).thenReturn(Optional.of(mockGrade));

        // When & Then
        mockMvc.perform(get("/members/mypage/addresses"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("addressList"))
                .andExpect(model().attributeExists("gradeDto"))
                .andExpect(view().name("mypage/mypage-address"));

        verify(memberAddressService, times(1)).getMemberAddressesList();
        verify(memberService, times(1)).getMembersGrade();
    }

    @Test
    void testCountMemberAddresses() throws Exception {
        // Given
        when(memberAddressService.countMemberAddresses()).thenReturn(ResponseEntity.ok(2L));

        // When & Then
        mockMvc.perform(get("/members/mypage/addresses/count"))
                .andExpect(status().isOk())
                .andExpect(content().string("2"));

        verify(memberAddressService, times(1)).countMemberAddresses();
    }

    @Test
    void testAddMemberAddress_Success() throws Exception {
        // Given
        MemberAddressRequest request = new MemberAddressRequest(
                12345L,
                "서울특별시 강남구",
                "101호",
                "집"
        );

        // When & Then
        mockMvc.perform(post("/members/mypage/addresses")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("postalCode", String.valueOf(request.postalCode()))
                        .param("roadNameAddress", request.roadNameAddress())
                        .param("detailAddress", request.detailAddress())
                        .param("alias", request.alias()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/members/mypage/addresses"));

        verify(memberAddressService, times(1)).addMemberAddress(any(MemberAddressRequest.class));
    }

    @Test
    void testUpdateMemberAddress() throws Exception {
        // Given
        Long addressId = 1L;
        MemberAddressRequest request = new MemberAddressRequest(
                12345L,
                "서울특별시 강남구",
                "101호",
                "집"
        );

        // When & Then
        mockMvc.perform(put("/members/mypage/addresses/{addressId}", addressId)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("postalCode", String.valueOf(request.postalCode()))
                        .param("roadNameAddress", request.roadNameAddress())
                        .param("detailAddress", request.detailAddress())
                        .param("alias", request.alias()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/members/mypage/addresses"));

        verify(memberAddressService, times(1)).updateMemberAddress(eq(addressId), any(MemberAddressRequest.class));
    }

    @Test
    void testDeleteMemberAddress() throws Exception {
        // Given
        Long addressId = 1L;

        // When & Then
        mockMvc.perform(delete("/members/mypage/addresses/{addressId}", addressId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/members/mypage/addresses"));

        verify(memberAddressService, times(1)).deleteMemberAddress(addressId);
    }
}
