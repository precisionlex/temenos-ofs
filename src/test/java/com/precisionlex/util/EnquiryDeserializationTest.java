package com.precisionlex.util;

import com.precisionlex.OfsEnquiryResponse;
import com.precisionlex.OfsObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EnquiryDeserializationTest {

    @Test
    void testEnquiryDeserializationSuccess() {

        String ofsResponse = ",@ID::Ccy/CCY.NAME::Currency Name,\"CAD\"\t\"CANADIAN DOLLAR\",\"CHF\"\t\"SWISS FRANCS\"";

        OfsObjectMapper mapper = new OfsObjectMapper();
        OfsEnquiryResponse response = mapper.readEnquiryResponse(ofsResponse);

        assertTrue(response.isSuccess());
        assertFalse(response.isError());

        // Check columns
        assertEquals(2, response.getColumnCount());
        assertEquals("@ID", response.getColumns().get(0).getIdentifier());
        assertEquals("Ccy", response.getColumns().get(0).getLabel());
        assertEquals("CCY.NAME", response.getColumns().get(1).getIdentifier());
        assertEquals("Currency Name", response.getColumns().get(1).getLabel());

        // Check rows
        assertEquals(2, response.getRowCount());

        // First row
        assertEquals("CAD", response.getCellValue(0, 0));
        assertEquals("CANADIAN DOLLAR", response.getCellValue(0, 1));

        // Second row
        assertEquals("CHF", response.getCellValue(1, 0));
        assertEquals("SWISS FRANCS", response.getCellValue(1, 1));

        // Test accessing by column identifier
        assertEquals("CAD", response.getCellValue(0, "@ID"));
        assertEquals("CANADIAN DOLLAR", response.getCellValue(0, "CCY.NAME"));
    }

    @Test
    void testEnquiryDeserializationError() {
        // Error format: <<ENQUIRY.NAME>>//-1/,<<Error message>>
        String ofsResponse = "CURRENCY-LIST//-1/,Invalid user credentials";

        OfsObjectMapper mapper = new OfsObjectMapper();
        OfsEnquiryResponse response = mapper.readEnquiryResponse(ofsResponse);

        assertTrue(response.isError());
        assertFalse(response.isSuccess());
        assertEquals("CURRENCY-LIST", response.getErrorEnquiryName());
        assertEquals("Invalid user credentials", response.getErrorMessage());
    }

    @Test
    void testEnquiryDeserializationWithEmptyResult() {
        // Empty result (null result from enquiry)
        String ofsResponse = ",@ID::Key/CCY.NAME::Name,";

        OfsObjectMapper mapper = new OfsObjectMapper();
        OfsEnquiryResponse response = mapper.readEnquiryResponse(ofsResponse);

        assertTrue(response.isSuccess());
        assertFalse(response.isError());

        // Check columns exist
        assertEquals(2, response.getColumnCount());

        // Check no rows
        assertEquals(0, response.getRowCount());
    }

    @Test
    void testEnquiryDeserializationWithSpaceSeparatedValues() {
        // Test with space-separated quoted values (as shown in example)
        String ofsResponse = ",@ID::Code/DESCRIPTION::Description,\"USD\" \"US DOLLAR\",\"EUR\" \"EURO\",\"GBP\" \"BRITISH POUND\"";

        OfsObjectMapper mapper = new OfsObjectMapper();
        OfsEnquiryResponse response = mapper.readEnquiryResponse(ofsResponse);

        assertTrue(response.isSuccess());

        // Check columns
        assertEquals(2, response.getColumnCount());

        // Check rows
        assertEquals(3, response.getRowCount());

        assertEquals("USD", response.getCellValue(0, 0));
        assertEquals("US DOLLAR", response.getCellValue(0, 1));

        assertEquals("EUR", response.getCellValue(1, 0));
        assertEquals("EURO", response.getCellValue(1, 1));

        assertEquals("GBP", response.getCellValue(2, 0));
        assertEquals("BRITISH POUND", response.getCellValue(2, 1));
    }

    @Test
    void testEnquiryDeserializationWithMultipleColumns() {
        // Test with more columns
        String ofsResponse = ",ID::ID/NAME::Name/TYPE::Type/STATUS::Status," +
                "\"001\"\t\"Customer 1\"\t\"INDIVIDUAL\"\t\"ACTIVE\"," +
                "\"002\"\t\"Customer 2\"\t\"CORPORATE\"\t\"INACTIVE\"";

        OfsObjectMapper mapper = new OfsObjectMapper();
        OfsEnquiryResponse response = mapper.readEnquiryResponse(ofsResponse);

        assertTrue(response.isSuccess());

        // Check columns
        assertEquals(4, response.getColumnCount());
        assertEquals("ID", response.getColumns().get(0).getIdentifier());
        assertEquals("NAME", response.getColumns().get(1).getIdentifier());
        assertEquals("TYPE", response.getColumns().get(2).getIdentifier());
        assertEquals("STATUS", response.getColumns().get(3).getIdentifier());

        // Check rows
        assertEquals(2, response.getRowCount());

        // First row
        assertEquals("001", response.getCellValue(0, "ID"));
        assertEquals("Customer 1", response.getCellValue(0, "NAME"));
        assertEquals("INDIVIDUAL", response.getCellValue(0, "TYPE"));
        assertEquals("ACTIVE", response.getCellValue(0, "STATUS"));

        // Second row
        assertEquals("002", response.getCellValue(1, "ID"));
        assertEquals("Customer 2", response.getCellValue(1, "NAME"));
        assertEquals("CORPORATE", response.getCellValue(1, "TYPE"));
        assertEquals("INACTIVE", response.getCellValue(1, "STATUS"));
    }

    @Test
    void testEnquiryDeserializationWithSingleRow() {
        // Test with single result row
        String ofsResponse = ",@ID::ID/BALANCE::Balance,\"ACC123\"\t\"1000.00\"";

        OfsObjectMapper mapper = new OfsObjectMapper();
        OfsEnquiryResponse response = mapper.readEnquiryResponse(ofsResponse);

        assertTrue(response.isSuccess());
        assertEquals(2, response.getColumnCount());
        assertEquals(1, response.getRowCount());
        assertEquals("ACC123", response.getCellValue(0, 0));
        assertEquals("1000.00", response.getCellValue(0, 1));
    }

    @Test
    void testEnquiryDeserializationInvalidColumnIndex() {
        String ofsResponse = ",@ID::Key,\"VALUE1\"";

        OfsObjectMapper mapper = new OfsObjectMapper();
        OfsEnquiryResponse response = mapper.readEnquiryResponse(ofsResponse);

        // Test invalid indices
        assertNull(response.getCellValue(0, 5));  // Column index out of bounds
        assertNull(response.getCellValue(5, 0));  // Row index out of bounds
        assertNull(response.getCellValue(0, "NONEXISTENT"));  // Non-existent column
    }

    @Test
    void testEnquiryDeserializationGetColumnIndex() {
        String ofsResponse = ",@ID::Key/NAME::Name/VALUE::Value,\"1\"\t\"Test\"\t\"100\"";

        OfsObjectMapper mapper = new OfsObjectMapper();
        OfsEnquiryResponse response = mapper.readEnquiryResponse(ofsResponse);

        assertEquals(0, response.getColumnIndex("@ID"));
        assertEquals(1, response.getColumnIndex("NAME"));
        assertEquals(2, response.getColumnIndex("VALUE"));
        assertEquals(-1, response.getColumnIndex("NONEXISTENT"));
    }
}

