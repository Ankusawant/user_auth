package com.usermanagement.controller;

import com.usermanagement.dto.LoginRequest;
import com.usermanagement.dto.LoginResponse;
import com.usermanagement.entity.Role;
import com.usermanagement.entity.RoleType;
import com.usermanagement.entity.User;
import com.usermanagement.security.JwtUtil;
import com.usermanagement.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {

	private final UserService userService;
	private final PasswordEncoder passwordEncoder;
	private final com.usermanagement.security.JwtUtil jwtUtil;

	public AuthController(UserService userService, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
		this.userService = userService;
		this.passwordEncoder = passwordEncoder;
		this.jwtUtil = jwtUtil;
	}

	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody User user) {
		try {

			if (userService.findByUsername(user.getUsername()).isPresent()) {
				return new ResponseEntity<>("Username already exists!", HttpStatus.BAD_REQUEST);
			}

			if (userService.findByEmail(user.getEmail()).isPresent()) {
				return new ResponseEntity<>("Email already exists!", HttpStatus.BAD_REQUEST);
			}

			for (Role role : user.getRoles()) {
				if (!EnumSet.allOf(RoleType.class).contains(role.getName())) {
					return new ResponseEntity<>("Invalid role: You only have access to the following roles: "
							+ EnumSet.allOf(RoleType.class), HttpStatus.BAD_REQUEST);
				}
			}

			user.setPassword(passwordEncoder.encode(user.getPassword()));
			userService.save(user);

			return new ResponseEntity<>("User registered successfully!", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Error during registration: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
		try {

			String email = loginRequest.getEmail();
			String password = loginRequest.getPassword();

			User user = userService.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

			if (passwordEncoder.matches(password, user.getPassword())) {

				List<String> roles = user.getRoles().stream().map(role -> role.getName().name())
						.collect(Collectors.toList());
				String token = jwtUtil.generateJwtToken(email, roles);

				return ResponseEntity.ok(new LoginResponse(user.getId(), roles, token));
			} else {
				return new ResponseEntity<>("Invalid password", HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			return new ResponseEntity<>("Error during login: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}