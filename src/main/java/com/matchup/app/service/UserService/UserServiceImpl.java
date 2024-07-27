package com.matchup.app.service.UserService;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.matchup.app.model.User;
import com.matchup.app.repository.UserRepository;
import com.matchup.app.service.CustomEncoderService.CustomEncoderService;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final CustomEncoderService customEncoderService;

    public UserServiceImpl(UserRepository userRepository, CustomEncoderService customEncoderService){
        this.userRepository = userRepository;
        this.customEncoderService = customEncoderService;
    }

    public User createUser(String firstname, String lastname,String email, String password){
        User newUser = new User(firstname, lastname, email, password);
        String visiblePassword = newUser.getPassword();
		newUser.setPassword(customEncoderService.passwordEncoder().encode(visiblePassword));
		User savedUser = userRepository.save(newUser);
        return userRepository.save(savedUser);
        
    }

    public User getUserByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public Optional<User> getOneUserById(String id){
        Integer identifer = Integer.parseInt(id);
        return userRepository.findById(identifer);
    }

    public Optional<User> deleteUserById(String id){
        Integer identifier = Integer.parseInt(id);
        return userRepository.findById(identifier);
    }
}
