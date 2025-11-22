package com.precisionlex;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OfsObjectMapper {

    // Transaction Request Serialization

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

        if (request.getRecordId() != null) {
            ofs.append(request.getRecordId());
        }
        ofs.append(",");

        if (request.getFields() != null && !request.getFields().isEmpty()) {
            ofs.append(serializeFields(request.getFields()));
        }

        return ofs.toString();
    }

    // Enquiry Request Serialization

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
            for (OfsEnquiryRequest.SelectionCriteria criteria : request.getSelectionCriteria()) {
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

    // Transaction Response Deserialization

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

    // Enquiry Response Deserialization

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

        if (current.length() > 0) {
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

            if (current.length() > 0) {
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
