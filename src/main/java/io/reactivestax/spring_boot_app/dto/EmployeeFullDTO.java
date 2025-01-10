package io.reactivestax.spring_boot_app.dto;

import lombok.Data;

import java.util.List;

@Data
public class EmployeeFullDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private AddressDTO addressDTO;

    private DepartmentDTO departmentDTO;
    private List<WorkGroupDTO> workGroupDTOS;
}
