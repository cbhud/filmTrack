package me.cbhud.model;

import lombok.Data;
import jakarta.persistence.*;

import java.util.List;

@Data
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "movie_seq")
    private Integer id;
    private String title;
    private String genre;
    private String director;
    private Integer releaseYear;
    private List<Review> reviews;
    private List<Profile> users;
}