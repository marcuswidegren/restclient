package com.visma.cash.restmodel;

import java.io.IOException;

import org.joda.money.Money;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public final class JodaMoneySerializer extends JsonSerializer<Money> {

	@Override
	public void serialize(Money money, JsonGenerator jgen,
			SerializerProvider serProv) throws IOException,
			JsonProcessingException {
        System.out.println("Serializing: " + money.toString());
		jgen.writeString(money.toString());
		
	}

}
