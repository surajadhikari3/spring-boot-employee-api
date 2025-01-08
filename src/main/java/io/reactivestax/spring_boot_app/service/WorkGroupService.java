package io.reactivestax.spring_boot_app.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.reactivestax.spring_boot_app.domain.WorkGroup;
import io.reactivestax.spring_boot_app.repository.WorkGroupRepository;

@Service
public class WorkGroupService {

    @Autowired
    private WorkGroupRepository repository;

    public List<WorkGroup> findAll() {
        return repository.findAll();
    }

    public Optional<WorkGroup> findById(Long id) {
        return repository.findById(id);
    }

    public WorkGroup save(WorkGroup workGroup) {
        return repository.save(workGroup);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}