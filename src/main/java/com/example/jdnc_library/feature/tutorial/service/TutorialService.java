package com.example.jdnc_library.feature.tutorial.service;

import com.example.jdnc_library.domain.tutorial.model.Tutorial;
import com.example.jdnc_library.domain.tutorial.repository.TutorialRepository;
import com.example.jdnc_library.exception.clienterror._400.EntityNotFoundException;
import com.example.jdnc_library.feature.tutorial.model.TutorialDetailDTO;
import com.example.jdnc_library.feature.tutorial.model.TutorialListDTO;
import com.example.jdnc_library.feature.tutorial.model.TutorialRequest;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TutorialService {

    private final TutorialRepository tutorialRepository;

    public List<TutorialListDTO> getTutorialList(Pageable pageable) {
        List<Tutorial> tutorials = tutorialRepository.findAll(pageable).getContent();
        return tutorials.stream().map(TutorialListDTO::of).collect(Collectors.toList());
    }

    public TutorialDetailDTO getTutorial(Long tutorialId) {
        Tutorial tutorial = tutorialRepository.findById(tutorialId).orElseThrow(() -> new EntityNotFoundException(tutorialId, Tutorial.class));
        return TutorialDetailDTO.of(tutorial);
    }

    public Long saveTutorial(TutorialRequest request) {
        Tutorial tutorial = request.toEntity();

        return tutorialRepository.save(tutorial).getId();
    }

    public void updateTutorial(Long tutorialId, TutorialRequest request) {
        Tutorial tutorial = tutorialRepository.findById(tutorialId).orElseThrow(() -> new EntityNotFoundException(tutorialId, Tutorial.class));

        tutorial.update(request.getTitle(), request.getContent());
    }

    public void deleteTutorial(Long tutorialId) {
        Tutorial tutorial = tutorialRepository.findById(tutorialId).orElseThrow(() -> new EntityNotFoundException(tutorialId, Tutorial.class));

        tutorialRepository.delete(tutorial);
    }
}
