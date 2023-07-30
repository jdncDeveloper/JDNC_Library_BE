package com.example.jdnc_library.feature.tutorial.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.jdnc_library.feature.tutorial.model.TutorialDetailDTO;
import com.example.jdnc_library.feature.tutorial.model.TutorialListDTO;
import com.example.jdnc_library.feature.tutorial.model.TutorialRequest;
import com.example.jdnc_library.feature.tutorial.service.TutorialService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(controllers = {TutorialController.class})
public class TutorialControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TutorialService tutorialService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser
    @DisplayName("튜토리얼 목록 조회가 정상적으로 조회 되어야 한다.")
    public void test1() throws Exception {
        //given
        List<TutorialListDTO> tutorialListDTOS = Arrays.asList(new TutorialListDTO(1L, "title1"),
            new TutorialListDTO(2L, "title2"));
        given(tutorialService.getTutorialList(any())).willReturn(tutorialListDTOS);

        //when
        ResultActions resultActions = mockMvc.perform(get("/tutorial"));

        //then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.data[0]").exists());
        resultActions.andExpect(jsonPath("$.data[1]").exists());
        resultActions.andExpect(jsonPath("$.data[2]").doesNotExist());
    }

    @Test
    @WithMockUser
    @DisplayName("튜토리얼 상세조회가 정상적으로 조회 되어야 한다.")
    public void test2() throws Exception {
        //given
        Long tutorialId = 1L;
        String title = "title1";
        String content = "content1";
        TutorialDetailDTO tutorialDTO = new TutorialDetailDTO(tutorialId, title, content);
        given(tutorialService.getTutorial(1L)).willReturn(tutorialDTO);

        //when
        ResultActions resultActions = mockMvc.perform(get("/tutorial/{tutorialId}", tutorialId));

        //then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.data.title").value(title));
        resultActions.andExpect(jsonPath("$.data.content").value(content));
    }

    @Test
    @WithMockUser
    @DisplayName("튜토리얼 저장이 성공적으로 되어야한다.")
    public void test3() throws Exception {
        //given
        TutorialRequest tutorialRequest = new TutorialRequest("title1", "content1");
        given(tutorialService.saveTutorial(any())).willReturn(1L);

        //when
        ResultActions resultActions = mockMvc.perform(post("/tutorial")
            .content(objectMapper.writeValueAsString(tutorialRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .with(csrf()) //시큐리티 설정이안되기 때문에 포함
        );

        //then
        resultActions.andExpect(status().isCreated());
        resultActions.andExpect(jsonPath("$.data").value(1L));
    }

    @Test
    @WithMockUser
    @DisplayName("튜토리얼 수정이 성공적으로 되어야한다.")
    public void test4() throws Exception {
        //given
        Long tutorialId = 1L;
        TutorialRequest tutorialRequest = new TutorialRequest("title1", "content1");

        //when
        ResultActions resultActions = mockMvc.perform(put("/tutorial/{tutorialId}", tutorialId)
            .content(objectMapper.writeValueAsString(tutorialRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .with(csrf()) //시큐리티 설정이안되기 때문에 포함
        );

        //then
        resultActions.andExpect(status().isNoContent());
        verify(tutorialService, times(1)).updateTutorial(tutorialId, tutorialRequest);
    }

    @Test
    @WithMockUser
    @DisplayName("튜토리얼 삭제가 성공적으로 되어야한다.")
    public void test5() throws Exception {
        //given
        Long tutorialId = 1L;

        //when
        ResultActions resultActions = mockMvc.perform(delete("/tutorial/{tutorialId}", tutorialId)
            .with(csrf()) //시큐리티 설정이안되기 때문에 포함
        );

        //then
        resultActions.andExpect(status().isNoContent());
        verify(tutorialService, times(1)).deleteTutorial(tutorialId);
    }
}
