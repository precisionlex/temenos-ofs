package com.precisionlex.interfaces;

import com.precisionlex.model.ReceivedDoc;

public interface ReceivedDocStore {
    ReceivedDoc findByMessageAndDocId(String messageId, String docId);
    ReceivedDoc save(ReceivedDoc doc);
}