package com.example.reactive.mongodb.security;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.example.reactive.mongodb.model.User;
import com.example.reactive.mongodb.repository.UserRepository;

import reactor.core.publisher.Mono;

@Component
public class ReactiveUserDetailsServiceImpl implements ReactiveUserDetailsService {

	private UserRepository userRepository;

	public ReactiveUserDetailsServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public Mono<UserDetails> findByUsername(String login) {
		return userRepository.findByLogin(login).filter(Objects::nonNull)
				.switchIfEmpty(Mono.error(new BadCredentialsException(String.format("User %s not found in database", login))))
				.map(this::createSpringSecurityUser);
	}

	private org.springframework.security.core.userdetails.User createSpringSecurityUser(User user) {
		List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
				.map(authority -> new SimpleGrantedAuthority(authority.getName())).collect(Collectors.toList());
		return new org.springframework.security.core.userdetails.User(user.getLogin(), user.getPassword(),grantedAuthorities);
	}
}
