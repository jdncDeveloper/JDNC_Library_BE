package com.example.jdnc_library.feature.tutorial.model;


import com.example.jdnc_library.domain.tutorial.model.Tutorial;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TutorialDetailDTO {

    private Long id;

    private String title;

    private String content;

    public static TutorialDetailDTO of (Tutorial tutorial) {
        return new TutorialDetailDTO(
            tutorial.getId(),
            tutorial.getTitle(),
            tutorial.getContent()
        );
    }
}
