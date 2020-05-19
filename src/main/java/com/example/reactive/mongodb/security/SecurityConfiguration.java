package com.example.reactive.mongodb.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

import com.example.reactive.mongodb.security.jwt.JWTHeadersExchangeMatcher;
import com.example.reactive.mongodb.security.jwt.JWTReactiveAuthenticationManager;
import com.example.reactive.mongodb.security.jwt.TokenProvider;
import com.example.reactive.mongodb.security.jwt.UnauthorizedAuthenticationEntryPoint;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity//(proxyTargetClass = true)
public class SecurityConfiguration {

    private final ReactiveUserDetailsServiceImpl reactiveUserDetailsService;
    private final TokenProvider tokenProvider;

    private static final String[] AUTH_WHITELIST = {
            "/resources/**",
            "/webjars/**",
            "/api/login/**",
            "/favicon.ico",
    };

	public SecurityConfiguration(ReactiveUserDetailsServiceImpl reactiveUserDetailsService,
			TokenProvider tokenProvider) {
		this.reactiveUserDetailsService = reactiveUserDetailsService;
		this.tokenProvider = tokenProvider;
	}

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http, UnauthorizedAuthenticationEntryPoint entryPoint) {

    	//You can't make individual api permit or disable after addFilterAt like below. 
		//You have to make them available after authenticationEntryPoint
		//http.authorizeExchange().pathMatchers(HttpMethod.GET, "api/tweets**").permitAll();
    	
    	http.httpBasic().disable().formLogin().disable().csrf().disable().logout().disable();
    	
		http.exceptionHandling()
			.authenticationEntryPoint(entryPoint)
			.and()
			.addFilterAt(webFilter(), SecurityWebFiltersOrder.AUTHORIZATION).authorizeExchange()
			.pathMatchers(AUTH_WHITELIST).permitAll()
			.anyExchange().authenticated();
		
		return http.build();
		
		// @formatter:off
		/*Example
        http
        .exceptionHandling()
        .authenticationEntryPoint(entryPoint)
        
        .and()
        .authorizeExchange()
        .matchers(EndpointRequest.to("health", "info"))
        .permitAll()
        
        .and()
        .authorizeExchange()
        .pathMatchers(HttpMethod.OPTIONS)
        .permitAll()
        
        .and()
        .authorizeExchange()
        .matchers(EndpointRequest.toAnyEndpoint())
        .hasAuthority(AuthoritiesConstants.ADMIN)
        
        .and()
        .addFilterAt(webFilter(), SecurityWebFiltersOrder.AUTHORIZATION)
        .authorizeExchange()
        .pathMatchers(AUTH_WHITELIST).permitAll()
        .anyExchange().authenticated();*/
		// @formatter:on
    }

    @Bean
    public AuthenticationWebFilter webFilter() {
        AuthenticationWebFilter authenticationWebFilter = new AuthenticationWebFilter(repositoryReactiveAuthenticationManager());
        authenticationWebFilter.setAuthenticationConverter(new TokenAuthenticationConverter(tokenProvider));
        authenticationWebFilter.setRequiresAuthenticationMatcher(new JWTHeadersExchangeMatcher());
//        authenticationWebFilter.setSecurityContextRepository(new WebSessionServerSecurityContextRepository());
        authenticationWebFilter.setSecurityContextRepository(NoOpServerSecurityContextRepository.getInstance());
        return authenticationWebFilter;
    }

    @Bean
    public JWTReactiveAuthenticationManager repositoryReactiveAuthenticationManager() {
        JWTReactiveAuthenticationManager repositoryReactiveAuthenticationManager = new JWTReactiveAuthenticationManager(reactiveUserDetailsService, passwordEncoder());
        return repositoryReactiveAuthenticationManager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(4);
    }


}
