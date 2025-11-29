package com.precisionlex;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class OfsEnquiryResponse {

    private Map<String, String> headerCaptions;
    private List<ColumnDefinition> columns;
    private List<List<String>> rows;
    private boolean isError;
    private String errorEnquiryName;
    private String errorMessage;

    public OfsEnquiryResponse() {
        this.headerCaptions = new LinkedHashMap<>();
        this.columns = new ArrayList<>();
        this.rows = new ArrayList<>();
        this.isError = false;
    }

    public Map<String, String> getHeaderCaptions() {
        return headerCaptions;
    }

    public void setHeaderCaptions(Map<String, String> headerCaptions) {
        this.headerCaptions = headerCaptions;
    }

    public void addHeaderCaption(String identifier, String text) {
        this.headerCaptions.put(identifier, text);
    }

    public List<ColumnDefinition> getColumns() {
        return columns;
    }

    public void setColumns(List<ColumnDefinition> columns) {
        this.columns = columns;
    }

    public void addColumn(ColumnDefinition column) {
        this.columns.add(column);
    }

    public void addColumn(String identifier, String formatType, String label) {
        this.columns.add(new ColumnDefinition(identifier, formatType, label));
    }

    public List<List<String>> getRows() {
        return rows;
    }

    public void setRows(List<List<String>> rows) {
        this.rows = rows;
    }

    public void addRow(List<String> row) {
        this.rows.add(row);
    }

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }

    public String getErrorEnquiryName() {
        return errorEnquiryName;
    }

    public void setErrorEnquiryName(String errorEnquiryName) {
        this.errorEnquiryName = errorEnquiryName;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean isSuccess() {
        return !isError;
    }

    public String getCellValue(int rowIndex, int columnIndex) {
        if (rowIndex < 0 || rowIndex >= rows.size()) {
            return null;
        }
        List<String> row = rows.get(rowIndex);
        if (columnIndex < 0 || columnIndex >= row.size()) {
            return null;
        }
        return row.get(columnIndex);
    }

    public String getCellValue(int rowIndex, String columnIdentifier) {
        int columnIndex = getColumnIndex(columnIdentifier);
        if (columnIndex == -1) {
            return null;
        }
        return getCellValue(rowIndex, columnIndex);
    }

    public int getColumnIndex(String identifier) {
        for (int i = 0; i < columns.size(); i++) {
            if (columns.get(i).getIdentifier().equals(identifier)) {
                return i;
            }
        }
        return -1;
    }

    public int getRowCount() {
        return rows.size();
    }

    public int getColumnCount() {
        return columns.size();
    }

    @Override
    public String toString() {
        if (isError) {
            return "OfsEnquiryResponse{" +
                    "isError=true" +
                    ", errorEnquiryName='" + errorEnquiryName + '\'' +
                    ", errorMessage='" + errorMessage + '\'' +
                    '}';
        }
        return "OfsEnquiryResponse{" +
                "headerCaptionCount=" + headerCaptions.size() +
                ", columnCount=" + columns.size() +
                ", rowCount=" + rows.size() +
                '}';
    }

    public static class ColumnDefinition {
        private String identifier;
        private String formatType;
        private String label;

        public ColumnDefinition() {}

        public ColumnDefinition(String identifier, String formatType, String label) {
            this.identifier = identifier;
            this.formatType = formatType;
            this.label = label;
        }

        public String getIdentifier() {
            return identifier;
        }

        public void setIdentifier(String identifier) {
            this.identifier = identifier;
        }

        public String getFormatType() {
            return formatType;
        }

        public void setFormatType(String formatType) {
            this.formatType = formatType;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        @Override
        public String toString() {
            return identifier + "::" + label + " (" + formatType + ")";
        }
    }
}

