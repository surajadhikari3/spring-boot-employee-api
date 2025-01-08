package io.reactivestax.spring_boot_app.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.reactivestax.spring_boot_app.domain.Department;
import io.reactivestax.spring_boot_app.dto.DepartmentDTO;
import io.reactivestax.spring_boot_app.repository.DepartmentRepository;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository repository;

    public List<DepartmentDTO> findAll() {
        return repository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public Optional<DepartmentDTO> findById(Long id) {
        return repository.findById(id).map(this::convertToDTO);
    }

    public DepartmentDTO save(DepartmentDTO departmentDTO) {
        Department department = convertToEntity(departmentDTO);
        return convertToDTO(repository.save(department));
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    private DepartmentDTO convertToDTO(Department department) {
        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setId(department.getId());
        departmentDTO.setName(department.getName());
        return departmentDTO;
    }

    private Department convertToEntity(DepartmentDTO departmentDTO) {
        Department department = new Department();
        department.setId(departmentDTO.getId());
        department.setName(departmentDTO.getName());
        return department;
    }
}