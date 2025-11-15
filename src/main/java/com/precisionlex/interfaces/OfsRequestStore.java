package com.precisionlex.interfaces;

public interface OfsRequestStore {
    OfsRequest createEmpty();
    OfsRequest save(OfsRequest request);
    OfsRequest findByCorrelationId(String correlationId);
}