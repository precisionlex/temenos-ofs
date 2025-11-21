package com.precisionlex;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OfsObjectMapper {

    public String writeValueAsString(OfsRequest request) {

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

    public OfsResponse readValue(String ofsResponseString, Class<OfsResponse> clazz) {
        OfsResponse response = new OfsResponse();

        if (ofsResponseString == null || ofsResponseString.trim().isEmpty()) {
            return response;
        }

        int firstCommaIndex = ofsResponseString.indexOf(',');
        if (firstCommaIndex == -1) {
            parseResponseHeader(ofsResponseString, response);
            return response;
        }

        String header = ofsResponseString.substring(0, firstCommaIndex);
        String data = ofsResponseString.substring(firstCommaIndex + 1);

        parseResponseHeader(header, response);

        if (!data.trim().isEmpty()) {
            parseResponseFields(data, response);
        }

        return response;
    }

    private void parseResponseHeader(String header, OfsResponse response) {
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

    private void parseResponseFields(String data, OfsResponse response) {

        String[] fieldParts = data.split(",");

        for (String fieldPart : fieldParts) {
            if (fieldPart.trim().isEmpty()) {
                continue;
            }

            parseAndAddField(fieldPart.trim(), response);
        }
    }

    private void parseAndAddField(String fieldEntry, OfsResponse response) {

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
}
