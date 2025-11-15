package com.precisionlex.interfaces;

public interface OfsClient {
    String send(String ofsRequest, String correlationId);
}
