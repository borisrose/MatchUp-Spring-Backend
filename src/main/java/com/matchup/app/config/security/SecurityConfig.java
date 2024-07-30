package com.matchup.app.config.security;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import com.matchup.app.service.CustomEncoderService.CustomEncoderService;
import com.matchup.app.service.CustomUserDetailsService.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    private final CustomEncoderService customEncoderService;
    private  JwtDecoder jwtDecoder;

    SecurityConfig(CustomEncoderService customEncoderService){
        this.customEncoderService = customEncoderService;
        this.jwtDecoder = this.customEncoderService.jwtDecoder();
    }

    @Bean
    public JwtDecoder JwtDecoder(){
        return this.jwtDecoder;
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors((cors) -> cors.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authorize -> authorize
                    .requestMatchers("/api/auth/register", "/api/auth/login","/{filename}", "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                    .anyRequest().authenticated())
                .oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults())
            );
        return http.build();
    }

    
    @Bean
	public AuthenticationManager authenticationManager(CustomUserDetailsService customDetailsService, CustomEncoderService customEncoderService) {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(customDetailsService);
		authenticationProvider.setPasswordEncoder(customEncoderService.passwordEncoder());
		return new ProviderManager(authenticationProvider);
	}

   

}
