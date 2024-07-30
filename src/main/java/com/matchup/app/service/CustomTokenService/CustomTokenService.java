package com.matchup.app.service.CustomTokenService;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import com.matchup.app.service.CustomEncoderService.CustomEncoderService;

@Service
public class CustomTokenService {
    
	private final CustomEncoderService customEncoderService;

	public CustomTokenService(CustomEncoderService customEncoderService) {
		this.customEncoderService = customEncoderService;
	}

	public String generateToken(Authentication authentication) {
		Instant now = Instant.now();
		JwtClaimsSet claims = JwtClaimsSet.builder()
			.issuer("self")
				.issuedAt(now)
			.expiresAt(now.plus(1, ChronoUnit.DAYS))
			.subject(authentication.getName())
			.build();
		JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(), claims);
		return this.customEncoderService.jwtEncoder().encode(jwtEncoderParameters).getTokenValue();
	}

	public String generateRegisteringToken(String email){
		Instant now = Instant.now();
		JwtClaimsSet claims = JwtClaimsSet.builder()
			.issuer("self")
			.issuedAt(now)
				.expiresAt(now.plus(1, ChronoUnit.DAYS))
				.subject(email)
				.build();

		JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(), claims);
		return this.customEncoderService.jwtEncoder().encode(jwtEncoderParameters).getTokenValue();
	}
}
