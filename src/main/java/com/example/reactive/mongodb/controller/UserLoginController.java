package com.example.reactive.mongodb.controller;

import javax.validation.Validator;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.reactive.mongodb.model.LoginVM;
import com.example.reactive.mongodb.security.jwt.JWTReactiveAuthenticationManager;
import com.example.reactive.mongodb.security.jwt.JWTToken;
import com.example.reactive.mongodb.security.jwt.TokenProvider;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
public class UserLoginController {

	private final TokenProvider tokenProvider;
	private final JWTReactiveAuthenticationManager authenticationManager;
	private final Validator validation;

	public UserLoginController(TokenProvider tokenProvider, JWTReactiveAuthenticationManager authenticationManager,
			Validator validation) {
		this.tokenProvider = tokenProvider;
		this.authenticationManager = authenticationManager;
		this.validation = validation;
	}

	@PostMapping("/login")
	public Mono<JWTToken> loginVerification(@RequestBody LoginVM loginVM) {
		if (!this.validation.validate(loginVM).isEmpty()) {
			return Mono.error(new RuntimeException("Bad request"));
		}

		Authentication authenticationToken = new UsernamePasswordAuthenticationToken(loginVM.getUsername(),
				loginVM.getPassword());

		Mono<Authentication> authentication = this.authenticationManager.authenticate(authenticationToken);
		authentication.doOnError(throwable -> {
			throw new BadCredentialsException("Bad crendentials");
		});
		ReactiveSecurityContextHolder.withAuthentication(authenticationToken);

		return authentication.map(auth -> {
			String jwt = tokenProvider.createToken(auth);
			return new JWTToken(jwt);
		});
	}

}
