package org.PayMyBuddy.controller;

import org.PayMyBuddy.model.Contact;
import org.PayMyBuddy.service.contracts.IContactService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ConnectionController.class)
class ConnectionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IContactService iContactService;

    @Test
    @WithMockUser(username = "user@example.com", roles = "USER")
    void testAddConnection_Get() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/connection"))
                .andExpect(status().isOk())
                .andExpect(view().name("connection"));
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = "USER")
    void testAddConnection_Post_Success() throws Exception {
        // Given
        String contactEmail = "test@example.com";

        // Simulate a successful creation of the contact
        Contact contact = new Contact();
        when(iContactService.createConnectionBetweenTwoUsers("user@example.com", contactEmail)).thenReturn(contact);

        // When
        mockMvc.perform(MockMvcRequestBuilders.post("/connection")
                        .with(csrf())
                        .param("contactEmail", contactEmail))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/connection"))
                .andExpect(MockMvcResultMatchers.flash().attributeExists("successMessage"));

        // Then
        verify(iContactService, times(1)).createConnectionBetweenTwoUsers("user@example.com", contactEmail);
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = "USER")
    void testAddConnection_Post_DuplicateError() throws Exception {
        // Given
        String contactEmail = "test@example.com";

        // Simulate a duplicate key error
        when(iContactService.createConnectionBetweenTwoUsers("user@example.com", contactEmail)).thenReturn(null);

        // When
        mockMvc.perform(MockMvcRequestBuilders.post("/connection")
                        .with(csrf())
                        .param("contactEmail", contactEmail))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/connection"))
                .andExpect(MockMvcResultMatchers.flash().attributeExists("errorMessage"));

        // Then
        verify(iContactService, times(1)).createConnectionBetweenTwoUsers("user@example.com", contactEmail);
    }
}
