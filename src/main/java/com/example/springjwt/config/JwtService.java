package com.example.springjwt.config;

import java.security.Key;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	private static final String  SECRET_KEY = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";
	
	public String extractUserName(String jwt) {
		
		return extractClaim(jwt,Claims::getSubject);
	}
	
	public <T> T extractClaim(String token , Function<Claims,T> claimResolver) {
		final Claims claims = extraClaims(token);
		return claimResolver.apply(claims);
	}
	
	private Claims extraClaims(String jwt) {
		return Jwts.parserBuilder()
				.setSigningKey(getSignInKey())
				.build()
				.parseClaimsJws(jwt)
				.getBody();
	}

	private Key getSignInKey() {
		byte[] keyBytes= Decoders.BASE64.decode(SECRET_KEY); 
		return Keys.hmacShaKeyFor(keyBytes);
	}
	
	public String generateToken (UserDetails userDetails) {
		return generateToken(new HashMap<>(), userDetails);
	}
	
	public String generateToken(
			Map<String, Object> extraClaims,
			UserDetails userDetails
			) {
		return Jwts
				.builder()
				.setClaims(extraClaims)
				.setSubject(userDetails.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis()+1000*60*24))
				.signWith(getSignInKey(),SignatureAlgorithm.HS256)
				.compact();
	}
	
	//validateToken
	public Boolean isTokenValid(String jwt,UserDetails userDetails) {
		final String userName = extractUserName(jwt);
		return (userName.equals(userDetails.getUsername())) && !isTokenExpired(jwt);
	}

	private boolean isTokenExpired(String jwt) {
		
		return extractExpiration(jwt).before(new Date(System.currentTimeMillis()));
	}

	private java.util.Date extractExpiration(String jwt) {
		
		return extractClaim(jwt,Claims::getExpiration);
	}
	
}
