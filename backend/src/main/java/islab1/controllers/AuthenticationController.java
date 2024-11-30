package islab1.controllers;

import java.util.List;
import java.util.NoSuchElementException;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import islab1.auth.AuthenticationRequest;
import islab1.auth.AuthenticationResponse;
import islab1.auth.RegisterRequest;
import islab1.auth.services.AuthenticationService;
import islab1.auth.services.JwtService;
import islab1.exceptions.ConvertionException;
import islab1.mappers.UserMapper;
import islab1.models.DTO.AdminRequestDTO;
import islab1.models.DTO.UserDTO;
import islab1.models.auth.AdminRequest;
import islab1.models.auth.Role;
import islab1.models.auth.User;
import islab1.repos.AdminRequestRepo;
import islab1.repos.UserRepo;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final UserRepo userRepo;
    private final UserMapper userMapper;
    private final AdminRequestRepo adminRequestRepo;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @Valid @RequestBody RegisterRequest request) {
        if (userRepo.getUserByUsername(request.getUsername()) != null
                || adminRequestRepo.getAdminRequestByUsername(request.getUsername()) != null) {
            return ResponseEntity.status(400).body(null);
        }
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/register/admin")
    public ResponseEntity<AuthenticationResponse> registerAdmin(
            @Valid @RequestBody RegisterRequest request) {
        if (userRepo.getUserByUsername(request.getUsername()) != null) {
            return ResponseEntity.status(400)
                    .body(new AuthenticationResponse("User with that name already exists", Role.UNDEFINED));
        }
        if (userRepo.getUsersByRole(Role.ADMIN).isEmpty()) {
            return ResponseEntity.ok(authenticationService.registerAdmin(request));
        } else {
            if (adminRequestRepo.getAdminRequestByUsername(request.getUsername()) != null) {
                return ResponseEntity.status(400)
                        .body(new AuthenticationResponse("User with that name already exists", Role.UNDEFINED));
            }
            return ResponseEntity.status(202).body(authenticationService.addRegisterAdminRequest(request));
        }
    }

    @PostMapping("/register/accept/{request_id}")
    public ResponseEntity<String> acceptRegistrationRequest(
            @PathVariable("request_id") long requestId) {
        AdminRequest adminRequest = null;
        try {
            adminRequest = adminRequestRepo.findById(requestId).get();
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(400).body("Request with that id doesn't exists");
        }
        if (adminRequest == null) {
            return ResponseEntity.status(400).body("Request with that id doesn't exists");
        }
        if (adminRequest.getReviewer() != null) {
            return ResponseEntity.status(400).body("Request with that id already accepted");
        }
        authenticationService.acceptAdminRequest(adminRequest);
        adminRequest.setReviewer(authenticationService.getCurrentUser());
        adminRequestRepo.save(adminRequest);
        return ResponseEntity.ok("Request with ID " + requestId + " has been accepted.");
    }

    @GetMapping("/register/accept")
    public ResponseEntity<List<AdminRequestDTO>> getRequests() {
        List<AdminRequest> requests = adminRequestRepo.findAll();
        List<AdminRequestDTO> result = authenticationService.convertRequestsToDTO(requests);
        return ResponseEntity.status(200).body(result);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("/login_tmp")
    public ResponseEntity<AuthenticationResponse> acceptRegistrationRequest(@RequestBody UserDTO UserDTO) {
        User currentUser = authenticationService.getCurrentUser();
        if (!UserDTO.getUsername().equals(currentUser.getUsername())) {
            if (userRepo.existsByUsername(UserDTO.getUsername())) {
                return ResponseEntity.status(400)
                        .body(new AuthenticationResponse("User with that name already exists", Role.UNDEFINED));
            }
        }
        User updatedUser = null;
        try {
            updatedUser = userMapper.toEntity(UserDTO);
        } catch (ConvertionException e) {
            return ResponseEntity.status(400).body(new AuthenticationResponse(e.getMessage(), Role.UNDEFINED));
        }
        updatedUser.setId(currentUser.getId());
        updatedUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        updatedUser.setRole(currentUser.getRole());
        userRepo.save(updatedUser);
        String jwtToken = jwtService.generateToken(updatedUser);
        return ResponseEntity.ok(AuthenticationResponse.builder()
                .token(jwtToken).role(Role.USER)
                .build());
    }

}
