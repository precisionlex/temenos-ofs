package com.precisionlex.util;

import com.precisionlex.OfsObjectMapper;
import com.precisionlex.OfsResponse;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DeserializationTest {

    @Test
    void testDeserializationValidResponse() {
        String ofsResponse = "17000000426/BNXVIB253211625630142.00/1,"
                + "MASTER.ACCOUNT:1:1=1000000098,"
                + "CUSTOMER:1:1=100123,"
                + "VIBAN:1:1=LT743420017000000426,"
                + "CURRENCY:1:1=EUR,"
                + "STATUS:1:1=OPEN,"
                + "RESTRICTED:1:1=N,"
                + "CREATED.DATE:1:1=20250308,"
                + "CURR.NO:1:1=1,"
                + "INPUTTER:1:1=16256_GTUSER_I_INAU_OFS_BNX.VIBAN.PREAUTH,"
                + "DATE.TIME:1:1=2511170822,"
                + "DATE.TIME:2:1=2511170822,"
                + "AUTHORISER:1:1=16256_GTUSER_OFS_BNX.VIBAN.PREAUTH,"
                + "CO.CODE:1:1=LT0010001,"
                + "DEPT.CODE:1:1=1";

        OfsObjectMapper mapper = new OfsObjectMapper();
        OfsResponse response = mapper.readValue(ofsResponse, OfsResponse.class);

        assertEquals("17000000426", response.getRecordId());
        assertEquals("BNXVIB253211625630142.00", response.getTransactionRef());
        assertEquals("1", response.getStatus());
        assertTrue(response.isSuccess());

        assertEquals("1000000098", response.getFields().get("MASTER.ACCOUNT").get(0).getSimpleValue());
        assertEquals("100123", response.getFields().get("CUSTOMER").get(0).getSimpleValue());
        assertEquals("LT743420017000000426", response.getFields().get("VIBAN").get(0).getSimpleValue());
        assertEquals("EUR", response.getFields().get("CURRENCY").get(0).getSimpleValue());
        assertEquals("OPEN", response.getFields().get("STATUS").get(0).getSimpleValue());
        assertEquals("N", response.getFields().get("RESTRICTED").get(0).getSimpleValue());
        assertEquals("20250308", response.getFields().get("CREATED.DATE").get(0).getSimpleValue());

        List<String> dateTimes = response.getFields().get("DATE.TIME").get(0).getMultiValues();
        assertEquals("2511170822", dateTimes.get(0));
        assertEquals("2511170822", dateTimes.get(1));
    }

    @Test
    void testDeserializationErrorResponse() {
        String ofsResponse = "17000000426/BNXVIB253211625630096.00/-1/NO,"
                + "CUSTOMER:1:1=INVALID CUSTOMER FOR MASTER ACCOUNT";

        OfsObjectMapper mapper = new OfsObjectMapper();
        OfsResponse response = mapper.readValue(ofsResponse, OfsResponse.class);

        assertEquals("17000000426", response.getRecordId());
        assertEquals("BNXVIB253211625630096.00", response.getTransactionRef());
        assertEquals("-1", response.getStatus());
        assertEquals("NO", response.getErrorCode());
        assertTrue(response.isError());

        assertEquals("INVALID CUSTOMER FOR MASTER ACCOUNT",
                response.getFields().get("CUSTOMER").get(0).getSimpleValue());
    }

    @Test
    void testDeserializationWithDifferentFieldOrder() {
        // Fields in completely different order than first test
        String ofsResponse = "17000000426/BNXVIB253211625630142.00/1,"
                + "CURRENCY:1:1=EUR,"
                + "CUSTOMER:1:1=100123,"
                + "VIBAN:1:1=LT743420017000000426,"
                + "MASTER.ACCOUNT:1:1=1000000098,"
                + "STATUS:1:1=OPEN";

        OfsObjectMapper mapper = new OfsObjectMapper();
        OfsResponse response = mapper.readValue(ofsResponse, OfsResponse.class);

        // Header should parse correctly regardless of field order
        assertEquals("17000000426", response.getRecordId());
        assertEquals("BNXVIB253211625630142.00", response.getTransactionRef());
        assertEquals("1", response.getStatus());
        assertTrue(response.isSuccess());

        // Fields should be accessible regardless of order
        assertEquals("1000000098", response.getFields().get("MASTER.ACCOUNT").get(0).getSimpleValue());
        assertEquals("100123", response.getFields().get("CUSTOMER").get(0).getSimpleValue());
        assertEquals("LT743420017000000426", response.getFields().get("VIBAN").get(0).getSimpleValue());
        assertEquals("EUR", response.getFields().get("CURRENCY").get(0).getSimpleValue());
        assertEquals("OPEN", response.getFields().get("STATUS").get(0).getSimpleValue());
    }

    @Test
    void testDeserializationWithMissingOptionalFields() {
        // Minimal response with only required fields
        String ofsResponse = "17000000426/BNXVIB253211625630142.00/1,"
                + "CUSTOMER:1:1=100123";

        OfsObjectMapper mapper = new OfsObjectMapper();
        OfsResponse response = mapper.readValue(ofsResponse, OfsResponse.class);

        // Header should parse correctly
        assertEquals("17000000426", response.getRecordId());
        assertEquals("BNXVIB253211625630142.00", response.getTransactionRef());
        assertEquals("1", response.getStatus());
        assertNull(response.getErrorCode()); // Optional, should be null
        assertTrue(response.isSuccess());

        // Only one field present
        assertEquals("100123", response.getFields().get("CUSTOMER").get(0).getSimpleValue());

        // Other fields should not exist
        assertNull(response.getFields().get("MASTER.ACCOUNT"));
        assertNull(response.getFields().get("CURRENCY"));
        assertNull(response.getFields().get("VIBAN"));
    }

    @Test
    void testDeserializationWithOnlyHeader() {
        // Response with header but no fields
        String ofsResponse = "17000000426/BNXVIB253211625630142.00/1";

        OfsObjectMapper mapper = new OfsObjectMapper();
        OfsResponse response = mapper.readValue(ofsResponse, OfsResponse.class);

        // Header should parse correctly
        assertEquals("17000000426", response.getRecordId());
        assertEquals("BNXVIB253211625630142.00", response.getTransactionRef());
        assertEquals("1", response.getStatus());
        assertNull(response.getErrorCode());
        assertTrue(response.isSuccess());

        // No fields should be present
        assertTrue(response.getFields().isEmpty());
    }

    @Test
    void testDeserializationWithMultipleMultiValueFields() {
        // Multiple fields with multi-values in mixed order
        String ofsResponse = "17000000426/TEST123/1,"
                + "FIELD.A:1:1=A1,"
                + "FIELD.B:1:1=B1,"
                + "FIELD.A:2:1=A2,"
                + "FIELD.B:2:1=B2,"
                + "FIELD.A:3:1=A3,"
                + "FIELD.B:3:1=B3";

        OfsObjectMapper mapper = new OfsObjectMapper();
        OfsResponse response = mapper.readValue(ofsResponse, OfsResponse.class);

        // Multi-values should be grouped correctly by field name
        List<String> fieldAValues = response.getFields().get("FIELD.A").get(0).getMultiValues();
        assertEquals(3, fieldAValues.size());
        assertEquals("A1", fieldAValues.get(0));
        assertEquals("A2", fieldAValues.get(1));
        assertEquals("A3", fieldAValues.get(2));

        List<String> fieldBValues = response.getFields().get("FIELD.B").get(0).getMultiValues();
        assertEquals(3, fieldBValues.size());
        assertEquals("B1", fieldBValues.get(0));
        assertEquals("B2", fieldBValues.get(1));
        assertEquals("B3", fieldBValues.get(2));
    }

    @Test
    void testDeserializationWithSparseMultiValueIndices() {
        // Multi-values with gaps (indices 1, 3, 5 - missing 2, 4)
        String ofsResponse = "17000000426/TEST123/1,"
                + "FIELD.A:1:1=Value1,"
                + "FIELD.A:3:1=Value3,"
                + "FIELD.A:5:1=Value5";

        OfsObjectMapper mapper = new OfsObjectMapper();
        OfsResponse response = mapper.readValue(ofsResponse, OfsResponse.class);

        // Should handle sparse indices with empty strings for gaps
        List<String> fieldValues = response.getFields().get("FIELD.A").get(0).getMultiValues();
        assertEquals(5, fieldValues.size());
        assertEquals("Value1", fieldValues.get(0));
        assertEquals("", fieldValues.get(1));  // Gap
        assertEquals("Value3", fieldValues.get(2));
        assertEquals("", fieldValues.get(3));  // Gap
        assertEquals("Value5", fieldValues.get(4));
    }

    @Test
    void testDeserializationWithEmptyValues() {
        // Fields with empty values
        String ofsResponse = "17000000426/TEST123/1,"
                + "FIELD.A:1:1=,"
                + "FIELD.B:1:1=\"\","
                + "FIELD.C:1:1=ActualValue";

        OfsObjectMapper mapper = new OfsObjectMapper();
        OfsResponse response = mapper.readValue(ofsResponse, OfsResponse.class);

        // Empty values should be preserved
        assertEquals("", response.getFields().get("FIELD.A").get(0).getSimpleValue());
        assertEquals("", response.getFields().get("FIELD.B").get(0).getSimpleValue());
        assertEquals("ActualValue", response.getFields().get("FIELD.C").get(0).getSimpleValue());
    }

    @Test
    void testDeserializationWithSpecialCharactersInValues() {
        // Values with special characters
        String ofsResponse = "17000000426/TEST123/1,"
                + "FIELD.A:1:1=\"Value with spaces\","
                + "FIELD.B:1:1=\"Value/with/slashes\","
                + "FIELD.C:1:1=\"Value:with:colons\"";

        OfsObjectMapper mapper = new OfsObjectMapper();
        OfsResponse response = mapper.readValue(ofsResponse, OfsResponse.class);

        // Special characters should be preserved (quotes removed)
        assertEquals("Value with spaces", response.getFields().get("FIELD.A").get(0).getSimpleValue());
        assertEquals("Value/with/slashes", response.getFields().get("FIELD.B").get(0).getSimpleValue());
        assertEquals("Value:with:colons", response.getFields().get("FIELD.C").get(0).getSimpleValue());
    }
}
