package me.cbhud.model;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data

@NamedQuery(name = Profile.GET_ALL_PROFILES, query = "SELECT p FROM Profile p")
@NamedQuery(name = Profile.GET_PROFILE_BY_NAME, query = "SELECT p FROM Profile p WHERE p.username = :username")
@NamedQuery(name = Profile.GET_PROFILE_BY_ID, query = "SELECT p FROM Profile p WHERE p.id = :id")
@NamedQuery(name = Profile.GET_PROFILE_BY_EMAIL, query = "SELECT p FROM Profile p WHERE p.email = :email")


public class Profile {
    public static final String GET_ALL_PROFILES = "Profile.getAllProfiles";
    public static final String GET_PROFILE_BY_NAME = "Profile.getProfileByName";
    public static final String GET_PROFILE_BY_ID = "Profile.getProfileById";
    public static final String GET_PROFILE_BY_EMAIL = "Profile.getProfileByEmail";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "profile_seq")
    private Integer id;
    private String email;
    private String fullName;
    private String username;
    private String passwordHash;
    private String avatarUrl;
    private String role; // "user" or "admin"
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    private List<Review> reviews;
    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProfileMovie> watchedMovies;

}