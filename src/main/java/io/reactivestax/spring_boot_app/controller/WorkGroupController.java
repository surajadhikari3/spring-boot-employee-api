package io.reactivestax.spring_boot_app.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.reactivestax.spring_boot_app.domain.WorkGroup;
import io.reactivestax.spring_boot_app.service.WorkGroupService;

@RestController
@RequestMapping("/api/workgroups")
public class WorkGroupController {

    @Autowired
    private WorkGroupService service;

    @GetMapping
    public List<WorkGroup> getAllWorkGroups() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkGroup> getWorkGroupById(@PathVariable Long id) {
        Optional<WorkGroup> workGroup = service.findById(id);
        return workGroup.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public WorkGroup createWorkGroup(@RequestBody WorkGroup workGroup) {
        return service.save(workGroup);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkGroup> updateWorkGroup(@PathVariable Long id, @RequestBody WorkGroup workGroupDetails) {
        Optional<WorkGroup> workGroup = service.findById(id);
        if (workGroup.isPresent()) {
            WorkGroup updatedWorkGroup = workGroup.get();
            updatedWorkGroup.setName(workGroupDetails.getName());
            return ResponseEntity.ok(service.save(updatedWorkGroup));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkGroup(@PathVariable Long id) {
        if (service.findById(id).isPresent()) {
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}