package com.precisionlex.util;

import com.precisionlex.OfsCsmRequest;
import com.precisionlex.OfsCsmResponse;
import com.precisionlex.OfsObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CsmHandlingTest {

    @Test
    void testCsmBookSerialization() {
        OfsCsmRequest request = new OfsCsmRequest();
        request.setOperation("BOOK");
        request.setOptions("");
        request.setUserId("SUSER1");
        request.setPassword("123456");
        request.setCompany("US0010001");
        request.setExtendedUserInformation(List.of("", "", "", ""));
        request.setProcessingRule("POSTINGRULE01");
        request.addEntry("1001,AA09357JSPG5,2000,USD,D,TRANSREF,403,,,RequestSourceDet,,,LocalTest,TransNarration,");
        request.addEntry("1003,AA09357B5863,100,USD,D,TRANSREF,51,,,RequestSourceDet,,,LocalTest,TransNarration,");
        request.addEntry("1004,02000000281,300,USD,C,TRANSREF,51,,,RequestSourceDet,,,LocalTest,TransNarration,");
        request.addEntry("1005,02000000286,300,USD,C,TRANSREF,51,,,RequestSourceDet,,,LocalTest,TransNarration,");

        OfsObjectMapper mapper = new OfsObjectMapper();
        String ofsRequest = mapper.writeValueAsString(request);

        String expectedResult = "CSM=BOOK,,SUSER1/123456/US0010001////,POSTINGRULE01,"
                + "1001,AA09357JSPG5,2000,USD,D,TRANSREF,403,,,RequestSourceDet,,,LocalTest,TransNarration,#"
                + "1003,AA09357B5863,100,USD,D,TRANSREF,51,,,RequestSourceDet,,,LocalTest,TransNarration,#"
                + "1004,02000000281,300,USD,C,TRANSREF,51,,,RequestSourceDet,,,LocalTest,TransNarration,#"
                + "1005,02000000286,300,USD,C,TRANSREF,51,,,RequestSourceDet,,,LocalTest,TransNarration,";

        assertEquals(expectedResult, ofsRequest);
    }

    @Test
    void testCsmBookSerializationWithOptionsAndCompactUserInformation() {
        OfsCsmRequest request = new OfsCsmRequest();
        request.setOperation("BOOK");
        request.setOptions("OFS/I/PROCESS");
        request.setUserId("SUSER1");
        request.setPassword("123456");
        request.setCompany("US0010001");
        request.setProcessingRule("POSTINGRULE01");
        request.addEntry("1001,AA09357JSPG5,2000,USD,D,TRANSREF,403,,,RequestSourceDet,,,LocalTest,TransNarration,");

        OfsObjectMapper mapper = new OfsObjectMapper();
        String ofsRequest = mapper.writeValueAsString(request);

        String expectedResult = "CSM=BOOK,OFS/I/PROCESS,SUSER1/123456/US0010001,POSTINGRULE01,"
                + "1001,AA09357JSPG5,2000,USD,D,TRANSREF,403,,,RequestSourceDet,,,LocalTest,TransNarration,";

        assertEquals(expectedResult, ofsRequest);
    }

    @Test
    void testCsmBookDeserialization() {
        String ofsResponse = "1, 2, 1/"
                + "ACSPLIT5-02-05-2020731144936702-2/"
                + "ACSPLIT5-02-05-2020731144936712-1_Insufficientfunds, "
                + "ACSPLIT5-02-05-2020981394764904-3_Insufficientfunds/"
                + "ACSPLIT5-02-05-2020731144936714-4_Restriction on Account";

        OfsObjectMapper mapper = new OfsObjectMapper();
        OfsCsmResponse response = mapper.readCsmResponse(ofsResponse);

        assertEquals(1, response.getSuccessfulEntriesCount());
        assertEquals(2, response.getSuspendedEntriesCount());
        assertEquals(1, response.getFailedEntriesCount());
        assertEquals(List.of("ACSPLIT5-02-05-2020731144936702-2"), response.getSuccessfulEntries());
        assertEquals(List.of(
                "ACSPLIT5-02-05-2020731144936712-1_Insufficientfunds",
                "ACSPLIT5-02-05-2020981394764904-3_Insufficientfunds"
        ), response.getSuspendedEntries());
        assertEquals(List.of(
                "ACSPLIT5-02-05-2020731144936714-4_Restriction on Account"
        ), response.getFailedEntries());
        assertFalse(response.isSuccess());
        assertTrue(response.isSuspended());
        assertTrue(response.isError());
    }
}
