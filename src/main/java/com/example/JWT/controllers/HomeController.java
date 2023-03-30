package com.example.JWT.controllers;
import java.util.HashSet;
import java.util.Set;

import javax.validation.Valid;

import com.example.JWT.controllers.request.AuthRequest;
import com.example.JWT.controllers.request.RegisterRequest;
import com.example.JWT.models.Client;
import com.example.JWT.models.ERole;
import com.example.JWT.models.Role;

import com.example.JWT.repository.RoleRepository;
import com.example.JWT.repository.UserRepository;
import com.example.JWT.security.SecurityUtil;
import com.example.JWT.security.jwt.JwtUtils;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class HomeController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;
    @Autowired
    SecurityUtil securityUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<String> authenticateUser(@Valid @RequestBody AuthRequest loginRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String jwt = jwtUtils.generateJwtToken(userDetails);

        return ResponseEntity.ok(jwt);
    }

    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@Valid @RequestBody RegisterRequest registerRequest) throws AuthenticationException {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new AuthenticationException("Username is taken");
        }

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new AuthenticationException("Email is taken");
        }

        // Create new user's account
        Client user = new Client();
        user.setEmail(registerRequest.getEmail());
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        Set<Role> roles = new HashSet<>();
        Role role = new Role(ERole.ROLE_USER);
        roles.add(role);
        user.setRoles(roles);
        userRepository.saveAndFlush(user);

        return ResponseEntity.ok("You have registered successfully");
    }


    @PostMapping("/googleLogin")
    public ResponseEntity<?> googleLogin() {
        return ResponseEntity.ok("Hello ");
    }

}
