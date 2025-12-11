package com.precisionlex;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Mapper for serializing and deserializing Temenos OFS (Open Financial Services) messages.
 * <p>
 * This class provides methods to convert between Java objects and OFS string format for both
 * transaction requests/responses and enquiry requests/responses. It handles the specific
 * formatting requirements of the Temenos T24 OFS protocol.
 * </p>
 *
 * @author PrecisionLex
 * @version 1.0.0
 */
public class OfsObjectMapper {

    /**
     * Serializes an OFS transaction request object into an OFS-formatted string.
     * <p>
     * The output format follows the pattern:
     * {@code APPLICATION,VERSION/FUNCTION/PROCESSING_FLAG,USER/PASSWORD/COMPANY,RECORD_ID,FIELDS}
     * </p>
     *
     * @param request the transaction request object to serialize
     * @return the OFS-formatted string representation of the request
     */
    public String writeValueAsString(OfsTransactionRequest request) {

        StringBuilder ofs = new StringBuilder();

        if (request.getApplication() != null) {
            ofs.append(request.getApplication());
        }
        ofs.append(",");

        if (request.getVersion() != null) {
            ofs.append(request.getVersion());
        }
        ofs.append("/");

        if (request.getFunction() != null) {
            ofs.append(request.getFunction());
        }
        ofs.append("/");

        if (request.getProcessingFlag() != null) {
            ofs.append(request.getProcessingFlag());
        }

        if (request.getAuthorisers() != null) {
            ofs.append("/");
            ofs.append("/");
            ofs.append(request.getAuthorisers());
        }

        ofs.append(",");

        // Only add user info section if at least one field is provided
        boolean hasUserInfo = request.getUserId() != null || request.getPassword() != null || request.getCompany() != null;

        if (hasUserInfo) {
            if (request.getUserId() != null) {
                ofs.append(request.getUserId());
            }
            ofs.append("/");

            if (request.getPassword() != null) {
                ofs.append(request.getPassword());
            }
            ofs.append("/");

            if (request.getCompany() != null) {
                ofs.append(request.getCompany());
            }
        }
        ofs.append(",");

        if (request.getRecordId() != null) {
            ofs.append(request.getRecordId());
        }
        ofs.append(",");

        if (request.getFields() != null && !request.getFields().isEmpty()) {
            ofs.append(serializeFields(request.getFields()));
        }

        return ofs.toString();
    }

    /**
     * Serializes an OFS enquiry request object into an OFS-formatted string.
     * <p>
     * The output format follows the pattern:
     * {@code OPERATION,OPTIONS,USER/PASSWORD/COMPANY,ENQUIRY_ID,SELECTION_CRITERIA}
     * </p>
     *
     * @param request the enquiry request object to serialize
     * @return the OFS-formatted string representation of the enquiry request
     */
    public String writeValueAsString(OfsEnquiryRequest request) {
        StringBuilder ofs = new StringBuilder();

        if (request.getOperation() != null) {
            ofs.append(request.getOperation());
        }
        ofs.append(",");

        if (request.getOptions() != null) {
            ofs.append(request.getOptions());
        }
        ofs.append(",");

        if (request.getUserId() != null) {
            ofs.append(request.getUserId());
        }
        ofs.append("/");

        if (request.getPassword() != null) {
            ofs.append(request.getPassword());
        }
        ofs.append("/");

        if (request.getCompany() != null) {
            ofs.append(request.getCompany());
        }
        ofs.append(",");

        if (request.getEnquiryId() != null) {
            ofs.append(request.getEnquiryId());
        }
        ofs.append(",");

        if (request.getSelectionCriteria() != null && !request.getSelectionCriteria().isEmpty()) {
            List<String> criteriaStrings = new ArrayList<>();
            for (OfsEnquirySelectionCriteria criteria : request.getSelectionCriteria()) {
                criteriaStrings.add(criteria.toString());
            }
            ofs.append(String.join(",", criteriaStrings));
        }

        return ofs.toString();
    }

    private String serializeFields(Map<String, List<OfsField>> fields) {
        List<String> serializedFields = new ArrayList<>();

        for (Map.Entry<String, List<OfsField>> entry : fields.entrySet()) {
            String fieldName = entry.getKey();
            List<OfsField> ofsFields = entry.getValue();

            for (OfsField ofsField : ofsFields) {
                List<String> multiValues = ofsField.getMultiValues();

                for (int i = 0; i < multiValues.size(); i++) {
                    String value = multiValues.get(i);

                    String serializedField = String.format("%s:%d:1=\"%s\"",
                        fieldName,
                        i + 1,
                        value != null ? value : ""
                    );

                    serializedFields.add(serializedField);
                }
            }
        }

        return String.join(",", serializedFields);
    }

    /**
     * Deserializes an OFS-formatted transaction response string into an OfsTransactionResponse object.
     * <p>
     * The expected format is: {@code RECORD_ID/TRANSACTION_REF/STATUS/ERROR_CODE,FIELDS}
     * where the header contains the response metadata and the optional fields section contains
     * the field values in the format {@code FIELD_NAME:MULTI_INDEX:SUB_INDEX="VALUE"}.
     * </p>
     *
     * @param ofsResponseString the OFS-formatted response string to deserialize
     * @return the deserialized OfsTransactionResponse object
     */
    public OfsTransactionResponse readTransactionResponse(String ofsResponseString) {
        OfsTransactionResponse response = new OfsTransactionResponse();

        if (ofsResponseString == null || ofsResponseString.trim().isEmpty()) {
            return response;
        }

        int firstCommaIndex = ofsResponseString.indexOf(',');
        if (firstCommaIndex == -1) {
            parseTransactionResponseHeader(ofsResponseString, response);
            return response;
        }

        String header = ofsResponseString.substring(0, firstCommaIndex);
        String data = ofsResponseString.substring(firstCommaIndex + 1);

        parseTransactionResponseHeader(header, response);

        if (!data.trim().isEmpty()) {
            parseTransactionResponseFields(data, response);
        }

        return response;
    }

    private void parseTransactionResponseHeader(String header, OfsTransactionResponse response) {
        String[] headerParts = header.split("/");

        if (headerParts.length > 0) {
            response.setRecordId(headerParts[0].trim());
        }

        if (headerParts.length > 1) {
            response.setTransactionRef(headerParts[1].trim());
        }

        if (headerParts.length > 2) {
            response.setStatus(headerParts[2].trim());
        }

        if (headerParts.length > 3) {
            response.setErrorCode(headerParts[3].trim());
        }
    }

    private void parseTransactionResponseFields(String data, OfsTransactionResponse response) {
        String[] fieldParts = data.split(",");

        for (String fieldPart : fieldParts) {
            if (fieldPart.trim().isEmpty()) {
                continue;
            }

            parseAndAddTransactionField(fieldPart.trim(), response);
        }
    }

    private void parseAndAddTransactionField(String fieldEntry, OfsTransactionResponse response) {
        int equalsIndex = fieldEntry.indexOf('=');
        if (equalsIndex == -1) {
            return;
        }

        String fieldNameWithIndices = fieldEntry.substring(0, equalsIndex);
        String value = fieldEntry.substring(equalsIndex + 1);

        if (value.startsWith("\"") && value.endsWith("\"")) {
            value = value.substring(1, value.length() - 1);
        }

        String[] parts = fieldNameWithIndices.split(":");
        if (parts.length < 3) {
            return;
        }

        String fieldName = parts[0];
        int multiIndex = Integer.parseInt(parts[1]);

        List<OfsField> ofsFields = response.getFields().get(fieldName);
        if (ofsFields == null) {
            ofsFields = new ArrayList<>();
            response.getFields().put(fieldName, ofsFields);
        }

        OfsField ofsField;
        if (ofsFields.isEmpty()) {
            ofsField = new OfsField();
            ofsFields.add(ofsField);
        } else {
            ofsField = ofsFields.get(0);
        }

        List<String> multiValues = ofsField.getMultiValues();

        while (multiValues.size() < multiIndex) {
            multiValues.add("");
        }

        multiValues.set(multiIndex - 1, value);

        ofsField.setMultiValues(multiValues);
    }

    /**
     * Deserializes an OFS-formatted enquiry response string into an OfsEnquiryResponse object.
     * <p>
     * Parses the enquiry response which consists of three main sections separated by commas:
     * header captions, column definitions, and row data. Handles error responses in the format
     * {@code ENQUIRY_NAME//-1/,ERROR_MESSAGE}.
     * </p>
     *
     * @param ofsResponseString the OFS-formatted enquiry response string to deserialize
     * @return the deserialized OfsEnquiryResponse object
     */
    public OfsEnquiryResponse readEnquiryResponse(String ofsResponseString) {
        OfsEnquiryResponse response = new OfsEnquiryResponse();

        if (ofsResponseString == null || ofsResponseString.trim().isEmpty()) {
            return response;
        }

        if (ofsResponseString.contains("//-1/,")) {
            parseEnquiryError(ofsResponseString, response);
            return response;
        }

        String[] mainParts = splitEnquiryResponse(ofsResponseString);

        if (mainParts.length >= 1) {
            parseHeaderCaptions(mainParts[0], response);
        }

        if (mainParts.length >= 2) {
            parseColumnDetails(mainParts[1], response);
        }

        if (mainParts.length >= 3) {
            parseRowData(mainParts, response);
        }

        return response;
    }

    private String[] splitEnquiryResponse(String response) {
        List<String> parts = new ArrayList<>();
        int commaCount = 0;
        int start = 0;

        for (int i = 0; i < response.length(); i++) {
            if (response.charAt(i) == ',') {
                if (commaCount < 2) {
                    parts.add(response.substring(start, i));
                    start = i + 1;
                    commaCount++;
                }
            }
        }

        if (start < response.length()) {
            parts.add(response.substring(start));
        }

        return parts.toArray(new String[0]);
    }

    private void parseEnquiryError(String response, OfsEnquiryResponse enquiryResponse) {
        enquiryResponse.setError(true);

        int errorDelimiterIndex = response.indexOf("//-1/,");
        if (errorDelimiterIndex > 0) {
            String enquiryName = response.substring(0, errorDelimiterIndex);
            enquiryResponse.setErrorEnquiryName(enquiryName);

            String errorMessage = response.substring(errorDelimiterIndex + 6);
            enquiryResponse.setErrorMessage(errorMessage);
        }
    }

    private void parseHeaderCaptions(String headerSection, OfsEnquiryResponse response) {
        if (headerSection == null || headerSection.trim().isEmpty()) {
            return;
        }

        String[] captions = headerSection.split("/");
        for (String caption : captions) {
            if (caption.trim().isEmpty()) {
                continue;
            }

            int equalsIndex = caption.indexOf('=');
            if (equalsIndex > 0) {
                String identifier = caption.substring(0, equalsIndex).trim();
                String text = caption.substring(equalsIndex + 1).trim();
                response.addHeaderCaption(identifier, text);
            }
        }
    }

    private void parseColumnDetails(String columnSection, OfsEnquiryResponse response) {
        if (columnSection == null || columnSection.trim().isEmpty()) {
            return;
        }

        String[] columnDefs = columnSection.split("/");
        for (String columnDef : columnDefs) {
            if (columnDef.trim().isEmpty()) {
                continue;
            }

            String[] parts = columnDef.split("::", 2);
            if (parts.length >= 1) {
                String identifier = parts[0].trim();
                String formatType = "";
                String label = "";

                if (parts.length == 2) {
                    label = parts[1].trim();
                }

                response.addColumn(identifier, formatType, label);
            }
        }
    }

    private void parseRowData(String[] mainParts, OfsEnquiryResponse response) {
        if (mainParts.length < 3) {
            return;
        }

        StringBuilder allRowData = new StringBuilder();
        for (int i = 2; i < mainParts.length; i++) {
            if (i > 2) {
                allRowData.append(",");
            }
            allRowData.append(mainParts[i]);
        }

        String rowDataStr = allRowData.toString();
        if (rowDataStr.trim().isEmpty()) {
            return;
        }

        List<String> rowStrings = splitRowsByComma(rowDataStr);

        for (String rowStr : rowStrings) {
            if (rowStr.trim().isEmpty()) {
                continue;
            }

            List<String> rowValues = parseRowValues(rowStr);
            if (!rowValues.isEmpty()) {
                response.addRow(rowValues);
            }
        }
    }

    private List<String> splitRowsByComma(String data) {
        List<String> rows = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuote = false;

        for (int i = 0; i < data.length(); i++) {
            char c = data.charAt(i);

            if (c == '"') {
                inQuote = !inQuote;
                current.append(c);
            } else if (c == ',' && !inQuote) {
                rows.add(current.toString());
                current = new StringBuilder();
            } else {
                current.append(c);
            }
        }

        if (!current.isEmpty()) {
            rows.add(current.toString());
        }

        return rows;
    }

    private List<String> parseRowValues(String rowStr) {
        List<String> values = new ArrayList<>();

        if (rowStr.contains("\t")) {
            String[] parts = rowStr.split("\t");
            for (String part : parts) {
                values.add(unquoteValue(part.trim()));
            }
        } else {
            StringBuilder current = new StringBuilder();
            boolean inQuote = false;

            for (int i = 0; i < rowStr.length(); i++) {
                char c = rowStr.charAt(i);

                if (c == '"') {
                    if (inQuote) {
                        values.add(current.toString());
                        current = new StringBuilder();
                        inQuote = false;
                    } else {
                        inQuote = true;
                    }
                } else if (inQuote) {
                    current.append(c);
                }
            }

            if (!current.isEmpty()) {
                values.add(current.toString());
            }
        }

        return values;
    }

    private String unquoteValue(String value) {
        if (value.startsWith("\"") && value.endsWith("\"") && value.length() >= 2) {
            return value.substring(1, value.length() - 1);
        }
        return value;
    }
}
