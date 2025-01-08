package io.reactivestax.spring_boot_app.dto;

import java.util.List;

import lombok.Data;

@Data
public class EmployeeDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Long addressId;

    private Long departmentId;
    private List<Long> workGroupIds;
}
