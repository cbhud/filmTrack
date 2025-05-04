package me.cbhud.model;

import lombok.Data;
import jakarta.persistence.*;

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
//    @OneToMany
//    private List<Review> reviews;


}