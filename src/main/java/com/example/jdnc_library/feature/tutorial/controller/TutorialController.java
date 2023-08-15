package com.example.jdnc_library.feature.tutorial.controller;

import com.example.jdnc_library.domain.ResponseData;
import com.example.jdnc_library.feature.tutorial.model.TutorialDetailDTO;
import com.example.jdnc_library.feature.tutorial.model.TutorialListDTO;
import com.example.jdnc_library.feature.tutorial.model.TutorialRequest;
import com.example.jdnc_library.feature.tutorial.service.TutorialService;
import java.util.List;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tutorial")
public class TutorialController {

    private final TutorialService tutorialService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    private ResponseData<List<TutorialListDTO>> getTutorialList(
            @PageableDefault Pageable pageable,
            @RequestParam(value = "title", required = false) String title

    ) { //페이져블 패키지가 java.awt.print (X) org.springframework.data.domain (O)
        return new ResponseData<>(tutorialService.getTutorialList(pageable));
    }

    @GetMapping("{tutorialId}")
    @ResponseStatus(HttpStatus.OK)
    private ResponseData<TutorialDetailDTO> getTutorial(@PathVariable Long tutorialId) {
        return new ResponseData<>(tutorialService.getTutorial(tutorialId));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    private ResponseData<Long> saveTutorial(@RequestBody TutorialRequest request) {
        return new ResponseData<>(tutorialService.saveTutorial(request));
    }

    @PutMapping("{tutorialId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    private void updateTutorial (@PathVariable Long tutorialId, @RequestBody TutorialRequest tutorialRequest) {
        tutorialService.updateTutorial(tutorialId, tutorialRequest);
    }

    @DeleteMapping("{tutorialId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    private void deleteTutorial (@PathVariable Long tutorialId) {
        tutorialService.deleteTutorial(tutorialId);
    }
}
