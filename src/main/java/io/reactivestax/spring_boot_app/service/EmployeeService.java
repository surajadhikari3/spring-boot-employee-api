package io.reactivestax.spring_boot_app.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import io.reactivestax.spring_boot_app.domain.Address;
import io.reactivestax.spring_boot_app.domain.Department;
import io.reactivestax.spring_boot_app.domain.Employee;
import io.reactivestax.spring_boot_app.domain.WorkGroup;
import io.reactivestax.spring_boot_app.repository.AddressRepository;
import io.reactivestax.spring_boot_app.repository.DepartmentRepository;
import io.reactivestax.spring_boot_app.repository.EmployeeRepository;
import io.reactivestax.spring_boot_app.repository.WorkGroupRepository;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private WorkGroupRepository workGroupRepository;

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Optional<Employee> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }

    public void associateEmployeeWithAddress(Long employeeId, Long addressId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));        
        address.setEmployee(employee);
        employee.setAddress(address);
        addressRepository.save(address);
        employeeRepository.save(employee);
    }

    public void addEmployeeToDepartment(Long employeeId, Long departmentId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Department not found"));
        employee.setDepartment(department);
        employeeRepository.save(employee);
    }

    public void addEmployeeToWorkGroups(Long employeeId, List<Long> workGroupIds) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        List<WorkGroup> workGroups = workGroupRepository.findAllById(workGroupIds);
        employee.getWorkGroups().addAll(workGroups);
        workGroups.forEach(workGroup -> workGroup.getEmployees().add(employee));
        workGroupRepository.saveAll(workGroups);
    }

    public List<Employee> findByDepartmentAndSort(String department, String sortBy) {
        if (department == null) {
            return employeeRepository.findAll(Sort.by(sortBy));
        } else {
            return employeeRepository.findByDepartment(department, Sort.by(sortBy));
        }
    }

    public List<Employee> findByDepartmentIdAndSort(Long departmentId, String sortBy) {
        return employeeRepository.findByDepartmentId(departmentId, Sort.by(sortBy));
    }
}
