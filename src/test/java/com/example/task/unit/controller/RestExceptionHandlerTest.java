package com.example.task.unit.controller;

import com.example.task.controller.RestExceptionHandler;
import com.example.task.dto.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RestExceptionHandlerTest {

    private final RestExceptionHandler restExceptionHandler = new RestExceptionHandler();

    @Test
    @DisplayName("Should return api error")
    void Should_ReturnApiError_When_BadRequest() {
        final String url = "/api/v1/animals";
        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn(url);
        final var exception = new IllegalArgumentException("Illegal argument provided");

        final ApiError error = restExceptionHandler.handleBadRequest(exception, request);

        assertThat(error).isNotNull();
        assertThat(error.getStatus()).isEqualTo(SC_BAD_REQUEST);
        assertThat(error.getError()).isEqualTo(exception.getMessage());
        assertThat(error.getPath()).isEqualTo(url);
    }

}
