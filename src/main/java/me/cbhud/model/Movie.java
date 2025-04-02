package me.cbhud.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import jakarta.persistence.*;
import java.util.List;

@Data
//@Entity
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "movie_seq")
    private Integer id;
    private String title;
    private String genre;
    private String director;
    private Integer releaseYear;
//    @OneToMany
//    @JoinColumn(name = "review_id")
//    @JsonIgnore
//    private List<Review> reviews;
//    @ManyToMany(mappedBy = "watchedMovies")
//    @JsonIgnore
//    private List<Profile> users;
}