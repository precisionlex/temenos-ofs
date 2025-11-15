package com.precisionlex.interfaces;

public interface ReceivedDocStore {
    ReceivedDoc findByMessageAndDocId(String messageId, String docId);
    ReceivedDoc save(ReceivedDoc doc);
}