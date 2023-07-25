package com.example.springjwt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.springjwt.Entity.Role;
import com.example.springjwt.Entity.User;
import com.example.springjwt.authontroller.AuthenticationRequest;
import com.example.springjwt.authontroller.AuthenticationResponse;
import com.example.springjwt.authontroller.RegisterRequset;
import com.example.springjwt.config.JwtService;
import com.example.springjwt.repo.UserRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
	
	@Autowired
	private final UserRepo repo;
	@Autowired
	private final JwtService jwtService;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	
	public AuthenticationResponse register(RegisterRequset request) {
		
//		User user = new User();
		User user;
		user = User.builder()
				.fname(request.getFname())
				.lname(request.getLname())
				.email(request.getEmail())
				.password(passwordEncoder.encode(request.getPassword()))
				.role(Role.USER)
				.build();
		repo.save(user);
		var jwtToken = jwtService.generateToken(user);
		return AuthenticationResponse.builder().token(jwtToken).build();
	}
	
	public AuthenticationResponse authenticate(AuthenticationRequest request) {
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
				);
		var user  = repo.findByEmail(request.getEmail()).orElseThrow();
				var jwtToken = jwtService.generateToken(user);
		return AuthenticationResponse.builder().token(jwtToken).build();
	}

	

}
