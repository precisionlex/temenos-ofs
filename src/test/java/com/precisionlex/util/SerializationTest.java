package com.precisionlex.util;

import com.precisionlex.OfsObjectMapper;
import com.precisionlex.OfsTransactionRequest;
import com.precisionlex.OfsTransactionResponse;
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

    @Test
    void testNumberOfAuthorisers() {
        OfsTransactionRequest request = new OfsTransactionRequest();
        request.setApplication("FUNDS.TRANSFER");
        request.setRequestType(RequestType.TRANSACTION);
        request.setAuthorisers(0);

        request.setField("TRANSACTION.TYPE", "AC");
        request.setField("DEBIT.ACCT.NO", "12345678");
        request.setField("CREDIT.ACCT.NO", "87654321");
        request.setField("DEBIT.CURRENCY", "USD");
        request.setField("DEBIT.AMOUNT", "100.00");

        request.setField("PAYMENT.DETAILS", java.util.Arrays.asList("line 1", "line 2", "line 3"));

        OfsObjectMapper mapper = new OfsObjectMapper();
        String ofsRequstString = mapper.writeValueAsString(request);

        String expectedResult = "FUNDS.TRANSFER,/I/PROCESS//0,,,"
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

    @Test
    void testReservedCharacterSanitization() {
        OfsTransactionRequest request = new OfsTransactionRequest();
        request.setApplication("FUNDS.TRANSFER");
        request.setVersion("TEST");
        request.setFunction(Function.INPUT);
        request.setProcessingFlag(ProcessingFlag.PROCESS);
        request.setRecordId("TEST001");

        request.setField("DESCRIPTION", "Test|pipe\"quote?mark,comma_under^caret/slash");
        request.setField("DASH.FIELD", "-");
        request.setField("NORMAL.FIELD", "Normal text");

        OfsObjectMapper mapper = new OfsObjectMapper();
        String ofsRequestString = mapper.writeValueAsString(request);

        assertTrue(ofsRequestString.contains("Test%|%pipe\"|\"quote%?%mark\"?\"comma'_'under%^%caret\"^\"slash"));

        assertTrue(ofsRequestString.contains("DASH.FIELD:1:1=\"NA\""));

        assertTrue(ofsRequestString.contains("NORMAL.FIELD:1:1=\"Normal text\""));
    }

    @Test
    void testNADeserialization() {
        // Test that "NA" in response is converted back to empty string
        String ofsResponse = "TEST001/TXN001/1,"
                + "FIELD1:1:1=\"NA\","
                + "FIELD2:1:1=\"Normal text\"";

        OfsObjectMapper mapper = new OfsObjectMapper();
        OfsTransactionResponse response = mapper.readTransactionResponse(ofsResponse);

        // "NA" should be deserialized to empty string
        assertEquals("", response.getFields().get("FIELD1").get(0).getSimpleValue());
        assertEquals("Normal text", response.getFields().get("FIELD2").get(0).getSimpleValue());
    }

    @Test
    void testEmptyStringRoundTrip() {
        // Test that empty string serializes to "NA" and deserializes back to empty string
        OfsTransactionRequest request = new OfsTransactionRequest();
        request.setApplication("TEST");
        request.setFunction(Function.INPUT);
        request.setRecordId("TEST001");
        request.setField("EMPTY.FIELD", "");
        request.setField("NORMAL.FIELD", "Value");

        OfsObjectMapper mapper = new OfsObjectMapper();
        String serialized = mapper.writeValueAsString(request);

        // Empty string should serialize to "NA"
        assertTrue(serialized.contains("EMPTY.FIELD:1:1=\"NA\""));

        // Simulate response from T24
        String mockResponse = "TEST001/TXN001/1,"
                + "EMPTY.FIELD:1:1=\"NA\","
                + "NORMAL.FIELD:1:1=\"Value\"";

        OfsTransactionResponse response = mapper.readTransactionResponse(mockResponse);

        // "NA" should deserialize back to empty string
        assertEquals("", response.getFields().get("EMPTY.FIELD").get(0).getSimpleValue());
        assertEquals("Value", response.getFields().get("NORMAL.FIELD").get(0).getSimpleValue());
    }

    @Test
    void testDashFieldRoundTrip() {
        // Test that single dash serializes to "NA" and deserializes back to empty string
        OfsTransactionRequest request = new OfsTransactionRequest();
        request.setApplication("TEST");
        request.setFunction(Function.INPUT);
        request.setRecordId("TEST001");
        request.setField("DASH.FIELD", "-");

        OfsObjectMapper mapper = new OfsObjectMapper();
        String serialized = mapper.writeValueAsString(request);

        // Single dash should serialize to "NA"
        assertTrue(serialized.contains("DASH.FIELD:1:1=\"NA\""));

        // Simulate response from T24
        String mockResponse = "TEST001/TXN001/1,DASH.FIELD:1:1=\"NA\"";

        OfsTransactionResponse response = mapper.readTransactionResponse(mockResponse);

        // "NA" should deserialize back to empty string (not dash, since dash is for deletion)
        assertEquals("", response.getFields().get("DASH.FIELD").get(0).getSimpleValue());
    }
}
