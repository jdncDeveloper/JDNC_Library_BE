package com.example.jdnc_library.feature.convert.DTO;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConvertRangeDTO {
    private LocalDate start;
    private LocalDate end;
}
