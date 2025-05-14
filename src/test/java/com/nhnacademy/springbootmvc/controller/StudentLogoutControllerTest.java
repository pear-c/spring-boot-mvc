package com.nhnacademy.springbootmvc.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StudentLogoutControllerTest {

    @InjectMocks
    private StudentLogoutController studentLogoutController;

    @Mock
    private HttpSession session;

    @Mock
    private HttpServletResponse response;

    @Captor
    private ArgumentCaptor<Cookie> cookieCaptor;

    @Test
    void logout() {
        String viewName = studentLogoutController.logout(session, response);

        verify(session, times(1)).invalidate();
        verify(response, times(1)).addCookie(cookieCaptor.capture());

        Cookie logoutCookie = cookieCaptor.getValue();
        assertThat(logoutCookie.getName()).isEqualTo("SESSION");
        assertThat(logoutCookie.getMaxAge()).isZero();
        assertThat(logoutCookie.getValue()).isNull();
        assertThat(logoutCookie.getPath()).isEqualTo("/");

        assertThat(viewName).isEqualTo("redirect:/login");
    }
}