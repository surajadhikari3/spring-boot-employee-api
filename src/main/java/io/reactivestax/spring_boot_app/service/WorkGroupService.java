package io.reactivestax.spring_boot_app.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.reactivestax.spring_boot_app.domain.WorkGroup;
import io.reactivestax.spring_boot_app.dto.WorkGroupDTO;
import io.reactivestax.spring_boot_app.repository.WorkGroupRepository;

@Service
public class WorkGroupService {

    @Autowired
    private WorkGroupRepository repository;

    public List<WorkGroupDTO> findAll() {
        return repository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public Optional<WorkGroupDTO> findById(Long id) {
        return repository.findById(id).map(this::convertToDTO);
    }

    public WorkGroupDTO save(WorkGroupDTO workGroupDTO) {
        WorkGroup workGroup = convertToEntity(workGroupDTO);
        return convertToDTO(repository.save(workGroup));
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    private WorkGroupDTO convertToDTO(WorkGroup workGroup) {
        WorkGroupDTO workGroupDTO = new WorkGroupDTO();
        workGroupDTO.setId(workGroup.getId());
        workGroupDTO.setName(workGroup.getName());
        return workGroupDTO;
    }

    private WorkGroup convertToEntity(WorkGroupDTO workGroupDTO) {
        WorkGroup workGroup = new WorkGroup();
        workGroup.setId(workGroupDTO.getId());
        workGroup.setName(workGroupDTO.getName());
        return workGroup;
    }
}