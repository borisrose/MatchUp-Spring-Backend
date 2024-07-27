package com.matchup.app.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.matchup.app.model.User;
import com.matchup.app.service.CustomEncoderService.CustomEncoderService;
import com.matchup.app.service.CustomTokenService.CustomTokenService;
import com.matchup.app.service.UserService.UserService;

@RestController
@RequestMapping("/api")
public class UserController {

	private final AuthenticationManager authenticationManager;
	private final CustomTokenService customTokenService;
	private final UserService userService;

	public UserController(
        AuthenticationManager authenticationManager, 
        CustomTokenService customTokenService, 
        UserService userService
    ) {
		this.authenticationManager = authenticationManager;
		this.customTokenService = customTokenService;
		this.userService = userService;
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