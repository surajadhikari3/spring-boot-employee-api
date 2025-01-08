package io.reactivestax.spring_boot_app.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import io.reactivestax.spring_boot_app.domain.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {
    // No need to implement anything, CRUD operations are auto-configured

    List<Employee> findByDepartment(String department, Sort sort);

    List<Employee> findByDepartmentId(Long departmentId, Sort sort);
}