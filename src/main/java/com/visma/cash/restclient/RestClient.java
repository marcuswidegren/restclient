package com.visma.cash.restclient;

import com.visma.cash.restmodel.Account;
import com.visma.cash.restmodel.Transaction;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import com.google.common.base.Optional;

public final class RestClient {
    private final String endpoint;
    private final RestTemplate restTemplate = new RestTemplate();

    public RestClient(String endpoint) {
        this.endpoint = endpoint;
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


}
