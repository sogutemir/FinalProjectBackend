package org.work.personnelinfo.project.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectUpdateRequestDTO {

    private Long projectId;

    private ProjectDTO projectDTO;

}
