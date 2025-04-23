package me.cbhud.model;

import lombok.Data;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Data
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "movie_seq")
    private Integer id;
    private String title;
    private String genre;
    private String director;
    private Integer releaseYear;
    @OneToMany
    private List<Review> reviews;
    @ManyToMany

    private List<Profile> users;
}