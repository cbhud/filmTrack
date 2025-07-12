package me.cbhud.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@NamedQuery(name = ProfileMovie.GET_ALL_WATCHED_MOVIES, query = "SELECT pm FROM ProfileMovie pm WHERE pm.profile.id = :profileId")
public class ProfileMovie {
    public static final String GET_ALL_WATCHED_MOVIES = "ProfileMovie.getAllWatchedMovies";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "movie_seq")
    private Long id;

    @ManyToOne
    private Profile profile;

    @ManyToOne
    private Movie movie;

    private LocalDate watchedAt;
    private Integer userRating;
    private String comment;

}
