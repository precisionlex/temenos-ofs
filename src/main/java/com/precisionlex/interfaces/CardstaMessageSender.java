package com.precisionlex.interfaces;

import com.precisionlex.model.ReceivedDoc;
import com.precisionlex.model.RequestedPayment;

public interface CardstaMessageSender {
    void send(Object message);
}