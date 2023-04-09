// File 6: User.java

package com.example.matchingapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String gender;

    private int age;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "interests_discussion_points", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "discussion_points")
    private List<String> discussionPoints;
}
