package com.stackeddeck.user;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Builder
@Table(name="users", indexes ={
        @Index(name= "idx_user_email", columnList = "email", unique = true),
        @Index(name= "idx_user_username", columnList = "username", unique = true)
})
public class User {

    @Id @GeneratedValue private UUID id;

    @Column(nullable=false, unique=true) private String email;
    @Column(nullable=false, unique=true) private String username;
    @Column(nullable=false) private String passwordHash;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;


    private String firstName;
    private String lastName;
    private String phone;

    private String addressLine1;
    private String addressLine2;
    private String city;
    private String country;
    private String postalCode;

    private boolean enabled = false;
    private Instant emailVerifiedAt;
    private Instant createdAt = Instant.now();
    private Instant updatedAt = Instant.now();

    public User() {}
}