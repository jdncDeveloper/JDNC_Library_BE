package com.example.jdnc_library.feature.tutorial.model;

import com.example.jdnc_library.domain.tutorial.model.Tutorial;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TutorialRequest {

    private String title;

    private String content;

    public Tutorial toEntity () {
        return new Tutorial(
            null,
            title,
            content
        );
    }
}
