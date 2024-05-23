package org.PayMyBuddy.controller;

import jakarta.servlet.ServletException;
import org.PayMyBuddy.model.User;
import org.PayMyBuddy.service.contracts.IUserService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;

import java.util.Optional;


import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(controllers = HomeController.class)
class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IUserService userService;

    @MockBean
    private Authentication authentication;

    @MockBean
    private SecurityContext securityContext;

    @MockBean
    private UserDetails userDetails;

    @MockBean
    private Model model;

    @InjectMocks
    private HomeController homeController;

    @Test
    @WithMockUser(username = "user@example.com", roles = { "USER" })
    void testHome() throws Exception {
        // Given
        String userEmail = "user@example.com";
        User user = new User();
        user.setEmail(userEmail);

        // when
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(userEmail);
        SecurityContextHolder.setContext(securityContext);

        when(userService.findByEmail(userEmail)).thenReturn(Optional.of(user));

        //  Then
        mockMvc.perform(get("/home"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attribute("user", user));

        verify(userService, times(1)).findByEmail(userEmail);
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = { "USER" })
    void testHomeUserNotFound() throws Exception {
        // Given
        String userEmail = "user@example.com";

        //  When
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(userEmail);
        SecurityContextHolder.setContext(securityContext);

        when(userService.findByEmail(userEmail)).thenReturn(Optional.empty());

        //  Then
        assertThrows(ServletException.class, () -> mockMvc.perform(get("/home")));
    }


}
