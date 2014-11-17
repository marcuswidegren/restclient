package com.visma.cash.restclient;

import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;


public class IntegrationTestSmall {

    private final RestClient restClient = new RestClient("http://localhost:8080");

    @Test
    public void ping() {
        assertTrue(restClient.isServerAvailable());
    }
}
