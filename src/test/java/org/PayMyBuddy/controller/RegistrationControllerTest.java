package org.PayMyBuddy.controller;

import org.PayMyBuddy.model.User;
import org.PayMyBuddy.service.contracts.IUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.validation.BindingResult;
import org.springframework.ui.Model;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@WebMvcTest(controllers = RegistrationController.class)
class RegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IUserService userService;

    @MockBean
    private Model model;

    @MockBean
    private BindingResult bindingResult;

    @Test
    @WithMockUser(username = "user", roles = { "USER" })
    void testShowRegistrationForm() throws Exception {
        // When
        mockMvc.perform(MockMvcRequestBuilders.get("/register"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("register"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("user"));
    }


    @Test
    @WithMockUser(username = "user", roles = { "USER" })
    void testRegisterUser_EmailAlreadyExists() throws Exception {
        // Given
        when(bindingResult.hasErrors()).thenReturn(false);
        when(userService.registerUser(any(User.class))).thenReturn(false);

        // When
        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                        .with(csrf())
                        .param("email", "existing@example.com")
                        .param("password", "password123")
                        .param("firstname", "John")
                        .param("lastname", "Doe")
                        .param("role", "USER")
                        .param("balance", "0")
                        .sessionAttr("user", new User()))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // Then
        verify(userService, times(1)).registerUser(any(User.class));
    }

    @Test
    @WithMockUser(username = "user", roles = { "USER" })
    void testRegisterUser_Success() throws Exception {
        // Given
        when(bindingResult.hasErrors()).thenReturn(false);
        when(userService.registerUser(any(User.class))).thenReturn(true);

        // When
        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                        .with(csrf())
                        .param("email", "new@example.com")
                        .param("password", "password123")
                        .param("firstname", "John")
                        .param("lastname", "Doe")
                        .param("role", "USER")
                        .param("balance", "0")
                        .sessionAttr("user", new User()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/home"));

        // Then
        verify(userService, times(1)).registerUser(any(User.class));
    }
}
