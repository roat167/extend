package com.extend.infrastructure.client;

import com.extend.domain.dto.*;
import com.extend.domain.service.TransactionService;
import com.extend.domain.service.UserService;
import com.extend.domain.service.VirtualCardsService;
import lombok.Data;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@Component
@Slf4j
public class ExtendClient implements VirtualCardsService, TransactionService, UserService {
    private final ExtendAuthConfig extendAuthConfig;
    private final RestTemplate restTemplate;

    public ExtendClient(ExtendAuthConfig extendAuthConfig, RestTemplate restTemplate) {
        this.extendAuthConfig = extendAuthConfig;
        this.restTemplate = restTemplate;
        this.setHeaders();
    }

    /**
     * Set the headers for each request
     */
    private void setHeaders() {
        this.restTemplate.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            request.getHeaders().setAccept(List.of(MediaType.valueOf(this.extendAuthConfig.getApiVersion())));
            return execution.execute(request, body);
        });
    }

    @Override
    public Transaction getTransactionDetails(String accessToken, String transactionId) {
        ResponseEntity<Transaction> transaction = restTemplate.exchange(
                extendAuthConfig.getBaseUrl() +  "/transactions/" + transactionId,
                HttpMethod.GET,
                getHttpEntityBearTokenHeader(accessToken),
                Transaction.class
        );
        return transaction.getBody();
    }

    @Override
    public List<VirtualCard> getVirtualCards(String accessToken) {
        ResponseEntity<VirtualCardResponse> virtualCardResp = restTemplate.exchange(
                extendAuthConfig.getBaseUrl() + "/virtualcards",
                HttpMethod.GET,
                getHttpEntityBearTokenHeader(accessToken),
                VirtualCardResponse.class
        );
        return Objects.nonNull(virtualCardResp.getBody()) ? virtualCardResp.getBody().getVirtualCards() : new ArrayList<>();
    }

    @Override
    public List<Transaction> getAllTransactions(String accessToken, String virtualCardId) {
        StringBuilder urlPath = new StringBuilder(extendAuthConfig.getBaseUrl());
        urlPath.append("/virtualcards/")
                .append(virtualCardId)
                .append("/transactions");
        ResponseEntity<TransactionResponse> transactionResp = restTemplate.exchange(
                urlPath.toString(),
                HttpMethod.GET,
                getHttpEntityBearTokenHeader(accessToken),
                TransactionResponse.class
        );
        return Objects.nonNull(transactionResp.getBody()) ? transactionResp.getBody().getTransactions() : new ArrayList<>();
    }

    @Override
    public UserResponse login(UserRequest userRequest) {
        return restTemplate.postForObject(extendAuthConfig.getBaseUrl() + "/signin", userRequest, UserResponse.class);
    }

    @Override
    public void logout(String refreshToken) {
        restTemplate.postForObject(extendAuthConfig.getBaseUrl() + "/signout", refreshToken, Void.class);
    }

    @Override
    public UserResponse refreshToken(String refreshToken) {
        return restTemplate.postForObject(extendAuthConfig.getBaseUrl() + "/renewauth", refreshToken, UserResponse.class);
    }

    private HttpEntity getHttpEntityBearTokenHeader(String bearToken) {
        HttpHeaders headers = new HttpHeaders();
        // add bearer token header
        headers.setBearerAuth(bearToken);
        return new HttpEntity(headers);
    }
}
