package com.codemainlabs.book_management_api.util.serializer;

public class NullableField<T> {
    
    private boolean present;
    private T value;

    public NullableField() {
        // Por defecto, el campo no fue enviado
        this.present = false;
    }

    public boolean isPresent() {
        return present;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
        this.present = true; // Se marca como presente al asignar cualquier valor (incluso null)
    }
}
