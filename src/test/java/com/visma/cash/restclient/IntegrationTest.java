package com.visma.cash.restclient;

import com.google.common.base.Optional;
import com.visma.cash.restmodel.Account;
import com.visma.cash.restmodel.Transaction;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.annotation.processing.SupportedAnnotationTypes;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Random;

import static org.testng.Assert.*;

public class IntegrationTest {

    private final RestClient restClient = new RestClient("http://localhost:8080");
    private final static Money ONE_EURO = Money.of(CurrencyUnit.EUR, BigDecimal.ONE);
    private final static Money ZERO_EUROS = Money.zero(CurrencyUnit.EUR);
    private Account account;

    @BeforeMethod
    public void setUp() {
        account = restClient.createAccount();
    }

    @Test
    public void newAccountShouldBeEmpty() {
        assertEquals(account.getAmount(), ZERO_EUROS);
    }

    @Test
    public void testAddTransaction() {
        Transaction transaction = Transaction.of(ONE_EURO);
        Transaction addedTransaction = restClient.addTransaction(account, transaction);

        assertEquals(transaction.getAmount(), addedTransaction.getAmount());
        assertEquals(transaction.getTimestamp(), addedTransaction.getTimestamp());
        assertEquals(transaction.getId(), Transaction.idNotSet());
        assertNotEquals(addedTransaction.getId(), Transaction.idNotSet());
    }

    @Test
    public void newTransactionShouldAppearInTransactionList() {
        Transaction transaction = Transaction.of(ONE_EURO);
        Transaction addedTransaction = restClient.addTransaction(account, transaction);
        Account updatedAccount = restClient.findExistingAccount(account);

        assertTrue(updatedAccount.getTransactions().contains(addedTransaction));
    }

    @Test
    public void deletedTransactionShouldNotAppearInTransactionList() {
        Transaction transaction = Transaction.of(ONE_EURO);
        Transaction addedTransaction = restClient.addTransaction(account, transaction);
        restClient.deleteTransaction(account, addedTransaction);
        Account updatedAccount = restClient.findExistingAccount(account);

        assertTrue(!updatedAccount.getTransactions().contains(addedTransaction));
    }

    @Test
    public void totalAmountShouldUpdateAfterAddedTransaction() {
        Transaction transaction = Transaction.of(ONE_EURO);
        restClient.addTransaction(account, transaction);
        Account updatedAccount = restClient.findExistingAccount(account);

        assertEquals(updatedAccount.getAmount(), ONE_EURO);
    }

    @Test
    public void findFakeAccountShouldReturnEmpty() {
        Random random = new Random();
        Optional<Account> accountOptional = restClient.findExistingAccount(random.nextLong());

        assertTrue(!accountOptional.isPresent());
    }

    @Test
    public void findNonExistingAccountShouldFail() {
        Random random = new Random();
        try {
            restClient.findExistingAccount(new Account(random.nextLong(), new HashSet<Transaction>(), ZERO_EUROS));
            fail();
        } catch(HttpClientErrorException e) {
            assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
        }
    }
}
