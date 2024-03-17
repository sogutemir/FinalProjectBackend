package org.work.personnelinfo.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.work.personnelinfo.personel.model.PersonelEntity;
import org.work.personnelinfo.personel.repository.PersonelRepository;
import org.work.personnelinfo.project.dto.ProjectDTO;
import org.work.personnelinfo.project.dto.ProjectUpdateRequestDTO;
import org.work.personnelinfo.project.mapper.ProjectMapper;
import org.work.personnelinfo.project.model.ProjectEntity;
import org.work.personnelinfo.project.repository.ProjectRepository;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final PersonelRepository personelRepository;

    private void validateInput(Object... inputs) {
        for (Object input : inputs) {
            if (input == null) {
                throw new IllegalArgumentException("Input cannot be null");
            }
        }
    }

    @Transactional(readOnly = true)
    public ProjectDTO getProjectById(Long id) {
        validateInput(id);
        return projectRepository.findById(id)
                .map(projectMapper::modelToDTO)
                .orElseThrow(() -> new IllegalArgumentException("Project not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<ProjectDTO> getProjectsByPersonelId(Long personelId) {
        validateInput(personelId);
        return projectRepository.findByPersonelId(personelId)
                .stream()
                .map(projectMapper::modelToDTO)
                .toList();
    }

    @Transactional
    public ProjectDTO addProject(ProjectDTO projectDTO) {
        validateInput(projectDTO, projectDTO.getPersonelId());

        ProjectEntity projectEntity = projectMapper.dtoToModel(projectDTO);
        PersonelEntity personel = personelRepository.findById(projectDTO.getPersonelId())
                .orElseThrow(() -> new IllegalArgumentException("Personel not found with id: " + projectDTO.getPersonelId()));

        projectEntity.setPersonel(personel);
        projectEntity = projectRepository.save(projectEntity);
        return projectMapper.modelToDTO(projectEntity);
    }

    @Transactional
    public ProjectDTO updateProject(ProjectUpdateRequestDTO projectUpdateRequest) {
        validateInput(projectUpdateRequest.getProjectId(), projectUpdateRequest.getProjectDTO());

        ProjectEntity existingProjectEntity = projectRepository.findById(projectUpdateRequest.getProjectId())
                .orElseThrow(() -> new IllegalArgumentException("Project not found with id: " + projectUpdateRequest.getProjectId()));
        projectMapper.updateModel(projectUpdateRequest.getProjectDTO(), existingProjectEntity);
        return projectMapper.modelToDTO(existingProjectEntity);
    }

    @Transactional
    public void deleteProject(Long projectId) {
        validateInput(projectId);
        ProjectEntity projectEntity = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Experience not found with id: " + projectId));
        projectRepository.delete(projectEntity);
    }
}
