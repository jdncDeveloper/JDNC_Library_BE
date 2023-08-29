package com.example.jdnc_library.converter.mvc;

import com.example.jdnc_library.domain.book.model.BookGroup;
import org.springframework.core.convert.converter.Converter;

public class BookGroupMvcConverter implements Converter<String, BookGroup> {

    @Override
    public BookGroup convert(String source) {
        try {
            return BookGroup.valueOf(source);
        }catch (Exception e) {
            return null;
        }
    }
}
