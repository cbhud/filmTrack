package me.cbhud.model;

import lombok.Data;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Data

@NamedQuery(name = Movie.GET_MOVIE_BY_TITLE, query = "SELECT m FROM Movie m WHERE m.title = :title")
@NamedQuery(name = Movie.GET_MOVIE_BY_ID, query = "SELECT m FROM Movie m WHERE m.id = :id")
public class Movie {
    public static final String GET_MOVIE_BY_TITLE = "Movie.getMovieByTitle";
    public static final String GET_MOVIE_BY_ID = "Movie.getMovieById";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "movie_seq")
    private Integer id;
    private String title;
    private String genre;
    private String director;
    private String writer;
    private String actors;
    private String releaseYear;
    private String plot;
    private String imdbId;
    private String imdbRating;
    private String runtime;
    private String rated;
    private String posterUrl;
    private String type;
    @OneToMany
    @JoinColumn(name = "movie_id")
    private List<Review> reviews;
    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProfileMovie> viewers;


}