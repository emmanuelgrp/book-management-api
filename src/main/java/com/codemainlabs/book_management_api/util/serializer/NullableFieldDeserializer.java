package com.codemainlabs.book_management_api.util.serializer;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;

public class NullableFieldDeserializer<T> extends JsonDeserializer<NullableField<T>> {

    private final Class<T> valueType;

    public NullableFieldDeserializer(Class<T> valueType) {
        this.valueType = valueType;
    }

    @Override
    public NullableField<T> deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        NullableField<T> result = new NullableField<>();
        JsonNode node = p.getCodec().readTree(p);
        // Si el campo está presente (aunque sea null) se asigna y se marca como presente
        if (node.isNull()) {
            result.setValue(null);
        } else {
            T value = p.getCodec().treeToValue(node, valueType);
            result.setValue(value);
        }
        return result;
    }
}
