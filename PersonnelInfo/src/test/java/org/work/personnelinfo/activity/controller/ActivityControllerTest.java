package org.work.personnelinfo.activity.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.work.personnelinfo.activity.dto.ActivityDTO;
import org.work.personnelinfo.activity.service.ActivityService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ActivityController.class)
class ActivityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ActivityService activityService;

    @Test
    void testAddActivity() throws Exception {
        ActivityDTO dto = new ActivityDTO();
        dto.setPersonelId(1L);

        MockMultipartFile file = new MockMultipartFile("file", "filename.jpeg", "image/jpeg", "Hello, World!".getBytes());

        Mockito.when(activityService.addActivity(Mockito.any(), Mockito.any())).thenReturn(dto);

        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders.multipart("/activity/add")
                .file(file)
                .param("personelId", "1")
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated());

        String response = resultActions.andReturn().getResponse().getContentAsString();
        assertThat(response).contains("1");
    }
}