package com.example.jdnc_library.feature.manager.model;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatusNResultDTO {
    private boolean check;

    private String message;

    private List<?> list;
}
