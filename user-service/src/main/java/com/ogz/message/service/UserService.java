package com.ogz.message.service;

import com.ogz.message.repository.UserRepository;
import org.ogz.model.User;
import org.springframework.stereotype.Service;

import java.util.*;

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
        return optUser.orElse(null);
    }
    public User findUserByGmail(String gmail){
       return  userRepository.findUserByGmailEquals(gmail);
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
            return userRepository.save(presentUser);
        }
        else return null;
    }
    public User reAccessToken(String id,String token){
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            User presentUser = user.get();
            presentUser.getAccessToken().put("Google",token);
            return userRepository.save(user.get());
        }
        else return null;
    }
    public List<User> searchUsers(String search){

        return userRepository.findAllByNameLikeIgnoreCaseOrSurnameLikeIgnoreCase(search,search);

    }
}
