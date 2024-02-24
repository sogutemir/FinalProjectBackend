package org.work.personnelinfo.slide.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.work.personnelinfo.resourceFile.service.ResourceFileService;
import org.work.personnelinfo.slide.dto.SlideDTO;
import org.work.personnelinfo.slide.mapper.SlideMapper;
import org.work.personnelinfo.slide.model.SlideEntity;
import org.work.personnelinfo.slide.repository.SlideRepository;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SlideService {

    private final SlideRepository slideRepository;
    private final SlideMapper slideMapper;
    private final ResourceFileService resourceFileService;

    @Transactional(readOnly = true)
    public List<SlideDTO> getAllSlides(){
        return slideRepository.findAll()
                .stream()
                .map(slideMapper::modelToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SlideDTO getSlideById(Long id){
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        return slideRepository.findById(id)
                .map(slideMapper::modelToDTO)
                .orElseThrow(() -> new IllegalArgumentException("Activity not found with id: " + id));
    }

    @Transactional
    public SlideDTO addSlide(MultipartFile file ,SlideDTO slideDTO) throws IOException {

        if (slideDTO == null ) {
            throw new IllegalArgumentException("slideDTO cannot be null");
        }

        SlideEntity slideEntity = slideMapper.dtoToModel(slideDTO);

        slideEntity = slideRepository.save(slideEntity);

        if (file != null){
            resourceFileService.uploadFile(file, slideEntity);
        }

        return slideMapper.modelToDTO(slideEntity);
    }

    @Transactional
    public SlideDTO updateSlide(Long slideId, SlideDTO slideDTO, MultipartFile file ) throws IOException {
        if (slideDTO == null || slideDTO == null) {
            throw new IllegalArgumentException("SlideId or SlideDTO cannot be null");
        }

        SlideEntity existingSlideEntity = slideRepository.findById(slideId)
                .orElseThrow(() -> new EntityNotFoundException("Slide not found with id: " + slideId));

        slideMapper.updateModel(slideDTO, existingSlideEntity);

        if (file != null && !file.isEmpty()) {
            if (existingSlideEntity.getResourceFile() != null) {
                resourceFileService.deleteFile(existingSlideEntity.getResourceFile().getId());
            }
            resourceFileService.uploadFile(file, existingSlideEntity);
        }

        SlideEntity updatedSlideEntity = slideRepository.save(existingSlideEntity);

        return slideMapper.modelToDTO(updatedSlideEntity);
    }

    @Transactional
    public void deleteSlide(Long slideId)throws FileNotFoundException {
        if (slideId == null) {
            throw new IllegalArgumentException("slideId cannot be null");
        }

        SlideEntity slideEntity = slideRepository.findById(slideId)
                .orElseThrow(() -> new EntityNotFoundException("Slide not found with id: " + slideId));

        if(slideEntity.getResourceFile() != null) {
            resourceFileService.deleteFile(slideEntity.getResourceFile().getId());
        }

        slideRepository.delete(slideEntity);
    }
}
