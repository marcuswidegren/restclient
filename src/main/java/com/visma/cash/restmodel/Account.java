package com.visma.cash.restmodel;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import org.joda.money.Money;

import java.util.*;

public final class Account {

    private final Long id;

    private final Set<Transaction> transactions;

    private Money amount;

    @JsonCreator
    public Account(@JsonProperty("id") Long id,
                   @JsonProperty("transactions") Set<Transaction> transactions,
                   @JsonProperty("amount")
                   @JsonDeserialize(using = JodaMoneyDeserializer.class) Money amount) {
        this.id = id;
        this.transactions = transactions;
        this.amount = amount;
    }

    @JsonSerialize(using = JodaMoneySerializer.class)
    public Money getAmount() {
        return amount;
    }

    public Long getId() {
        return id;
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public Collection<Transaction> getTransactions() {
        List<Transaction> trList = new ArrayList<>(transactions);
        Collections.sort(trList);
        return trList;
    }

    @Override
    public String toString() {
        return "Account id: " + id.toString() + ", no of transactions: " + transactions.size();
    }

    public String transactionListAsString() {
        String res = "Account " + getId();
        res += "\n Transactions: \n";
        for(Transaction transaction : transactions) {
            res += transaction.toString();
        }
        return res;
    }
}
