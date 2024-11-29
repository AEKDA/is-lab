package islab1.services;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;

import islab1.exceptions.ConvertionException;
import islab1.mappers.AddressMapper;
import islab1.models.DTO.AddressDTO;
import islab1.models.Address;
import islab1.models.auth.Role;
import islab1.models.auth.User;
import islab1.repos.AddressRepo;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressMapper addressMapper;
    private final AddressRepo addressRepo;

    public List<Address> getAllAddreses() {
        return addressRepo.findAll();
    }

    public List<Address> getAddresesByUser(User user) {
        return addressRepo.getAddresssByCreator(user);
    }

    public Address getAddressById(Long id) {
        return addressRepo.getById(id);
    }

    public boolean existById(Long id) {
        return addressRepo.existsById(id);
    }

    public Address createAddress(AddressDTO addressDTO) throws ConvertionException {
        Address address = addressMapper.toEntity(addressDTO);
        return addressRepo.save(address);
    }

    public Address updateAddress(Long id, AddressDTO addressDTO) throws ConvertionException, EntityNotFoundException {
        Address newAddress = addressMapper.toEntity(addressDTO);
        if (!existById(id)) {
            throw new EntityNotFoundException("There is no address with id " + id);
        }
        Address address = getAddressById(id);
        
        newAddress.setId(id);
        newAddress.setCreator(address.getCreator());
        addressRepo.save(newAddress);
        return newAddress;
    }

    public void deleteAddress(Long id){
        try{
            addressRepo.deleteById(id);
        }catch (Exception e){
            throw e;
        }
    }

    public boolean checkAccess(User user, Long addressId) {
        if (user.getRole().equals(Role.ADMIN)) {
            return true;
        }
        Address address = getAddressById(addressId);
        return address != null && address.getCreator().equals(user);
    }

    public List<AddressDTO> convertAddresesToDTOs(List<Address> addresss) {
        return addresss.stream()
            .map(addressMapper::toDto)
            .collect(Collectors.toList());
    }

    public AddressDTO convertAddressToDTO(Address address) {
        return addressMapper.toDto(address);
    }
}
