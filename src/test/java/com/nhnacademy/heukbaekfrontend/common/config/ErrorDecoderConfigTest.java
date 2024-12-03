package com.nhnacademy.heukbaekfrontend.common.config;

import com.nhnacademy.heukbaekfrontend.common.exception.*;
import feign.Response;
import feign.Request;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ErrorDecoderConfigTest {

    private ErrorDecoderConfig errorDecoderConfig;

    @BeforeEach
    void setUp() {
        errorDecoderConfig = new ErrorDecoderConfig();
    }

    @Test
    void decode_ShouldReturnBadRequestException_WhenStatusIs400() {
        // Arrange
        Response response = createFeignResponse(400, "Bad Request Error");

        // Act
        Exception exception = errorDecoderConfig.decode("TestService", response);

        // Assert
        assertThat(exception).isInstanceOf(BadRequestException.class);
        assertThat(exception.getMessage()).isEqualTo("Bad Request Error");
    }

    @Test
    void decode_ShouldReturnUnauthorizedException_WhenStatusIs401() {
        // Arrange
        Response response = createFeignResponse(401, "Unauthorized Error");

        // Act
        Exception exception = errorDecoderConfig.decode("TestService", response);

        // Assert
        assertThat(exception).isInstanceOf(UnauthorizedException.class);
        assertThat(exception.getMessage()).isEqualTo("Unauthorized Error");
    }

    @Test
    void decode_ShouldReturnForbiddenException_WhenStatusIs403() {
        // Arrange
        Response response = createFeignResponse(403, "Forbidden Error");

        // Act
        Exception exception = errorDecoderConfig.decode("TestService", response);

        // Assert
        assertThat(exception).isInstanceOf(ForbiddenException.class);
        assertThat(exception.getMessage()).isEqualTo("Forbidden Error");
    }

    @Test
    void decode_ShouldReturnNotFoundException_WhenStatusIs404() {
        // Arrange
        Response response = createFeignResponse(404, "Not Found Error");

        // Act
        Exception exception = errorDecoderConfig.decode("TestService", response);

        // Assert
        assertThat(exception).isInstanceOf(NotFoundException.class);
        assertThat(exception.getMessage()).isEqualTo("Not Found Error");
    }

    @Test
    void decode_ShouldReturnServerErrorException_WhenStatusIs500() {
        // Arrange
        Response response = createFeignResponse(500, "Server Error");

        // Act
        Exception exception = errorDecoderConfig.decode("TestService", response);

        // Assert
        assertThat(exception).isInstanceOf(ServerErrorException.class);
        assertThat(exception.getMessage()).isEqualTo("Server Error");
    }

    @Test
    void decode_ShouldReturnException_WhenStatusIsUnknown() {
        // Arrange
        Response response = createFeignResponse(418, "Unknown Error");

        // Act
        Exception exception = errorDecoderConfig.decode("TestService", response);

        // Assert
        assertThat(exception).isInstanceOf(Exception.class);
        assertThat(exception.getMessage()).isEqualTo("Unknown error: Unknown Error");
    }

    @Test
    void decode_ShouldHandleNullBodyGracefully() {
        // Arrange
        Response response = createFeignResponse(500, null);

        // Act
        Exception exception = errorDecoderConfig.decode("TestService", response);

        // Assert
        assertThat(exception).isInstanceOf(ServerErrorException.class);
        assertThat(exception.getMessage()).isEqualTo("Just Feign Error. No other error message");
    }

    private Response createFeignResponse(int status, String bodyContent) {
        return Response.builder()
                .status(status)
                .reason("Mocked Reason")
                .request(Request.create(Request.HttpMethod.GET, "/test", Collections.emptyMap(), null, null, null))
                .body(bodyContent == null ? null : bodyContent.getBytes())
                .build();
    }
}
