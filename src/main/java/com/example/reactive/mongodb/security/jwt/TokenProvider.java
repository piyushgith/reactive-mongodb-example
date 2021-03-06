package com.example.reactive.mongodb.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class TokenProvider {

	private final Logger log = LoggerFactory.getLogger(TokenProvider.class);

	private static final String SECRETE_KEY = "JpxM4e858rc673syopdZnMFb*ExedfsdtyujiuuldgfsfagdfhgfjhgjqJtUc0HJ_iOxu~jiSYu+yPdPw93OBBjF";
	private static final String AUTHORITIES_KEY = "auth";
	private final int TOKEN_VALIDITY = 300; // Value in second
	private long tokenValidityInMilliseconds;

	private Key myKey;

	@PostConstruct
	public void init() {
		//Multiple Ways to do it
		//A Base64-encoded string: ==>
		//SecretKey key2 = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRATE_KEY));
		//A Base64URL-encoded string: ==>
		//SecretKey key3 = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(SECRATE_KEY));
		//A raw (non-encoded) string (e.g. a password String): ==>
		//SecretKey key4 = Keys.hmacShaKeyFor(SECRATE_KEY.getBytes(StandardCharsets.UTF_8));

		myKey= Keys.hmacShaKeyFor(SECRETE_KEY.getBytes(StandardCharsets.UTF_8));
		this.tokenValidityInMilliseconds = 1000 * TOKEN_VALIDITY;
	}

	public String createToken(Authentication authentication) {
		String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(","));

		long now = (new Date()).getTime();
		Date validity = new Date(now + this.tokenValidityInMilliseconds);
		//creating JWT
		return Jwts.builder().setSubject(authentication.getName()).claim(AUTHORITIES_KEY,authorities)
				.signWith(myKey,SignatureAlgorithm.HS512).setExpiration(validity).compact();
	}

	public Authentication getAuthentication(String token) {
		if (StringUtils.isEmpty(token) || !validateToken(token)) {
			throw new BadCredentialsException("Invalid token");
		}
		//We should use Interface Key implementations not simple Strings
		Claims claims= Jwts.parserBuilder().setSigningKey(myKey).build().parseClaimsJws(token).getBody();

		Collection<? extends GrantedAuthority> authorities = Arrays
				.stream(claims.get(AUTHORITIES_KEY).toString().split(",")).map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());

		User principal = new User(claims.getSubject(), "", authorities);

		return new UsernamePasswordAuthenticationToken(principal, token, authorities);
	}

	public boolean validateToken(String authToken) {
		try {
			Jwts.parserBuilder().setSigningKey(myKey).build().parseClaimsJws(authToken);
			return true;
		} catch (SignatureException e) {
			log.info("Invalid JWT signature.");
			log.trace("Invalid JWT signature trace: {}", e);
		} catch (MalformedJwtException e) {
			log.info("Invalid JWT token.");
			log.trace("Invalid JWT token trace: {}", e);
		} catch (ExpiredJwtException e) {
			log.info("Expired JWT token.");
			log.trace("Expired JWT token trace: {}", e);
		} catch (UnsupportedJwtException e) {
			log.info("Unsupported JWT token.");
			log.trace("Unsupported JWT token trace: {}", e);
		} catch (IllegalArgumentException e) {
			log.info("JWT token compact of handler are invalid.");
			log.trace("JWT token compact of handler are invalid trace: {}", e);
		}
		return false;
	}
}
