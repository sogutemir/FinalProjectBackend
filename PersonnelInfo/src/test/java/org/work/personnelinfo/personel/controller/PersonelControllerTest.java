package org.work.personnelinfo.personel.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.work.personnelinfo.personel.dto.PersonelDTO;
import org.work.personnelinfo.personel.service.PersonelService;
import org.springframework.http.MediaType;
import java.util.Arrays;
import java.util.List;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "admin", roles = {"ADMIN"})
public class PersonelControllerTest {

    private static final Long TEST_ID = 1L;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonelService personelService;

    @Test
    public void testGetPersonelById() throws Exception {
        PersonelDTO personelDTO = new PersonelDTO();
        given(personelService.getPersonelById(TEST_ID)).willReturn(personelDTO);
        mockMvc.perform(get("/personel/{id}", TEST_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{}"));
    }

    @Test
    void testGetAllPersonel() throws Exception {

        PersonelDTO person1 = new PersonelDTO();
        PersonelDTO person2 = new PersonelDTO();

        List<PersonelDTO> allPersonel = Arrays.asList(person1, person2);

        given(personelService.getAllPersonel()).willReturn(allPersonel);

        mockMvc.perform(get("/personel/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[*]").isNotEmpty());

        assertThat(personelService.getAllPersonel()).isEqualTo(allPersonel);
    }

    @Test
    public void testAddPersonel() throws Exception {
        PersonelDTO mockPersonel = new PersonelDTO();
        when(personelService.addPersonel(any(PersonelDTO.class), any())).thenReturn(mockPersonel);
        mockMvc.perform(MockMvcRequestBuilders.multipart("/personel/admin/add")
                .file(new MockMultipartFile("file", "test.txt", "text/plain", "test data".getBytes()))
                .param("personelDTO", mockPersonel.toString())
        ).andExpect(status().isOk());
    }

    @Test
    void testUpdatePersonel() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "hello.txt", MediaType.TEXT_PLAIN_VALUE, "Hello, World!".getBytes());
        PersonelDTO personelDTO = new PersonelDTO();
        given(personelService.updatePersonel(anyLong(), any(PersonelDTO.class), any())).willReturn(personelDTO);
        mockMvc.perform(MockMvcRequestBuilders.multipart("/personel/update/1")
                        .file(file)
                        .flashAttr("personelDTO", personelDTO)
                        .with(request -> {request.setMethod("PUT"); return request;})
                )
                .andExpect(status().isOk());
    }

    @Test
    void testDeletePersonel() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/personel/admin/delete/{personelId}", TEST_ID))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

        verify(personelService, times(1)).deletePersonel(TEST_ID);
    }
}