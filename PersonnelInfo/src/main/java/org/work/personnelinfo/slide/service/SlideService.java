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

    private static final String NULL_ID_ERROR = "Id cannot be null";
    private static final String NULL_SLIDE_DTO_ERROR = "slideDTO cannot be null";
    private static final String SLIDE_NOT_FOUND_ERROR = "Slide not found with id: ";

    @Transactional(readOnly = true)
    public List<SlideDTO> getAllSlides() {
        return slideRepository.findAll()
                .stream()
                .map(slideMapper::modelToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SlideDTO getSlideById(Long id) {
        validateId(id);
        return slideRepository.findById(id)
                .map(slideMapper::modelToDTO)
                .orElseThrow(() -> new IllegalArgumentException(SLIDE_NOT_FOUND_ERROR + id));
    }

    @Transactional
    public SlideDTO addSlide(MultipartFile file, SlideDTO slideDTO) throws IOException {
        validateSlideDTO(slideDTO);
        SlideEntity slideEntity = slideMapper.dtoToModel(slideDTO);
        slideEntity = slideRepository.save(slideEntity);
        if (file != null) {
            resourceFileService.saveFile(file, slideEntity);
        }
        return slideMapper.modelToDTO(slideEntity);
    }

    @Transactional
    public SlideDTO updateSlide(Long slideId, SlideDTO slideDTO, MultipartFile file) throws IOException {
        validateSlideIdAndDTO(slideId, slideDTO);
        SlideEntity existingSlideEntity = slideRepository.findById(slideId)
                .orElseThrow(() -> new EntityNotFoundException(SLIDE_NOT_FOUND_ERROR + slideId));
        slideMapper.updateModel(slideDTO, existingSlideEntity);
        if (file != null && !file.isEmpty()) {
            if (existingSlideEntity.getResourceFile() != null) {
                resourceFileService.deleteFile(existingSlideEntity.getResourceFile().getId());
            }
            resourceFileService.saveFile(file, existingSlideEntity);
        }
        SlideEntity updatedSlideEntity = slideRepository.save(existingSlideEntity);
        return slideMapper.modelToDTO(updatedSlideEntity);
    }

    @Transactional
    public void deleteSlide(Long slideId) throws FileNotFoundException {
        validateId(slideId);
        SlideEntity slideEntity = slideRepository.findById(slideId)
                .orElseThrow(() -> new EntityNotFoundException(SLIDE_NOT_FOUND_ERROR + slideId));
        if (slideEntity.getResourceFile() != null) {
            resourceFileService.deleteFile(slideEntity.getResourceFile().getId());
        }
        slideRepository.delete(slideEntity);
    }

    private void validateId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException(NULL_ID_ERROR);
        }
    }

    private void validateSlideDTO(SlideDTO slideDTO) {
        if (slideDTO == null) {
            throw new IllegalArgumentException(NULL_SLIDE_DTO_ERROR);
        }
    }

    private void validateSlideIdAndDTO(Long slideId, SlideDTO slideDTO) {
        if (slideId == null || slideDTO == null) {
            throw new IllegalArgumentException("SlideId or SlideDTO cannot be null");
        }
    }
}
