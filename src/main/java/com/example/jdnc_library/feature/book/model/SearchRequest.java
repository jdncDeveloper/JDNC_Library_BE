package com.example.jdnc_library.feature.book.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchRequest {

    private String title;

    private Long bookNumber;

    private int year;

    private int month;

}
