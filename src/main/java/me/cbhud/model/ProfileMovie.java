package me.cbhud.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
public class ProfileMovie {
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
