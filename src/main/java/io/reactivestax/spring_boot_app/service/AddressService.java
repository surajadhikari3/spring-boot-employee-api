package io.reactivestax.spring_boot_app.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.reactivestax.spring_boot_app.domain.Address;
import io.reactivestax.spring_boot_app.dto.AddressDTO;
import io.reactivestax.spring_boot_app.repository.AddressRepository;

@Service
public class AddressService {

    @Autowired
    private AddressRepository repository;

    public List<AddressDTO> findAll() {
        return repository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public Optional<AddressDTO> findById(Long id) {
        return repository.findById(id).map(this::convertToDTO);
    }

    public AddressDTO save(AddressDTO addressDTO) {
        Address address = convertToEntity(addressDTO);
        return convertToDTO(repository.save(address));
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public AddressDTO convertToDTO(Address address) {
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setId(address.getId());
        addressDTO.setStreet(address.getStreet());
        addressDTO.setCity(address.getCity());
        addressDTO.setState(address.getState());
        addressDTO.setZipCode(address.getZipCode());
        return addressDTO;
    }

    public Address convertToEntity(AddressDTO addressDTO) {
        Address address = new Address();
        address.setId(addressDTO.getId());
        address.setStreet(addressDTO.getStreet());
        address.setCity(addressDTO.getCity());
        address.setState(addressDTO.getState());
        address.setZipCode(addressDTO.getZipCode());
        return address;
    }
}