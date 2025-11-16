package com.precisionlex.utils;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.util.Map;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class GeneralOfsParser {

    private GeneralOfsParser() {
        // prevent instantiation
    }

    public static <T> Optional<T> parseFirstRow(String data, Class<T> type) {
        Optional<List<T>> optionalList = parse(data, type);

        if (optionalList.isPresent() && !optionalList.get().isEmpty()) {
            return Optional.of(optionalList.get().get(0));
        } else {
            return Optional.empty();
        }
    }

    public static <T> Optional<List<T>> parse(String data, Class<T> type) {
        try {
            if (data.contains("/-1/")) {
                return Optional.empty();
            }

            data = preprocessData(data);
            CsvMapper csvMapper = new CsvMapper();
            CsvSchema schema = CsvSchema.emptySchema().withHeader().withColumnSeparator(',');
            MappingIterator<Map<String, String>> mapIterator =
                    csvMapper.readerFor(Map.class).with(schema).readValues(data);

            ObjectMapper jsonMapper = new ObjectMapper();
            List<T> list = new ArrayList<>();
            while (mapIterator.hasNext()) {
                Map<String, String> row = mapIterator.next();
                T obj = jsonMapper.convertValue(row, type);
                list.add(obj);
            }

            return Optional.ofNullable(list.isEmpty() ? null : list);
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    private static String preprocessData(String data) {
        if (data.startsWith(",")) {
            data = data.substring(1);
        }

        // remove trailing 4 columns
        data = data.replaceAll("(?:,[^,]*){4}$", "");
        String[] lines = data.split(",");

        // process header
        lines[0] = Arrays.stream(lines[0].split("/"))
                .map(headerPart -> headerPart.split("::")[0].trim())
                .collect(Collectors.joining(","));

        // process rows
        for (int i = 1; i < lines.length; i++) {
            lines[i] = Arrays.stream(lines[i].split("\t"))
                    .map(field -> field.replaceAll("^\"\\s+", "\"")
                            .replaceAll("\\s+\"$", "\""))
                    .collect(Collectors.joining(","));
        }

        return String.join("\n", lines).trim();
    }

    public static String getFtNumberFromPaymentOfs(String ofsRequest) {
        String regex = "TransactionReferenceNumber:1:1=(BNK\\w*)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(ofsRequest);

        if (matcher.find() && matcher.group().length() > 1) {
            return matcher.group(1);
        }
        return "";
    }
}
