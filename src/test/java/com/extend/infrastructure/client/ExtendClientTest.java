package com.extend.infrastructure.client;

import com.extend.domain.lookup.VirtualCardStatus;
import com.extend.domain.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExtendClientTest {
    private final String accessToken = "ezToken_abcd";
    @Mock
    private ExtendAuthConfig extendAuthConfig;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    HttpEntity httpEntity;
    @InjectMocks
    private ExtendClient extendClient;

    @BeforeEach
    public void initMock() {
        when(extendAuthConfig.getBaseUrl()).thenReturn("http://localhost:8080");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(accessToken);
        httpEntity  = new HttpEntity<>(httpHeaders);
    }

    @Test
    void getTransactionDetails_givenValidTransactionId_shouldReturnTransaction() {
        String transactionId = "v_1001";
        Transaction expected = mockTransaction();
        when(restTemplate.exchange(
                extendAuthConfig.getBaseUrl() +  "/transactions/" + transactionId,
                HttpMethod.GET,
                httpEntity,
                Transaction.class
        )).thenReturn(new ResponseEntity<>(expected, HttpStatus.OK));
        Transaction transaction = extendClient.getTransactionDetails(accessToken, transactionId);
        assertEquals(transaction, expected);
    }

    // invalid transaction ID refer to non-exist id
    @Test
    void getTransactionDetails_givenInvalidTransactionId_shouldThrowException() {
        when(restTemplate.exchange(
                extendAuthConfig.getBaseUrl() +  "/transactions/v_t1002" ,
                HttpMethod.GET,
                httpEntity,
                Transaction.class
        )).thenThrow(HttpClientErrorException.NotFound.class);

        assertThrows(HttpClientErrorException.NotFound.class,
                () -> extendClient.getTransactionDetails(accessToken, "v_t1002"));
    }

    @Test
    void getVirtualCards_shouldReturnListOfVirtualCard() {
        VirtualCardResponse virtualCardResponse = new VirtualCardResponse();
        virtualCardResponse.setVirtualCards(
                List.of(mockVirtualCard("v_c_1001", BigDecimal.valueOf(1500), VirtualCardStatus.ACTIVE),
                        mockVirtualCard("v_c_1002", BigDecimal.valueOf(2000), VirtualCardStatus.ACTIVE),
                        mockVirtualCard("v_c_1003", BigDecimal.valueOf(1000), VirtualCardStatus.PENDING)));
        when(restTemplate.exchange(
                extendAuthConfig.getBaseUrl() +  "/virtualcards",
                HttpMethod.GET,
                httpEntity,
                VirtualCardResponse.class))
                .thenReturn(new ResponseEntity<>(virtualCardResponse, HttpStatus.OK));
        List<VirtualCard> virtualCards = extendClient.getVirtualCards(accessToken);
        assertEquals(virtualCards, virtualCardResponse.getVirtualCards());
    }

    @Test
    void getAllTransactions_givenValidVirtualCardId_shouldReturnListOfTransaction() {
        String virtualCardId = "v_c_1001";
        TransactionResponse transactionResponse = new TransactionResponse();
        when(restTemplate.exchange(
                extendAuthConfig.getBaseUrl() +  "/virtualcards/" + virtualCardId+"/transactions",
                HttpMethod.GET,
                httpEntity,
                TransactionResponse.class))
                .thenReturn(new ResponseEntity<>(transactionResponse, HttpStatus.OK));
        List<Transaction> transactions = extendClient.getAllTransactions(accessToken, virtualCardId);
        assertEquals(transactions, transactionResponse.getTransactions());
    }

    // invalid Virtual card ID refer to non-exist id
    @Test
    void getAllTransactions_givenInvalidVirtualCardId_shouldThrowException() {
        when(restTemplate.exchange(
                extendAuthConfig.getBaseUrl() +  "/virtualcards/v_c_1001/transactions",
                HttpMethod.GET,
                httpEntity,
                TransactionResponse.class))
                .thenThrow(HttpClientErrorException.NotFound.class);

        assertThrows(HttpClientErrorException.NotFound.class,
                () -> extendClient.getAllTransactions(accessToken, "v_c_1001"));
    }

    @Test
    void login_givenValidUserRequest_shouldReturnToken() {
        UserRequest userRequest = new UserRequest("demo@extend.com", "@demo-1234");
        UserResponse userResponse = new UserResponse("ezb1001", "ezbpg1002");
        when(restTemplate.postForObject(extendAuthConfig.getBaseUrl() + "/signin", userRequest, UserResponse.class))
                .thenReturn(userResponse);
        UserResponse result = extendClient.login(userRequest);
        assertEquals(result.getToken(), userResponse.getToken());
        assertEquals(result.getRefreshToken(), userResponse.getRefreshToken());
    }

    @Test
    void login_givenInValidUserRequest_shouldThrowUnAuthorizedException() {
        UserRequest userRequest = new UserRequest("demo@extend.com", "@demo-1234");
        when(restTemplate
                .postForObject(extendAuthConfig.getBaseUrl() + "/signin", userRequest, UserResponse.class)
        ).thenThrow(HttpClientErrorException.Unauthorized.class);

        assertThrows(HttpClientErrorException.Unauthorized.class,
                () -> extendClient.login(userRequest));
    }

    public Transaction mockTransaction() {
        return Transaction.builder()
                .id("v_transaction_001")
                .cardholderId("v_cardholder_001")
                .authedAt(LocalDateTime.now())
                .cardholderName("demo")
                .build();
    }

    public VirtualCard mockVirtualCard(String virtualCardId, BigDecimal balanceCent, VirtualCardStatus status) {
        return VirtualCard.builder()
                .id(virtualCardId)
                .balanceCents(balanceCent)
                .displayName("Demo")
                .status(status)
                .validTo(LocalDateTime.now().plusYears(1))
                .build();
    }
}