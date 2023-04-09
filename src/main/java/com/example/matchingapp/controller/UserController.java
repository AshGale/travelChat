package com.example.matchingapp.controller;

import com.example.matchingapp.model.ChatPrompt;
import com.example.matchingapp.model.MatchingUser;
import com.example.matchingapp.model.User;
import com.example.matchingapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(user));
    }

    @GetMapping("/{userId}/matching-users")
    public ResponseEntity<List<MatchingUser>> getMatchingUsers(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getMatchingUsers(userId));
    }

    @PostMapping("/{userId}/chat-prompt")
    public ResponseEntity<ChatPrompt> sendChatPrompt(@PathVariable Long userId, @RequestBody ChatPrompt chatPrompt) {
        return ResponseEntity.ok(userService.sendChatPrompt(userId, chatPrompt));
    }
}
