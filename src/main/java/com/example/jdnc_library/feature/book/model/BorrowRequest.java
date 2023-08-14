package com.example.jdnc_library.feature.book.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BorrowRequest {
    private Long borrowId;

    private Long bookNumber;



}
