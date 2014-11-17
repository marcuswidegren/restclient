package com.visma.cash.restclient;

import com.visma.cash.restmodel.Account;
import com.visma.cash.restmodel.Transaction;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import com.google.common.base.Optional;

public final class RestClient {

    private final String endpoint;
    private final RestTemplate restTemplate;

    public RestClient(String endpoint) {
        this.endpoint = endpoint;
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setReadTimeout(2000);
        factory.setConnectTimeout(2000);
        restTemplate = new RestTemplate(factory);
    }

    public Optional<Account> findExistingAccount(long id) {
        try {
            return Optional.of(findAccount(id));
        } catch (HttpClientErrorException e) {
            if(e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                return Optional.absent();
            } else {
                throw e;
            }
        }
    }

    public Account findExistingAccount(Account account) {
        return findAccount(account.getId());
    }

    private Account findAccount(long id) {
        return restTemplate.getForObject(endpoint + "/rest/account/" + id, Account.class);
    }

    public Account createAccount() {
        return restTemplate.postForObject(endpoint + "/rest/account", null, Account.class);
    }

    public Transaction addTransaction(Account account, Transaction transaction) {
        return restTemplate.postForObject(endpoint + "/rest/account/" + account.getId() + "/transaction", transaction, Transaction.class);
    }

    public void deleteTransaction(Account account, Transaction transaction) {
        restTemplate.delete(endpoint + "/rest/account/" + account.getId() + "/transaction/" + transaction.getId());
    }

    public boolean isServerAvailable() {
        try {
            restTemplate.getForObject(endpoint + "/rest/ping", String.class);
        } catch (HttpClientErrorException | HttpServerErrorException | ResourceAccessException e) {
            return false;
        }
        return true;
    }

}
