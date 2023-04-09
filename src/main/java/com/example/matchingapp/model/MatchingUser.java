package com.example.matchingapp.model;

import lombok.Data;

@Data
public class MatchingUser {

    private Long userId;
    private String firstName;
    private String lastName;
    private String matchedInterest;
    private int numberOfSharedInterests;
    private String gender;
    private String location;

}
