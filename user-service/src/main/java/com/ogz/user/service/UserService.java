package com.ogz.user.service;

import com.ogz.user.repository.UserRepository;
import org.ogz.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findUserByGoogleId(String idToken){
        return userRepository.findUserByGoogleID(idToken);
    }

    public User addUser(User user) {
        return userRepository.insert(user);
    }
    public List<User> findAll(){
        return userRepository.findAll();
    }
}
