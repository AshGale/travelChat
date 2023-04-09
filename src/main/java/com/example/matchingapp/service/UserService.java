package com.example.matchingapp.service;

import com.example.matchingapp.model.ChatPrompt;
import com.example.matchingapp.model.MatchingUser;
import com.example.matchingapp.model.User;
import com.example.matchingapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public List<MatchingUser> getMatchingUsers(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // Implement your matching logic here
            // ...
        }
        throw new RuntimeException("User not found with ID: " + userId);
    }

    public ChatPrompt sendChatPrompt(Long userId, ChatPrompt chatPrompt) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // Implement your chat prompt logic here
            // ...
        }
        throw new RuntimeException("User not found with ID: " + userId);
    }
}
