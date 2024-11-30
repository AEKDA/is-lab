package islab1.auth.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import islab1.auth.AuthenticationRequest;
import islab1.auth.AuthenticationResponse;
import islab1.auth.RegisterRequest;
import islab1.mappers.AdminRequestsMapper;
import islab1.models.DTO.AdminRequestDTO;
import islab1.models.auth.AdminRequest;
import islab1.models.auth.Role;
import islab1.models.auth.User;
import islab1.models.auth.User.UserBuilder;
import islab1.repos.AdminRequestRepo;
import islab1.repos.UserRepo;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthenticationService {
        private final UserRepo userRepo;
        private final AdminRequestRepo adminRequestRepo;
        private final PasswordEncoder passwordEncoder;
        private final JwtService jwtService;
        private final AuthenticationManager authenticationManager;
        private final AdminRequestsMapper adminRequestsMapper;

        public AuthenticationResponse register(RegisterRequest request) {
                UserBuilder userBuilder = User.builder()
                                .username(request.getUsername())
                                .password(passwordEncoder.encode(request.getPassword()))
                                .role(Role.USER);
                User user = userBuilder.build();
                userRepo.save(user);
                String jwtToken = jwtService.generateToken(user);
                return AuthenticationResponse.builder()
                                .token(jwtToken).role(user.getRole())
                                .build();
        }

        public AuthenticationResponse authenticate(AuthenticationRequest request) {
                authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(
                                                request.getUsername(),
                                                request.getPassword()));
                User user = userRepo.findByUsername(request.getUsername())
                                .orElseThrow(null);
                String jwtToken = jwtService.generateToken(user);
                return AuthenticationResponse.builder()
                                .token(jwtToken).role(user.getRole())
                                .build();
        }

        public AuthenticationResponse registerAdmin(RegisterRequest request) {
                UserBuilder userBuilder = User.builder()
                                .username(request.getUsername())
                                .password(passwordEncoder.encode(request.getPassword()))
                                .role(Role.ADMIN);
                User user = userBuilder.build();
                userRepo.save(user);
                String jwtToken = jwtService.generateToken(user);
                return AuthenticationResponse.builder()
                                .token(jwtToken).role(user.getRole())
                                .build();
        }

        public AuthenticationResponse addRegisterAdminRequest(RegisterRequest request) {
                AdminRequest adminRequest = new AdminRequest();
                adminRequest.setUsername(request.getUsername());
                adminRequest.setPassword(request.getPassword());
                adminRequestRepo.save(adminRequest);
                return AuthenticationResponse.builder()
                                .token("")
                                .build();
        }

        public User getCurrentUser() {
                UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                                .getPrincipal();
                User user = userRepo.getUserByUsername(userDetails.getUsername());
                return user;
        }

        public void acceptAdminRequest(AdminRequest adminRequest) {
                registerAdmin(new RegisterRequest(adminRequest.getUsername(), adminRequest.getPassword()));
        }

        public List<AdminRequestDTO> convertRequestsToDTO(List<AdminRequest> requests) {
                return requests.stream()
                                .map(adminRequestsMapper::toDto)
                                .collect(Collectors.toList());
        }
}
