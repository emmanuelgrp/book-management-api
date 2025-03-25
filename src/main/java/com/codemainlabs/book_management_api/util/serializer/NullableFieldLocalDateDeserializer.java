package com.codemainlabs.book_management_api.util.serializer;

import java.time.LocalDate;

public class NullableFieldLocalDateDeserializer extends NullableFieldDeserializer<LocalDate> {
    public NullableFieldLocalDateDeserializer() {
        super(LocalDate.class);
    }
}


