package com.precisionlex.util;

import com.precisionlex.OfsObjectMapper;
import com.precisionlex.OfsTransactionRequest;
import com.precisionlex.enums.Function;
import com.precisionlex.enums.ProcessingFlag;
import com.precisionlex.enums.RequestType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SerializationTest {

    @Test
    void testTransactionSerialization() {

        OfsTransactionRequest request = new OfsTransactionRequest();
        request.setApplication("FUNDS.TRANSFER");
        request.setVersion("CREATE");
        request.setRequestType(RequestType.TRANSACTION);
        request.setFunction(Function.INPUT);
        request.setProcessingFlag(ProcessingFlag.PROCESS);
        request.setUserId("INPUTTER");
        request.setPassword("123456");
        request.setCompany("GB0010001");
        request.setRecordId("FT2503100001");

        request.setField("TRANSACTION.TYPE", "AC");
        request.setField("DEBIT.ACCT.NO", "12345678");
        request.setField("CREDIT.ACCT.NO", "87654321");
        request.setField("DEBIT.CURRENCY", "USD");
        request.setField("DEBIT.AMOUNT", "100.00");

        request.setField("PAYMENT.DETAILS", java.util.Arrays.asList("line 1", "line 2", "line 3"));

        OfsObjectMapper mapper = new OfsObjectMapper();
        String ofsRequstString = mapper.writeValueAsString(request);

        String expectedResult = "FUNDS.TRANSFER,CREATE/I/PROCESS,INPUTTER/123456/GB0010001,FT2503100001,"
                + "TRANSACTION.TYPE:1:1=\"AC\","
                + "DEBIT.ACCT.NO:1:1=\"12345678\","
                + "CREDIT.ACCT.NO:1:1=\"87654321\","
                + "DEBIT.CURRENCY:1:1=\"USD\","
                + "DEBIT.AMOUNT:1:1=\"100.00\","
                + "PAYMENT.DETAILS:1:1=\"line 1\","
                + "PAYMENT.DETAILS:2:1=\"line 2\","
                + "PAYMENT.DETAILS:3:1=\"line 3\"";

        assertEquals(expectedResult, ofsRequstString);

    }

}
