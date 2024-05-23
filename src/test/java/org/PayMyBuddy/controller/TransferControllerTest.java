package org.PayMyBuddy.controller;

import org.PayMyBuddy.model.User;
import org.PayMyBuddy.service.contracts.ITransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.ui.Model;
import org.mockito.ArgumentCaptor;



import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(controllers = TransferController.class)
class TransferControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ITransactionService transactionService;

    ArgumentCaptor<Model> modelCaptor = ArgumentCaptor.forClass(Model.class);

    @Test
    @WithMockUser(username = "user@example.com", roles = { "USER" })
    void testTransferPage() throws Exception {
        // Given
        User currentUser = new User();
        currentUser.setEmail("user@example.com");
        List<User> contacts = List.of(new User(), new User());
        when(transactionService.getCurrentUser()).thenReturn(currentUser);
        when(transactionService.getUserConnections(currentUser.getEmail())).thenReturn(contacts);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/transfer"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("transfer"))
                .andExpect(MockMvcResultMatchers.model().attribute("contacts", contacts));
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = { "USER" })
    void testPerformTransfer() throws Exception {
        // Given
        int senderId = 1;
        int receiverId = 2;
        String description = "Test transfer";
        BigDecimal amount = BigDecimal.TEN;
        User currentUser = new User();
        currentUser.setId(senderId);
        when(transactionService.getCurrentUserId()).thenReturn(senderId);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.post("/transfer")
                        .with(csrf())
                        .param("transaction_receiver", String.valueOf(receiverId))
                        .param("description", description)
                        .param("amount", amount.toString()))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // Verify that transferMoney method is called with correct parameters
        verify(transactionService, times(1)).transferMoney(eq(senderId), eq(receiverId), eq(description), eq(amount), modelCaptor.capture());

        // Get the captured Model argument
        Model model = modelCaptor.getValue();
    }
}



