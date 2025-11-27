package com.precisionlex.util;

import com.precisionlex.OfsEnquiryRequest;
import com.precisionlex.OfsEnquirySelectionCriteria;
import com.precisionlex.OfsObjectMapper;
import com.precisionlex.enums.SelectionOperand;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SelectionOperandTest {

    @Test
    void testAllSelectionOperands() {
        OfsEnquiryRequest request = new OfsEnquiryRequest();
        request.setUserId("TEST.USER");
        request.setPassword("123456");
        request.setCompany("GB0010001");
        request.setEnquiryId("CUSTOMER.LIST");

        // Test all operands
        request.addSelectionCriteria("FIELD1", SelectionOperand.EQ, "VALUE1");
        request.addSelectionCriteria("FIELD2", SelectionOperand.LK, "VALUE*");
        request.addSelectionCriteria("FIELD3", SelectionOperand.NE, "VALUE3");
        request.addSelectionCriteria("FIELD4", SelectionOperand.UL, "EXCL*");
        request.addSelectionCriteria("FIELD5", SelectionOperand.GT, "100");
        request.addSelectionCriteria("FIELD6", SelectionOperand.GE, "200");
        request.addSelectionCriteria("FIELD7", SelectionOperand.LT, "300");
        request.addSelectionCriteria("FIELD8", SelectionOperand.LE, "400");
        request.addSelectionCriteria("FIELD9", SelectionOperand.RG, "100 200");

        OfsObjectMapper mapper = new OfsObjectMapper();
         String result = mapper.writeValueAsString(request);

        // Verify all operands are correctly serialized
        assertTrue(result.contains("FIELD1:EQ=VALUE1"));
        assertTrue(result.contains("FIELD2:LK=VALUE*"));
        assertTrue(result.contains("FIELD3:NE=VALUE3"));
        assertTrue(result.contains("FIELD4:UL=EXCL*"));
        assertTrue(result.contains("FIELD5:GT=100"));
        assertTrue(result.contains("FIELD6:GE=200"));
        assertTrue(result.contains("FIELD7:LT=300"));
        assertTrue(result.contains("FIELD8:LE=400"));
        assertTrue(result.contains("FIELD9:RG=100 200"));
    }

    @Test
    void testFromCodeMethod() {
        assertEquals(SelectionOperand.EQ, SelectionOperand.fromCode("EQ"));
        assertEquals(SelectionOperand.LK, SelectionOperand.fromCode("LK"));
        assertEquals(SelectionOperand.NE, SelectionOperand.fromCode("NE"));
        assertEquals(SelectionOperand.UL, SelectionOperand.fromCode("UL"));
        assertEquals(SelectionOperand.GT, SelectionOperand.fromCode("GT"));
        assertEquals(SelectionOperand.GE, SelectionOperand.fromCode("GE"));
        assertEquals(SelectionOperand.LT, SelectionOperand.fromCode("LT"));
        assertEquals(SelectionOperand.LE, SelectionOperand.fromCode("LE"));
        assertEquals(SelectionOperand.RG, SelectionOperand.fromCode("RG"));
    }

    @Test
    void testFromCodeCaseInsensitive() {
        assertEquals(SelectionOperand.EQ, SelectionOperand.fromCode("eq"));
        assertEquals(SelectionOperand.LK, SelectionOperand.fromCode("lk"));
        assertEquals(SelectionOperand.GT, SelectionOperand.fromCode("Gt"));
    }

    @Test
    void testFromCodeWithWhitespace() {
        assertEquals(SelectionOperand.EQ, SelectionOperand.fromCode(" EQ "));
        assertEquals(SelectionOperand.LK, SelectionOperand.fromCode("  LK  "));
    }

    @Test
    void testFromCodeInvalid() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            SelectionOperand.fromCode("INVALID");
        });
        assertTrue(exception.getMessage().contains("Invalid selection operand code"));
        assertTrue(exception.getMessage().contains("INVALID"));
    }

    @Test
    void testFromCodeNull() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            SelectionOperand.fromCode(null);
        });
        assertTrue(exception.getMessage().contains("cannot be null"));
    }

    @Test
    void testGetCode() {
        assertEquals("EQ", SelectionOperand.EQ.getCode());
        assertEquals("LK", SelectionOperand.LK.getCode());
        assertEquals("NE", SelectionOperand.NE.getCode());
        assertEquals("UL", SelectionOperand.UL.getCode());
        assertEquals("GT", SelectionOperand.GT.getCode());
        assertEquals("GE", SelectionOperand.GE.getCode());
        assertEquals("LT", SelectionOperand.LT.getCode());
        assertEquals("LE", SelectionOperand.LE.getCode());
        assertEquals("RG", SelectionOperand.RG.getCode());
    }

    @Test
    void testToString() {
        assertEquals("EQ", SelectionOperand.EQ.toString());
        assertEquals("LK", SelectionOperand.LK.toString());
        assertEquals("RG", SelectionOperand.RG.toString());
    }

    @Test
    void testBackwardsCompatibilityWithStringConstructor() {
        // Test that string operands still work
        OfsEnquirySelectionCriteria criteria1 = new OfsEnquirySelectionCriteria("FIELD", "EQ", "VALUE");
        assertEquals(SelectionOperand.EQ, criteria1.getOperand());
        assertEquals("FIELD:EQ=VALUE", criteria1.toString());

        OfsEnquirySelectionCriteria criteria2 = new OfsEnquirySelectionCriteria("FIELD", "LK", "VAL*");
        assertEquals(SelectionOperand.LK, criteria2.getOperand());
        assertEquals("FIELD:LK=VAL*", criteria2.toString());
    }

    @Test
    void testBackwardsCompatibilityWithStringSetter() {
        OfsEnquirySelectionCriteria criteria = new OfsEnquirySelectionCriteria();
        criteria.setField("FIELD");
        criteria.setOperand("GT");
        criteria.setCriteria("100");

        assertEquals(SelectionOperand.GT, criteria.getOperand());
        assertEquals("FIELD:GT=100", criteria.toString());
    }

    @Test
    void testDirectEnumUsage() {
        // Test using enum directly
        OfsEnquirySelectionCriteria criteria = new OfsEnquirySelectionCriteria(
            "AMOUNT",
            SelectionOperand.GE,
            "1000"
        );

        assertEquals("AMOUNT", criteria.getField());
        assertEquals(SelectionOperand.GE, criteria.getOperand());
        assertEquals("1000", criteria.getCriteria());
        assertEquals("AMOUNT:GE=1000", criteria.toString());
    }

    @Test
    void testMixedEnumAndStringUsage() {
        OfsEnquiryRequest request = new OfsEnquiryRequest();
        request.setUserId("USER");
        request.setPassword("PASS");
        request.setCompany("COMPANY");
        request.setEnquiryId("ENQUIRY");

        // Mix string and enum usage
        request.addSelectionCriteria("FIELD1", "EQ", "VAL1");  // String
        request.addSelectionCriteria("FIELD2", SelectionOperand.LK, "VAL2*");  // Enum

        OfsObjectMapper mapper = new OfsObjectMapper();
        String result = mapper.writeValueAsString(request);

        assertTrue(result.contains("FIELD1:EQ=VAL1"));
        assertTrue(result.contains("FIELD2:LK=VAL2*"));
    }

    @Test
    void testInvalidOperandThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new OfsEnquirySelectionCriteria("FIELD", "INVALID_OP", "VALUE");
        });
        assertTrue(exception.getMessage().contains("Invalid selection operand code"));
    }
}

