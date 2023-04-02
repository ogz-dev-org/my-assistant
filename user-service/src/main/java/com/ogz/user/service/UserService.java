package com.ogz.user.service;

import com.ogz.user.repository.UserRepository;
import org.ogz.model.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findUserByGoogleId(String idToken){
        return userRepository.findUserByGoogleID(idToken);
    }
    public User findUserById(String userId){
        Optional<User> optUser = userRepository.findById(userId);
        if (optUser.isEmpty()) return null;
        return optUser.get();
    }
    public User addUser(User user) {
        return userRepository.insert(user);
    }
    public List<User> findAll(){
        return userRepository.findAll();
    }

    public User reRefreshToken(String id,String token){
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            User presentUser = user.get();
            presentUser.getRefreshToken().put("Google",token);
            return userRepository.insert(user.get());
        }
        else return null;
    }

    public User reAccessToken(String id,String token){
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            User presentUser = user.get();
            presentUser.getAccessToken().put("Google",token);
            return userRepository.insert(user.get());
        }
        else return null;
    }

    public List<User> searchUsers(String search){

        return userRepository.findAllByNameLikeIgnoreCaseOrSurnameLikeIgnoreCase(search,search);

    }
}
