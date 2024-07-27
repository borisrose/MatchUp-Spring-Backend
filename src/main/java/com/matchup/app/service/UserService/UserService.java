package com.matchup.app.service.UserService;

import java.util.List;
import java.util.Optional;

import com.matchup.app.model.User;

public interface UserService {
    public User createUser(String firstname, String lastname, String email, String password);
    public User getUserByEmail(String email);
    public List<User> getAllUsers();
    public Optional<User> getOneUserById(String id);
    public Optional<User> deleteUserById(String id);
}
