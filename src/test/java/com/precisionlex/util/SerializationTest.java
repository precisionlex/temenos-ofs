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
    void testUserInfoUsesLastOccupiedPosition() {
        assertEquals("EB.API,/I/PROCESS,,,",
                serializeWithUserInfo(null, null, null));
        assertEquals("EB.API,/I/PROCESS,,,",
                serializeWithUserInfo("", "", ""));
        assertEquals("EB.API,/I/PROCESS,INPUTT,,",
                serializeWithUserInfo("INPUTT", null, null));
        assertEquals("EB.API,/I/PROCESS,/123456,,",
                serializeWithUserInfo(null, "123456", null));
        assertEquals("EB.API,/I/PROCESS,//GB0010001,,",
                serializeWithUserInfo(null, null, "GB0010001"));
        assertEquals("EB.API,/I/PROCESS,INPUTT/123456,,",
                serializeWithUserInfo("INPUTT", "123456", null));
        assertEquals("EB.API,/I/PROCESS,INPUTT//GB0010001,,",
                serializeWithUserInfo("INPUTT", null, "GB0010001"));
        assertEquals("EB.API,/I/PROCESS,INPUTT,,",
                serializeWithUserInfo("INPUTT", "", ""));
        assertEquals("EB.API,/I/PROCESS,/123456,,",
                serializeWithUserInfo("", "123456", ""));
    }

    private String serializeWithUserInfo(String userId, String password, String company) {
        OfsTransactionRequest request = new OfsTransactionRequest();
        request.setApplication("EB.API");
        request.setRequestType(RequestType.TRANSACTION);
        request.setUserId(userId);
        request.setPassword(password);
        request.setCompany(company);
        return new OfsObjectMapper().writeValueAsString(request);
    }


    @Test
    void testReplaceWithCredentials() {
        OfsTransactionRequest request = new OfsTransactionRequest();
        request.setApplication("EB.API");
        request.setRequestType(RequestType.TRANSACTION);
        request.setAuthorisers(1);
        request.setUserId("INPUTT");
        request.setPassword("123456");
        request.setCompany("GB0010001");
        request.setReplace(true);

        String serialized = new OfsObjectMapper().writeValueAsString(request);

        assertEquals("EB.API,/I/PROCESS//1,INPUTT/123456/GB0010001///1,,", serialized);
    }

    @Test
    void testReplaceWithoutCredentials() {
        OfsTransactionRequest request = new OfsTransactionRequest();
        request.setApplication("EB.API");
        request.setRequestType(RequestType.TRANSACTION);
        request.setAuthorisers(1);
        request.setReplace(true);

        String serialized = new OfsObjectMapper().writeValueAsString(request);

        assertEquals("EB.API,/I/PROCESS//1,/////1,,", serialized);
    }

    @Test
    void testAdditionalUserInfoPositionsWithoutCredentials() {
        assertEquals("EB.API,/I/PROCESS,///VALUE4,,",
                serializeWithAdditionalUserInfo("VALUE4", null, false));
        assertEquals("EB.API,/I/PROCESS,///VALUE4//1,,",
                serializeWithAdditionalUserInfo("VALUE4", null, true));

        assertEquals("EB.API,/I/PROCESS,////VALUE5,,",
                serializeWithAdditionalUserInfo(null, "VALUE5", false));
        assertEquals("EB.API,/I/PROCESS,////VALUE5/1,,",
                serializeWithAdditionalUserInfo(null, "VALUE5", true));

        assertEquals("EB.API,/I/PROCESS,///VALUE4/VALUE5,,",
                serializeWithAdditionalUserInfo("VALUE4", "VALUE5", false));
        assertEquals("EB.API,/I/PROCESS,///VALUE4/VALUE5/1,,",
                serializeWithAdditionalUserInfo("VALUE4", "VALUE5", true));

        assertEquals("EB.API,/I/PROCESS,////VALUE5,,",
                serializeWithAdditionalUserInfo("", "VALUE5", false));
        assertEquals("EB.API,/I/PROCESS,////VALUE5/1,,",
                serializeWithAdditionalUserInfo("", "VALUE5", true));

        assertEquals("EB.API,/I/PROCESS,///VALUE4,,",
                serializeWithAdditionalUserInfo("VALUE4", "", false));
        assertEquals("EB.API,/I/PROCESS,///VALUE4//1,,",
                serializeWithAdditionalUserInfo("VALUE4", "", true));

        assertEquals("EB.API,/I/PROCESS,,,",
                serializeWithAdditionalUserInfo("", "", false));
        assertEquals("EB.API,/I/PROCESS,/////1,,",
                serializeWithAdditionalUserInfo("", "", true));
    }

    private String serializeWithAdditionalUserInfo(String ofsAlias, String userAliasPassword, boolean replace) {
        OfsTransactionRequest request = new OfsTransactionRequest();
        request.setApplication("EB.API");
        request.setRequestType(RequestType.TRANSACTION);
        request.setOfsAlias(ofsAlias);
        request.setUserAliasPassword(userAliasPassword);
        request.setReplace(replace);
        return new OfsObjectMapper().writeValueAsString(request);
    }

    @Test
    void testReplaceWithAdditionalUserInfoPositions() {
        OfsTransactionRequest request = new OfsTransactionRequest();
        request.setApplication("EB.API");
        request.setRequestType(RequestType.TRANSACTION);
        request.setUserId("INPUTT");
        request.setPassword("123456");
        request.setCompany("GB0010001");
        request.setOfsAlias("VALUE4");
        request.setUserAliasPassword("VALUE5");
        request.setReplace(true);

        OfsObjectMapper mapper = new OfsObjectMapper();

        assertEquals("EB.API,/I/PROCESS,INPUTT/123456/GB0010001/VALUE4/VALUE5/1,,",
                mapper.writeValueAsString(request));

        request.setReplace(false);

        assertEquals("EB.API,/I/PROCESS,INPUTT/123456/GB0010001/VALUE4/VALUE5,,",
                mapper.writeValueAsString(request));

        request.setUserAliasPassword(null);

        assertEquals("EB.API,/I/PROCESS,INPUTT/123456/GB0010001/VALUE4,,",
                mapper.writeValueAsString(request));

        request.setOfsAlias(null);

        assertEquals("EB.API,/I/PROCESS,INPUTT/123456/GB0010001,,",
                mapper.writeValueAsString(request));
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

        assertTrue(ofsRequestString.contains("DASH.FIELD:1:1=\"-\""));

        assertTrue(ofsRequestString.contains("NORMAL.FIELD:1:1=\"Normal text\""));
    }

    @Test
    void testEmptyStringRoundTrip() {
        OfsTransactionRequest request = new OfsTransactionRequest();
        request.setApplication("TEST");
        request.setFunction(Function.INPUT);
        request.setRecordId("TEST001");
        request.setField("EMPTY.FIELD", "");
        request.setField("NORMAL.FIELD", "Value");

        OfsObjectMapper mapper = new OfsObjectMapper();
        String serialized = mapper.writeValueAsString(request);

        assertTrue(serialized.contains("EMPTY.FIELD:1:1=\"\""));

        String mockResponse = "TEST001/TXN001/1,"
                + "EMPTY.FIELD:1:1=\"\","
                + "NORMAL.FIELD:1:1=\"Value\"";

        OfsTransactionResponse response = mapper.readTransactionResponse(mockResponse);

        assertEquals("", response.getFields().get("EMPTY.FIELD").get(0).getSimpleValue());
        assertEquals("Value", response.getFields().get("NORMAL.FIELD").get(0).getSimpleValue());
    }

    @Test
    void testDashFieldRoundTrip() {
        OfsTransactionRequest request = new OfsTransactionRequest();
        request.setApplication("TEST");
        request.setFunction("I");
        request.setRecordId("TEST001");
        request.setField("DASH.FIELD", "-");

        OfsObjectMapper mapper = new OfsObjectMapper();
        String serialized = mapper.writeValueAsString(request);

        assertTrue(serialized.contains("DASH.FIELD:1:1=\"-\""));

        String mockResponse = "TEST001/TXN001/1,DASH.FIELD:1:1=\"-\"";

        OfsTransactionResponse response = mapper.readTransactionResponse(mockResponse);

        assertEquals("-", response.getFields().get("DASH.FIELD").get(0).getSimpleValue());
    }
}
