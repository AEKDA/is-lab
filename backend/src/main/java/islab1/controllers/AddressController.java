package islab1.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import islab1.auth.services.AuthenticationService;
import islab1.exceptions.ConvertionException;
import islab1.models.DTO.AddressDTO;
import islab1.models.Address;
import islab1.models.auth.Role;
import islab1.models.auth.User;
import islab1.services.AddressService;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/address")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AddressController {

    private final AddressService addressService;
    private final AuthenticationService authenticationService;

    @GetMapping
    public ResponseEntity<List<AddressDTO>> getAllAddreses() {
        User user = authenticationService.getCurrentUser();
        List<Address> addreses = new ArrayList<>();
        if (user.getRole() == Role.ADMIN) {
            addreses = addressService.getAllAddreses();
        } else {
            addreses = addressService.getAddresesByUser(user);
        }
        List<AddressDTO> addressDTOs = addressService.convertAddresesToDTOs(addreses);
        return ResponseEntity.ok(addressDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AddressDTO> getAddressById(@PathVariable Long id) {
        User user = authenticationService.getCurrentUser();
        if (!addressService.existById(id)) {
            return ResponseEntity.status(400).header("ErrMessage", "Address not found").body(null);
        }
        if (!addressService.checkAccess(user, id)) {
            return ResponseEntity.status(403).header("ErrMessage", "Access denied").body(null);
        }
        Address address = addressService.getAddressById(id);
        AddressDTO addressDTO = addressService.convertAddressToDTO(address);
        return ResponseEntity.ok(addressDTO);
    }

    @PostMapping
    public ResponseEntity<AddressDTO> createAddress(@RequestBody AddressDTO addressDTO) {
        User user = authenticationService.getCurrentUser();
        addressDTO.setCreatorId(user.getId());
        Address address;
        try {
            address = addressService.createAddress(addressDTO);
        } catch (ConvertionException e) {
            return ResponseEntity.status(400).header("ErrMessage", e.getMessage()).body(null);
        }
        addressDTO = addressService.convertAddressToDTO(address);
        return ResponseEntity.ok(addressDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AddressDTO> updateAddress(@PathVariable Long id, @RequestBody AddressDTO addressDTO) {
        User user = authenticationService.getCurrentUser();
        if (!addressService.existById(id)) {
            return ResponseEntity.status(400).header("ErrMessage", "Address not found").body(null);
        }
        if (user.getRole() != Role.ADMIN && !addressService.checkAccess(user, id)) {
            return ResponseEntity.status(403).header("ErrMessage", "Access denied").body(null);
        }
        Address address;
        try {
            address = addressService.updateAddress(id, addressDTO);
        } catch (EntityNotFoundException | ConvertionException e) {
            return ResponseEntity.status(400).header("ErrMessage", e.getMessage()).body(null);
        }
        addressDTO = addressService.convertAddressToDTO(address);
        return ResponseEntity.ok(addressDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long id) {
        User user = authenticationService.getCurrentUser();
        if (!addressService.existById(id)) {
            return ResponseEntity.status(400).header("ErrMessage", "Address not found").body(null);
        }
        if (user.getRole() != Role.ADMIN && !addressService.checkAccess(user, id)) {
            return ResponseEntity.status(403).header("ErrMessage", "Access denied").body(null);
        }
        try{
            addressService.deleteAddress(id);
        } catch (Exception e){
            return ResponseEntity.status(400).header("ErrMessage", "Unable to delete entity because it is used in other entity").body(null);
        }
        return ResponseEntity.ok().build();
    }
}
