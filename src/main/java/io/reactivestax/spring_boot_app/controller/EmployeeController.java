package io.reactivestax.spring_boot_app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.reactivestax.spring_boot_app.domain.Employee;
import io.reactivestax.spring_boot_app.service.EmployeeService;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService service;

    @GetMapping
    public List<Employee> getAllEmployees() {
        return service.getAllEmployees();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        return service.getEmployeeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/employees")
    public ResponseEntity<List<Employee>> getEmployeesByDepartmentAndSort(
            @RequestParam(required = false) String department,
            @RequestParam(required = false, defaultValue = "id") String sortBy) {
        List<Employee> employees = service.findByDepartmentAndSort(department, sortBy);
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/departments/{id}/employees")
    public ResponseEntity<List<Employee>> getEmployeesByDepartment(
            @PathVariable Long id,
            @RequestParam(required = false, defaultValue = "id") String sortBy) {
        List<Employee> employees = service.findByDepartmentIdAndSort(id, sortBy);
        return ResponseEntity.ok(employees);
    }

    @PostMapping
    public Employee createEmployee(@RequestBody Employee employee) {
        return service.saveEmployee(employee);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee employeeDetails) {
        return service.getEmployeeById(id)
                .map(employee -> {
                    employee.setFirstName(employeeDetails.getFirstName());
                    employee.setLastName(employeeDetails.getLastName());
                    employee.setEmail(employeeDetails.getEmail());
                    return ResponseEntity.ok(service.saveEmployee(employee));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteEmployee(@PathVariable Long id) {
        return service.getEmployeeById(id)
                .map(employee -> {
                    service.deleteEmployee(id);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{employeeId}/address/{addressId}")
    public ResponseEntity<String> associateEmployeeWithAddress(
            @PathVariable Long employeeId,
            @PathVariable Long addressId) {
        service.associateEmployeeWithAddress(employeeId, addressId);
        return ResponseEntity.ok("Address associated with Employee");
    }

    @PostMapping("/{employeeId}/department/{departmentId}")
    public ResponseEntity<String> addEmployeeToDepartment(
            @PathVariable Long employeeId,
            @PathVariable Long departmentId) {
        service.addEmployeeToDepartment(employeeId, departmentId);
        return ResponseEntity.ok("Employee added to Department");
    }

    @PostMapping("/{employeeId}/workgroups")
    public ResponseEntity<String> addEmployeeToWorkGroups(
            @PathVariable Long employeeId,
            @RequestBody List<Long> workGroupIds) {
        service.addEmployeeToWorkGroups(employeeId, workGroupIds);
        return ResponseEntity.ok("Employee added to WorkGroups");
    }
}