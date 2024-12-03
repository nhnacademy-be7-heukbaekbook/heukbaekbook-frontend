package com.nhnacademy.heukbaekfrontend.memberset.address.service.impl;

import com.nhnacademy.heukbaekfrontend.memberset.address.client.MemberAddressClient;
import com.nhnacademy.heukbaekfrontend.memberset.address.dto.MemberAddressRequest;
import com.nhnacademy.heukbaekfrontend.memberset.address.dto.MemberAddressResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class MemberAddressServiceImplTest {

    @InjectMocks
    private MemberAddressServiceImpl memberAddressService;

    @Mock
    private MemberAddressClient memberAddressClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetMemberAddressesList() {
        // Given
        List<MemberAddressResponse> mockResponse = List.of(
                new MemberAddressResponse(1L, 123L, "City", "State", "12345"),
                new MemberAddressResponse(2L, 456L, "City", "State", "67890")
        );

        when(memberAddressClient.getAllMemberAddresses())
                .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

        // When
        List<MemberAddressResponse> result = memberAddressService.getMemberAddressesList();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).postalCode()).isEqualTo(123L);
        verify(memberAddressClient, times(1)).getAllMemberAddresses();
    }

    @Test
    void testAddMemberAddress() {
        // Given
        MemberAddressRequest request = new MemberAddressRequest(789L, "City", "State", "54321");
        MemberAddressResponse mockResponse = new MemberAddressResponse(3L, 789L, "City", "State", "54321");

        when(memberAddressClient.createMemberAddress(any(MemberAddressRequest.class)))
                .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.CREATED));

        // When
        Optional<MemberAddressResponse> result = memberAddressService.addMemberAddress(request);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().postalCode()).isEqualTo(789L);
        verify(memberAddressClient, times(1)).createMemberAddress(any(MemberAddressRequest.class));
    }

    @Test
    void testUpdateMemberAddress() {
        // Given
        Long addressId = 1L;
        MemberAddressRequest request = new MemberAddressRequest(123L, "City", "State", "54321");
        MemberAddressResponse mockResponse = new MemberAddressResponse(1L, 2L, "City", "State", "54321");

        when(memberAddressClient.updateMemberAddress(anyLong(), any(MemberAddressRequest.class)))
                .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

        // When
        Optional<MemberAddressResponse> result = memberAddressService.updateMemberAddress(addressId, request);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().postalCode()).isEqualTo(2L);
        verify(memberAddressClient, times(1)).updateMemberAddress(anyLong(), any(MemberAddressRequest.class));
    }

    @Test
    void testDeleteMemberAddress() {
        // Arrange
        Long addressId = 123L;

        // Act
        memberAddressService.deleteMemberAddress(addressId);

        // Assert
        verify(memberAddressClient, times(1)).deleteMemberAddress(addressId);
    }

    @Test
    void testCountMemberAddresses() {
        // Given
        Long mockCount = 5L;

        when(memberAddressClient.countMemberAddresses())
                .thenReturn(new ResponseEntity<>(mockCount, HttpStatus.OK));

        // When
        ResponseEntity<Long> result = memberAddressService.countMemberAddresses();

        // Then
        assertThat(result.getBody()).isEqualTo(mockCount);
        verify(memberAddressClient, times(1)).countMemberAddresses();
    }
}
