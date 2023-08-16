package com.example.jdnc_library.feature.convert.model;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConvertTestDTO {
    private long id;
    private int serialNum;
    private String bookName;
    private int cardinalNum;
    private String userName;
    private LocalDateTime rentalDateTime;

}
