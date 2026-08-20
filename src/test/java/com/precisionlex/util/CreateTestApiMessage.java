package com.precisionlex.util;

import com.precisionlex.OfsObjectMapper;
import com.precisionlex.OfsTransactionRequest;
import com.precisionlex.enums.Function;
import com.precisionlex.enums.ProcessingFlag;

public class CreateTestApiMessage {

    public static void main(String[] args) {
        OfsTransactionRequest request = new OfsTransactionRequest();
        request.setApplication("EB.API");
        request.setVersion("t24data");
        request.setFunction(Function.INPUT);
        request.setProcessingFlag(ProcessingFlag.PROCESS);
        request.setRecordId("TESTAPI");
        request.setAuthorisers(0);
        request.setUserId("ADRIANS1");
        request.setPassword("123456");

        request.setField("SOURCE.TYPE", "JAVA");
        request.setField("DESCRIPTION", "Get SysBillType if AutoWaive, is set to NULL");
        request.setField("PROTECTION.LEVEL", "NONE");

        String ofsMessage = new OfsObjectMapper().writeValueAsString(request);
        System.out.println(ofsMessage);
    }
}
