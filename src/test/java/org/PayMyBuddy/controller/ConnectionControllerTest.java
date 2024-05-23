package org.PayMyBuddy.controller;

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
    void testAddConnection_Post() throws Exception {
        // Given
        String contactEmail = "test@example.com";

        // When
        mockMvc.perform(MockMvcRequestBuilders.post("/connection")
                        .with(csrf())
                        .param("contactEmail", contactEmail))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // Then
        verify(iContactService, times(1)).createConnectionBetweenTwoUsers("user@example.com", contactEmail);
    }
}
