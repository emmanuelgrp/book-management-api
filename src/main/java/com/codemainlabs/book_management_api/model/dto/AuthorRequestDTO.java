package com.codemainlabs.book_management_api.model.dto;

import com.codemainlabs.book_management_api.util.serializer.NullableField;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.JsonNode;
import java.time.LocalDate;
import java.util.function.Function;

public record AuthorRequestDTO(
        Long authorID,
        String firstName,
        String lastName,
        LocalDate birthDate,
        NullableField<LocalDate> deathDate,
        String biography,
        String nationality,
        String city
) {
    @JsonCreator
    public static AuthorRequestDTO fromJson(JsonNode node) {
        Long authorID = getLong(node, "authorID");
        String firstName = getString(node, "firstName");
        String lastName = getString(node, "lastName");
        LocalDate birthDate = getLocalDate(node, "birthDate");
        NullableField<LocalDate> deathDate = getNullableField(node, "deathDate", LocalDate::parse);
        String biography = getString(node, "biography");
        String nationality = getString(node, "nationality");
        String city = getString(node, "city");

        return new AuthorRequestDTO(authorID, firstName, lastName, birthDate, deathDate, biography, nationality, city);
    }

    private static String getString(JsonNode node, String fieldName) {
        return node.has(fieldName) && !node.get(fieldName).isNull()
                ? node.get(fieldName).asText()
                : null;
    }

    private static Long getLong(JsonNode node, String fieldName) {
        return node.has(fieldName) && !node.get(fieldName).isNull()
                ? node.get(fieldName).asLong()
                : null;
    }

    private static LocalDate getLocalDate(JsonNode node, String fieldName) {
        return node.has(fieldName) && !node.get(fieldName).isNull()
                ? LocalDate.parse(node.get(fieldName).asText())
                : null;
    }

    private static <T> NullableField<T> getNullableField(JsonNode node, String fieldName, Function<String, T> parser) {
        NullableField<T> field = new NullableField<>();
        if (node.has(fieldName)) {
            field.setValue(node.get(fieldName).isNull() ? null : parser.apply(node.get(fieldName).asText()));
        }
        return field;
    }
}
