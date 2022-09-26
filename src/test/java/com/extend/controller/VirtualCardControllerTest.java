package com.extend.controller;

import com.extend.config.Constants;
import com.extend.config.TestUtils;
import com.extend.domain.dto.TransactionResponse;
import com.extend.domain.dto.VirtualCardResponse;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = VirtualCardController.class)
@Import({ RestExceptionHandler.class })
class VirtualCardControllerTest {
    private final String accessToken = "az_token_100s";
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExtendClient extendClient;

    @Test
    void getAllVirtualCards_whenTokenIsValid_shouldReturnAllUserVirtualCards() throws Exception {
        VirtualCardResponse vcResponse = TestUtils
                .getObjectFromJsonFile("data/virtual-cards-response.json", VirtualCardResponse.class);
        when(extendClient.getVirtualCards(accessToken)).thenReturn(vcResponse.getVirtualCards());
        mockMvc.perform(get(Constants.GET_VIRTUAL_CARDS_URL)
                        .content(MediaType.APPLICATION_JSON_VALUE)
                        .header("accessToken", accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(vcResponse.getVirtualCards().get(0).getId()))
                .andExpect(jsonPath("$[0].availableCredit").value("15.97"));
        verify(extendClient, times(1)).getVirtualCards(accessToken);
    }

    @Test
    void getAllVirtualCards_whenTokenInValid_shouldReturnStatus401() throws Exception {
        when(extendClient.getVirtualCards(accessToken)).thenThrow(HttpClientErrorException.Unauthorized.class);
        mockMvc.perform(get(Constants.GET_VIRTUAL_CARDS_URL)
                .header("accessToken", accessToken))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getVirtualCardTransactions_whenVirtualCardIdValid_shouldReturnAllVirtualCardTransactions() throws Exception {
        String virtualCardId = "v_v_1001";
        TransactionResponse transactionResponse = TestUtils
                .getObjectFromJsonFile("data/transactions-response.json", TransactionResponse.class);
        when(extendClient.getAllTransactions(accessToken, virtualCardId)).thenReturn(transactionResponse.getTransactions());

        mockMvc.perform(get(Constants.GET_VIRTUAL_CARD_TRANSACTION_URL, virtualCardId)
                        .content(MediaType.APPLICATION_JSON_VALUE)
                        .header("accessToken", accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id")
                        .value(transactionResponse.getTransactions().get(0).getId()))
                .andExpect(jsonPath("$[1].vcnLast4")
                        .value(transactionResponse.getTransactions().get(1).getVcnLast4()));
        verify(extendClient, times(1)).getAllTransactions(accessToken, virtualCardId);
    }

    @Test
    void getVirtualCardTransactions_whenVirtualCardIdInValid_shouldReturn404Status() throws Exception {
        when(extendClient.getAllTransactions(anyString(), anyString())).thenThrow(HttpClientErrorException.NotFound.class);
        mockMvc.perform(get(Constants.GET_VIRTUAL_CARD_TRANSACTION_URL, "v_v_1001")
                        .header("accessToken", accessToken))
                .andExpect(status().isNotFound());
    }

    @Test
    void getVirtualCardTransactions_whenInvalidToken_shouldReturn401Status() throws Exception {
        when(extendClient.getAllTransactions(accessToken, "v_v_1002")).thenThrow(HttpClientErrorException.Unauthorized.class);
        mockMvc.perform(get(Constants.GET_VIRTUAL_CARD_TRANSACTION_URL, "v_v_1002")
                        .header("accessToken", accessToken))
                .andExpect(status().isUnauthorized());
    }
}