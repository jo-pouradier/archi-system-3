package com.sp.service;

import com.sp.model.User;
import com.sp.repository.UserRepository;
import fr.dtos.common.user.UserRegisterDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
//    @Autowired
//    private CardService cardService;
    private User addUser(User user) {
        user = userRepository.save(user);
//        cardService.newUserSet(user);
        return user;
    }

    public boolean remUser(UUID uuid) {
        return false;
    }

    public User getUser(UUID uuid) {
        return userRepository.findById(uuid).orElse(null);
    }

    public User getUser(String username) {
        return userRepository.findByName(username);
    }

    public UUID existUser(String username, String password) {
        User user = userRepository.findByName(username);
        if (user != null) {
            return user.getPassword().equals(password) ? user.getUUID() : null;
        }
        return null;
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean debit(UUID uuid, float value) {
        User user = getUser(uuid);
        float balance = user.getBalance();
        if (balance-value>=0.0){
            user.setBalance(balance-value);
            return true;
        }
        return false;
    }

    public boolean depot(UUID uuid, float value){
        float balance = getUser(uuid).getBalance();
        getUser(uuid).setBalance(balance+value);
        return getUser(uuid).getBalance()==balance+value;
    }

    public User createUser(UserRegisterDTO userRegisterDTO) {
        User user = new User(userRegisterDTO.getName(), userRegisterDTO.getPassword(), userRegisterDTO.getEmail());
        return addUser(user);
    }
}



