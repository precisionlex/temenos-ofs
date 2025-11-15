package com.precisionlex.interfaces;

public interface MessageSender {
    void sendMessage(String message, String correlationId);
    void sendError(String message, String details);
}