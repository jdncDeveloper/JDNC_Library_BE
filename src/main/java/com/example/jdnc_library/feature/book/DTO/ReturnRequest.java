package com.example.jdnc_library.feature.book.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReturnRequest {

    private Long bookNumber;

    private Integer floor;
}
