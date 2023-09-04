package com.example.jdnc_library.feature.book.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCollectionDTO {

    private Long bookId;

    private Long bookNumber;
}
