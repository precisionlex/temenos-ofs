package com.precisionlex.interfaces;

import com.precisionlex.model.ReceivedDoc;

public interface NameValidator {
    boolean isNameValid(ReceivedDoc doc, String name);
}