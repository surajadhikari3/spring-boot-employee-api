package io.reactivestax.spring_boot_app.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import io.reactivestax.spring_boot_app.dto.*;
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
    private AddressService addressService;
    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private WorkGroupService workGroupService;
    @Autowired
    private WorkGroupRepository workGroupRepository;

    public List<EmployeeDTO> findAll() {
        return employeeRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public Optional<EmployeeDTO> findById(Long id) {
        return employeeRepository.findById(id).map(this::convertToDTO);
    }

    public EmployeeDTO save(EmployeeDTO employeeDTO) {
        Employee employee = convertToEntity(employeeDTO);
        return convertToDTO(employeeRepository.save(employee));
    }


    public Employee addFullEmployee(EmployeeFullDTO employeeFullDTO) {
        Employee employee = convertToFullEntityAndPersistToDB(employeeFullDTO);
        return employeeRepository.save(employee);
    }

    public void deleteById(Long id) {
        employeeRepository.deleteById(id);
    }

    private EmployeeDTO convertToDTO(Employee employee) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setId(employee.getId());
        employeeDTO.setFirstName(employee.getFirstName());
        employeeDTO.setLastName(employee.getLastName());
        employeeDTO.setEmail(employee.getEmail());
        employeeDTO.setAddressId(employee.getAddress() != null ? employee.getAddress().getId() : null);
        employeeDTO.setDepartmentId(employee.getDepartment() != null ? employee.getDepartment().getId() : null);
        employeeDTO.setWorkGroupIds(employee.getWorkGroups().stream().map(WorkGroup::getId).collect(Collectors.toList()));
        return employeeDTO;
    }

    private Employee convertToEntity(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        employee.setId(employeeDTO.getId());
        employee.setFirstName(employeeDTO.getFirstName());
        employee.setLastName(employeeDTO.getLastName());
        employee.setEmail(employeeDTO.getEmail());
        // Assuming you have methods to fetch Address, Department, and WorkGroups by their IDs
        if (employeeDTO.getAddressId() != null) {
            employee.setAddress(fetchAddressById(employeeDTO.getAddressId()));
        }
        if (employeeDTO.getDepartmentId() != null) {
            employee.setDepartment(fetchDepartmentById(employeeDTO.getDepartmentId()));
        }
        if (employeeDTO.getWorkGroupIds() != null) {
            employee.setWorkGroups(fetchWorkGroupsByIds(employeeDTO.getWorkGroupIds()));
        }

        return employee;
    }

    private Employee convertToFullEntityAndPersistToDB(EmployeeFullDTO employeeDTO) {
        Employee employee = new Employee();
        employee.setId(employeeDTO.getId());
        employee.setFirstName(employeeDTO.getFirstName());
        employee.setLastName(employeeDTO.getLastName());
        employee.setEmail(employeeDTO.getEmail());
        if (employeeDTO.getAddressDTO() != null) {
            employee.setAddress(addressService.convertToEntity(employeeDTO.getAddressDTO()));
        }
        if (employeeDTO.getDepartmentDTO() != null) {
            employee.setDepartment(departmentService.convertToEntity(employeeDTO.getDepartmentDTO()));
        }
        if (employeeDTO.getWorkGroupDTOS() != null) {
            ArrayList<WorkGroup> workGroups = new ArrayList<>();
            employeeDTO.getWorkGroupDTOS().forEach(workGroupDTO ->
                workGroups.add(workGroupService.convertToEntity(workGroupDTO)));
            employee.setWorkGroups(workGroups);
        }
        return employee;
    }

    private Address fetchAddressById(Long addressId) {
        return addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));
    }

    private Department fetchDepartmentById(Long departmentId) {
        return departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Department not found"));
    }

    private List<WorkGroup> fetchWorkGroupsByIds(List<Long> workGroupIds) {
        return workGroupRepository.findAllById(workGroupIds);
    }

    public void associateEmployeeWithNewAddress(Long employeeId, Long addressId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));
        address.setEmployee(employee);
        employee.setAddress(address);
        addressRepository.save(address);
        employeeRepository.save(employee);
    }

    public void associateEmployeeWithNewAddress(Long employeeId, AddressDTO addressDTO) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        AddressDTO savedAddress = addressService.save(addressDTO);
        Address address = addressRepository.findById(savedAddress.getId())
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


    public void associateEmployeeWithNewDepartment(Long employeeId, DepartmentDTO departmentDTO) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        if (departmentDTO.getEmployeeIds() == null) {
            departmentDTO.setEmployeeIds(List.of(employeeId));
        }
        DepartmentDTO savedDepartment = departmentService.save(departmentDTO);
        Department department = departmentRepository.findById(savedDepartment.getId())
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
        employeeRepository.save(employee);
    }

    public void associateEmployeeWithNewWorkGroups(Long employeeId, List<WorkGroupDTO> workGroupDTOS) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        List<Long> workGroupIds = new ArrayList<>();
        workGroupDTOS.forEach(workGroupDTO -> {
            WorkGroupDTO savedWorkGroupDTO = workGroupService.save(workGroupDTO);
            workGroupIds.add(savedWorkGroupDTO.getId());
        });
        List<WorkGroup> workGroups = workGroupRepository.findAllById(workGroupIds);
        employee.getWorkGroups().addAll(workGroups);
        workGroups.forEach(workGroup -> workGroup.getEmployees().add(employee));
//        workGroupRepository.saveAll(workGroups);
        employeeRepository.save(employee);
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
