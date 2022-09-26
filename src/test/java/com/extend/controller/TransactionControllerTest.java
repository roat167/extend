package com.extend.controller;

import com.extend.config.Constants;
import com.extend.config.TestUtils;
import com.extend.domain.dto.Transaction;
import com.extend.infrastructure.client.ExtendClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.HttpClientErrorException;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = TransactionController.class)
@Import({ RestExceptionHandler.class })
class TransactionControllerTest {
    private final String accessToken = "ezToken_1001";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExtendClient extendClient;

    @Test
    void getTransactionDetails_givenTransactionIdIsValid_shouldReturnTransactionDetail() throws Exception {
        Transaction transaction = TestUtils
                .getObjectFromJsonFile("data/transaction-response.json", Transaction.class);
        when(extendClient.getTransactionDetails(accessToken, transaction.getId())).thenReturn(transaction);
        mockMvc.perform(get(Constants.GET_TRANSACTION_DETAIL_URL, transaction.getId())
                        .content(MediaType.APPLICATION_JSON_VALUE)
                        .header("accessToken", accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(transaction.getId()));
        verify(extendClient, times(1)).getTransactionDetails(accessToken, transaction.getId());
    }

    @Test
    void getTransactionDetails_givenTokenInvalid_shouldReturnStatus401() throws Exception {
        when(extendClient.getTransactionDetails(anyString(), anyString())).thenThrow(HttpClientErrorException.Unauthorized.class);
        mockMvc.perform(get(Constants.GET_TRANSACTION_DETAIL_URL, "v_t_1000")
                .header("accessToken", accessToken))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getTransactionDetails_givenTransactionIdIsInValid_shouldReturnNotFound() throws Exception {
        when(extendClient.getTransactionDetails(anyString(), anyString())).thenThrow(HttpClientErrorException.NotFound.class);
        mockMvc.perform(get(Constants.GET_TRANSACTION_DETAIL_URL, "v_t_1001")
                .header("accessToken", accessToken))
                .andExpect(status().isNotFound());
    }
}