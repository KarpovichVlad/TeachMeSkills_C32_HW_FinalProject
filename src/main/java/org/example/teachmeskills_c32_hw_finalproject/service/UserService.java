package org.example.teachmeskills_c32_hw_finalproject.service;

import org.example.teachmeskills_c32_hw_finalproject.model.users.User;
import org.example.teachmeskills_c32_hw_finalproject.repository.SecurityRepository;
import org.example.teachmeskills_c32_hw_finalproject.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    public final SecurityRepository securityRepository;
    private final UserRepository userRepository;
    /*private static final Logger log = LoggerFactory.getLogger(UserService.class);*/

    @Autowired
    public UserService(UserRepository userRepository, SecurityRepository securityRepository) {
        this.userRepository = userRepository;
        this.securityRepository = securityRepository;
    }

    public Boolean createUser(User user) {
        try{
            userRepository.save(user);
        }catch (Exception e){
           /* log.error(e.getMessage());*/
            return false;
        }
        return true;
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> updateUser(User user) {
        return Optional.of(userRepository.save(user));
    }

    public Boolean deleteUser(Long id) {
        userRepository.deleteById(id);
        return !userRepository.existsById(id);
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }
}
