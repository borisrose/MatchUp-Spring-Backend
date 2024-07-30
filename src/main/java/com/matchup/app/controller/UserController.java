package com.matchup.app.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.matchup.app.dto.UserDto;
import com.matchup.app.model.User;
import com.matchup.app.service.CustomEncoderService.CustomEncoderService;
import com.matchup.app.service.CustomTokenService.CustomTokenService;
import com.matchup.app.service.UserService.UserService;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    ModelMapper modelMapper;

	private final AuthenticationManager authenticationManager;
	private final CustomTokenService customTokenService;
	private final UserService userService;
    private final CustomEncoderService customEncoderService;

	public UserController(
        AuthenticationManager authenticationManager, 
        CustomTokenService customTokenService, 
        UserService userService,
        CustomEncoderService customEncoderService
    ) {
		this.authenticationManager = authenticationManager;
		this.customTokenService = customTokenService;
		this.userService = userService;
        this.customEncoderService = customEncoderService;
	}

    // CONVERTER DTO

    private UserDto convertToDto(Object user) {
        UserDto userDto = modelMapper.map(user, UserDto.class);
        return userDto;
    }



	@PostMapping("/auth/login")
    public ResponseEntity<ResponseToken> loginUser(@RequestBody LoginBody body) {
        Authentication authenticationRequest = UsernamePasswordAuthenticationToken.unauthenticated(body.email,
                body.password);
        Authentication authenticationResponse = this.authenticationManager.authenticate(authenticationRequest);
        String token = customTokenService.generateToken(authenticationResponse);

        if (token != null) {
            return ResponseEntity.ok(new ResponseToken(token));
        } else {
            return ResponseEntity.status(401).build();
        }

    }

    @GetMapping(path = "/auth/me")
    public ResponseEntity<UserDto> getCurrentUser(@RequestHeader(name = "Authorization") String authorizationHeader) {
       
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        Jwt jwt = customEncoderService.jwtDecoder().decode(jwtToken);
        String username = jwt.getSubject(); 
        User user = userService.getUserByEmail(username);
        UserDto userDto = convertToDto(user);
        if(userDto != null){
            return ResponseEntity.ok(userDto);
        } else {
            return ResponseEntity.status(401).build();
        }
    }


	//------------------------------REGISTER-----------------------

	@PostMapping("/auth/register")
    public ResponseEntity<ResponseToken> registerUser(@RequestBody RegisterBody body) {
        User newUser = userService.createUser(body.firstname, body.lastname, body.email,body.password);
        if (newUser != null) {
            String token = customTokenService.generateRegisteringToken(newUser.getEmail());
            return ResponseEntity.ok(new ResponseToken(token));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }



	//--------------------------------RECORDS------------------
	public record LoginBody(String email, String password) {
    }
	public record ResponseToken(String token){}
    public record RegisterBody(String firstname, String lastname, String email, String password) {
    }

}