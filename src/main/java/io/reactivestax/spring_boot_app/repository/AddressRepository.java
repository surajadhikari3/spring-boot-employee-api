package io.reactivestax.spring_boot_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.reactivestax.spring_boot_app.domain.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {
}