package com.matchup.app.service.CustomUserDetailsService;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.matchup.app.model.User;
import com.matchup.app.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
	private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		User user = userRepository.findByEmail(email);

		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
			getGrantedAuthorities("USER"));

    }

    private List<GrantedAuthority> getGrantedAuthorities(String role) {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
		return authorities;
	}
                        
}
