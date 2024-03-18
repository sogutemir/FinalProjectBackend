package org.work.personnelinfo.personel.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.work.personnelinfo.personel.dto.PersonelDTO;
import org.work.personnelinfo.personel.mapper.PersonelMapper;
import org.work.personnelinfo.personel.model.PersonelEntity;
import org.work.personnelinfo.personel.repository.PersonelRepository;
import org.work.personnelinfo.resourceFile.service.ResourceFileService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PersonelServiceTest {

    @Mock
    private PersonelRepository personelRepository;
    @Mock
    private ResourceFileService resourceFileService;
    @Mock
    private PersonelMapper personelMapper;
    @Mock
    private MultipartFile multipartFile;

    @InjectMocks
    private PersonelService personelService;

    @Test
    public void getPersonelByIdTest() {

        Long id = 1L;
        PersonelEntity personelEntity = new PersonelEntity();
        PersonelDTO personelDTO = new PersonelDTO();

        when(personelRepository.findById(any(Long.class))).thenReturn(Optional.of(personelEntity));
        when(personelMapper.modelToDTO(any(PersonelEntity.class))).thenReturn(personelDTO);

        personelService.getPersonelById(id);

        verify(personelRepository, times(1)).findById(id);
        verify(personelMapper, times(1)).modelToDTO(personelEntity);
    }

    @Test
    public void getNonExistingPersonelByIdTest() {

        Long id = 1L;

        when(personelRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                personelService.getPersonelById(id));
        verify(personelRepository, times(1)).findById(id);
    }

    @Test
    public void getAllPersonelTest() {

        List<PersonelEntity> personelEntityList = List.of(new PersonelEntity());
        List<PersonelDTO> personelDTOList = List.of(new PersonelDTO());

        when(personelRepository.findAll()).thenReturn(personelEntityList);
        when(personelMapper.modelToDTO(any(PersonelEntity.class))).thenReturn(personelDTOList.get(0));

        personelService.getAllPersonel();

        verify(personelRepository, times(1)).findAll();
    }

    @Test
    public void addPersonelTest() throws IOException {

        PersonelDTO personelDTO = new PersonelDTO();
        PersonelEntity personelEntity = new PersonelEntity();
        when(personelMapper.dtoToModel(any(PersonelDTO.class))).thenReturn(personelEntity);
        when(personelRepository.save(any(PersonelEntity.class))).thenReturn(personelEntity);

        personelService.addPersonel(personelDTO, multipartFile);

        verify(personelMapper, times(1)).dtoToModel(personelDTO);
        verify(personelRepository, times(1)).save(personelEntity);
        verify(resourceFileService, times(1)).saveFile(multipartFile, personelEntity);
    }

    @Test
    public void updatePersonelTest() throws IOException {

        Long id = 1L;
        PersonelDTO personelDTO = new PersonelDTO();
        PersonelEntity personelEntity = new PersonelEntity();
        when(personelRepository.findById(any(Long.class))).thenReturn(Optional.of(personelEntity));

        personelService.updatePersonel(id, personelDTO, multipartFile);

        verify(personelMapper, times(1)).updateModel(personelDTO, personelEntity);
        verify(resourceFileService, times(1)).saveFile(multipartFile, personelEntity);
    }

    @Test
    public void deletePersonelTest() throws IOException {

        Long id = 1L;
        PersonelEntity personelEntity = new PersonelEntity();
        when(personelRepository.findById(any(Long.class))).thenReturn(Optional.of(personelEntity));

        personelService.deletePersonel(id);

        verify(personelRepository, times(1)).delete(personelEntity);
    }
}