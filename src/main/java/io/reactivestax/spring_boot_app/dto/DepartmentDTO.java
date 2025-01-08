package io.reactivestax.spring_boot_app.dto;

import java.util.List;

import lombok.Data;

@Data
public class DepartmentDTO {
    private Long id;
    private String name;
    private List<Long> employeeIds;
}
