package com.visma.cash.restmodel;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.joda.money.Money;

public final class JodaMoneyDeserializer extends JsonDeserializer<Money> {

    @Override
    public Money deserialize(JsonParser parser, DeserializationContext context) throws IOException, JsonProcessingException {
        String text = parser.getText();
        System.out.println("Serializing: " + text);
        return Money.parse(text);
    }

}
