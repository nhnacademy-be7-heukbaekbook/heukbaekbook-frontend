package com.nhnacademy.heukbaekfrontend.common.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.io.ObjectOutputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CookieUtilTest {

    @InjectMocks
    private CookieUtil cookieUtil;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Test
    void testGetCookie_ReturnsCookieValueValue() {
        Cookie cookie = new Cookie("testCookie", "testValue");
        when(request.getCookies()).thenReturn(new Cookie[]{cookie});

        String result = cookieUtil.getCookieValue(request, "testCookie");

        assertEquals("testValue", result);
    }

    @Test
    void testGetCookie_ReturnsNullWhenCookieValueNotFound() {
        when(request.getCookies()).thenReturn(new Cookie[]{new Cookie("otherCookie", "otherValue")});

        String result = cookieUtil.getCookieValue(request, "testCookie");

        assertNull(result);
    }

    @Test
    void testGetCookie_Value_ReturnsNullWhenNoCookiesPresent() {
        when(request.getCookies()).thenReturn(null);

        String result = cookieUtil.getCookieValue(request, "testCookie");

        assertNull(result);
    }

    @Test
    void testDeleteCookie_AddsExpiredCookieToResponse() {
        cookieUtil.deleteCookie(response, "testCookie");

        ArgumentCaptor<Cookie> cookieCaptor = ArgumentCaptor.forClass(Cookie.class);
        verify(response).addCookie(cookieCaptor.capture());

        Cookie deletedCookie = cookieCaptor.getValue();
        assertEquals("testCookie", deletedCookie.getName());
        assertEquals(0, deletedCookie.getMaxAge());
        assertEquals("/", deletedCookie.getPath());
        assertNull(deletedCookie.getValue());
    }

    @Test
    void testDeleteAllCookies_RemovesAllCookies() {
        // Arrange
        Cookie cookie1 = new Cookie("cookie1", "value1");
        Cookie cookie2 = new Cookie("cookie2", "value2");
        when(request.getCookies()).thenReturn(new Cookie[]{cookie1, cookie2});

        // Act
        cookieUtil.deleteAllCookies(request, response);

        // Assert
        ArgumentCaptor<Cookie> cookieCaptor = ArgumentCaptor.forClass(Cookie.class);
        verify(response, times(2)).addCookie(cookieCaptor.capture());
        for (Cookie deletedCookie : cookieCaptor.getAllValues()) {
            assertEquals(0, deletedCookie.getMaxAge());
            assertNull(deletedCookie.getValue());
            assertEquals("/", deletedCookie.getPath());
        }
    }

    @Test
    void testAddCookie_SetsCorrectProperties() {
        // Act
        cookieUtil.addCookie(response, "testCookie", "testValue", 3600);

        // Assert
        ArgumentCaptor<Cookie> cookieCaptor = ArgumentCaptor.forClass(Cookie.class);
        verify(response).addCookie(cookieCaptor.capture());

        Cookie addedCookie = cookieCaptor.getValue();
        assertEquals("testCookie", addedCookie.getName());
        assertEquals("testValue", addedCookie.getValue());
        assertEquals(3600, addedCookie.getMaxAge());
        assertEquals("/", addedCookie.getPath());
        assertEquals("Lax", addedCookie.getAttribute("SameSite"));
        assertTrue(addedCookie.isHttpOnly());
    }

    @Test
    void testSerialize_And_Deserialize_Object() {
        // Arrange
        String testObject = "This is a test";

        // Act
        String serialized = cookieUtil.serialize(testObject);
        String deserialized = cookieUtil.deserialize(serialized, String.class);

        // Assert
        assertEquals(testObject, deserialized);
    }

    @Test
    void testSerialize_ThrowsExceptionOnInvalidObject() {
        // Arrange
        Object invalidObject = new Object() {
            private void writeObject(ObjectOutputStream oos) throws IOException {
                throw new IOException("Serialization error");
            }
        };

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> cookieUtil.serialize(invalidObject));
        assertEquals("Failed to serialize object", exception.getMessage());
    }

    @Test
    void testDeserialize_ThrowsExceptionOnInvalidData() {
        // Arrange
        String invalidData = "InvalidSerializedData";

        // Act
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> cookieUtil.deserialize(invalidData, String.class));

        // Assert
        assertNotNull(exception, "Exception should not be null");
        assertEquals("Last unit does not have enough valid bits", exception.getMessage(),
                "Exception message should match expected value");
    }

}
