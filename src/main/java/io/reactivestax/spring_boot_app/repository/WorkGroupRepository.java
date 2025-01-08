package io.reactivestax.spring_boot_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.reactivestax.spring_boot_app.domain.WorkGroup;

public interface WorkGroupRepository extends JpaRepository<WorkGroup, Long> {
}