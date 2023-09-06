package com.example.jdnc_library.feature.book.DTO;

import com.example.jdnc_library.domain.book.model.CollectionInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookAvailableDTO {

    private Long bookNumber;

    private Boolean available;

    public BookAvailableDTO (CollectionInfo collectionInfo) {
        this.bookNumber = collectionInfo.getBookNumber();
        this.available = collectionInfo.isAvailable();
    }

}
