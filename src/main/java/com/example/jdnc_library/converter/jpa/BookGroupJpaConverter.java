package com.example.jdnc_library.converter.jpa;

import com.example.jdnc_library.domain.book.model.BookGroup;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class BookGroupJpaConverter implements AttributeConverter<BookGroup, String> {

    @Override
    public String convertToDatabaseColumn(BookGroup attribute) {
        if (attribute == null) return null;
        return attribute.name();
    }

    @Override
    public BookGroup convertToEntityAttribute(String dbData) {
        try {
            return BookGroup.valueOf(dbData);
        }catch (Exception e) {
            return null;
        }
    }
}
