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
}
