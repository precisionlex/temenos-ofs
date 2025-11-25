package com.precisionlex.util;

import com.precisionlex.OfsEnquiryRequest;
import com.precisionlex.OfsObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EnquirySerializationTest {

    @Test
    void testEnquirySerializationWithSelectionCriteria() {
        // Test case: ENQUIRY.SELECT,,TEST.USER/654321,CURRENCY-LIST,@ID:LK=C...
        OfsEnquiryRequest request = new OfsEnquiryRequest();
        request.setUserId("TEST.USER");
        request.setPassword("654321");
        request.setCompany("");
        request.setEnquiryId("CURRENCY-LIST");
        request.addSelectionCriteria("@ID", "LK", "C...");

        OfsObjectMapper mapper = new OfsObjectMapper();
        String ofsRequestString = mapper.writeValueAsString(request);

        String expectedResult = "ENQUIRY.SELECT,,TEST.USER/654321/,CURRENCY-LIST,@ID:LK=C...";

        assertEquals(expectedResult, ofsRequestString);
    }

    @Test
    void testEnquirySerializationWithMultipleCriteria() {
        OfsEnquiryRequest request = new OfsEnquiryRequest();
        request.setUserId("INPUTTER");
        request.setPassword("123456");
        request.setCompany("GB0010001");
        request.setEnquiryId("CUSTOMER.LIST");
        request.addSelectionCriteria("CUSTOMER.NAME", "LK", "JOHN");
        request.addSelectionCriteria("ACCOUNT.OFFICER", "EQ", "123");

        OfsObjectMapper mapper = new OfsObjectMapper();
        String ofsRequestString = mapper.writeValueAsString(request);

        String expectedResult = "ENQUIRY.SELECT,,INPUTTER/123456/GB0010001,CUSTOMER.LIST," +
                "CUSTOMER.NAME:LK=JOHN,ACCOUNT.OFFICER:EQ=123";

        assertEquals(expectedResult, ofsRequestString);
    }

    @Test
    void testEnquirySerializationWithoutCriteria() {
        // Simple enquiry without selection criteria
        OfsEnquiryRequest request = new OfsEnquiryRequest();
        request.setUserId("USER1");
        request.setPassword("PASS123");
        request.setCompany("GB0010001");
        request.setEnquiryId("ALL.CURRENCIES");

        OfsObjectMapper mapper = new OfsObjectMapper();
        String ofsRequestString = mapper.writeValueAsString(request);

        String expectedResult = "ENQUIRY.SELECT,,USER1/PASS123/GB0010001,ALL.CURRENCIES,";

        assertEquals(expectedResult, ofsRequestString);
    }

    @Test
    void testEnquirySerializationWithAllOperands() {
        // Test different operands: EQ, NE, GE, GT, LE, LT, UL, LK, NR
        OfsEnquiryRequest request = new OfsEnquiryRequest();
        request.setUserId("TESTER");
        request.setPassword("TEST123");
        request.setCompany("TEST.CO");
        request.setEnquiryId("ACCOUNT.ENQUIRY");

        request.addSelectionCriteria("FIELD1", "EQ", "VALUE1");
        request.addSelectionCriteria("FIELD2", "NE", "VALUE2");
        request.addSelectionCriteria("FIELD3", "GT", "100");
        request.addSelectionCriteria("FIELD4", "LK", "ABC");

        OfsObjectMapper mapper = new OfsObjectMapper();
        String ofsRequestString = mapper.writeValueAsString(request);

        String expectedResult = "ENQUIRY.SELECT,,TESTER/TEST123/TEST.CO,ACCOUNT.ENQUIRY," +
                "FIELD1:EQ=VALUE1,FIELD2:NE=VALUE2,FIELD3:GT=100,FIELD4:LK=ABC";

        assertEquals(expectedResult, ofsRequestString);
    }
}

