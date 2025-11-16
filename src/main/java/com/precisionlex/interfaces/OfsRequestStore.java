package com.precisionlex.interfaces;

import com.precisionlex.model.OfsRequest;

public interface OfsRequestStore {
    OfsRequest createEmpty();
    OfsRequest save(OfsRequest request);
    OfsRequest findByCorrelationId(String correlationId);
}