package com.visma.cash.restmodel;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.joda.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.joda.ser.LocalDateTimeSerializer;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;

import java.io.Serializable;
import java.math.BigDecimal;

public final class Transaction implements Comparable<Transaction> {

    private final long id;

    private final Money amount;

    private final LocalDateTime timestamp;

    @JsonCreator
    public Transaction( @JsonProperty("id") long id,
                        @JsonProperty("amount")
                        @JsonDeserialize(using = JodaMoneyDeserializer.class) Money amount,
                        @JsonProperty("timestamp")
                        @JsonDeserialize(using = LocalDateTimeDeserializer.class) LocalDateTime timestamp){
        this.id = id;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    private Transaction(Money amount) {
        this.id = 0;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
    }

    public static Transaction of(Money amount) {
        return new Transaction(amount);
    }

    public static Transaction newTransactionInEuros(BigDecimal amount) {
        return of(Money.of(CurrencyUnit.EUR, amount));
    }

    @JsonDeserialize
    public long getId() {
        return id;
    }

    @JsonSerialize(using = JodaMoneySerializer.class)
    public Money getAmount() {
        return amount;
    }

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "Transaction time: " + timestamp.toString("yyyy-MM-dd'T'HH:mm:ss") + ", amount: " + amount.toString();
    }

    @Override
    public int compareTo(Transaction o) {
        return o.getTimestamp().compareTo(timestamp);
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Transaction)) {
            return false;
        }
        Transaction t = (Transaction) o;
        return (t.getId() == this.getId())
            && (t.getTimestamp().equals(this.getTimestamp()))
            && (t.getAmount().equals(this.getAmount()));
    }

    @Override
    public int hashCode() {
        int result = (int)id;
        result = 31*result + amount.hashCode();
        result = 31*result + timestamp.hashCode();
        return result;
    }

    public static long idNotSet() {
        return 0;
    }

}
