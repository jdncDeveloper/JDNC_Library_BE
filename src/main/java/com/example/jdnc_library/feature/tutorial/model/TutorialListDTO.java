package com.example.jdnc_library.feature.tutorial.model;

import com.example.jdnc_library.domain.tutorial.model.Tutorial;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TutorialListDTO {

    private Long id;

    private String title;

    public static TutorialListDTO of(Tutorial tutorial) {
        return new TutorialListDTO(
            tutorial.getId(),
            tutorial.getTitle()
        );
    }

}
