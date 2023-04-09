package com.example.matchingapp.model;

import lombok.Data;

@Data
public class ChatPrompt {

    private Long recipientUserId;
    private String matchedInterest;
    private String message;

}
