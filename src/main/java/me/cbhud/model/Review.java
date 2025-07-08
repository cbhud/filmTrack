package me.cbhud.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;
@Entity
@Data

@NamedQuery(name = Review.GET_ALL_REVIEWS_PROFILE, query = "SELECT r from Review r where r.profile.id = :id")
@NamedQuery(name = Review.GET_REVIEW_BY_ID, query = "SELECT r FROM Review r WHERE r.id = :id")
@NamedQuery(name = Review.GET_ALL_REVIEWS_Movie, query = "SELECT r from Review r where r.movie.id = :id")


public class Review {
    public static final String GET_ALL_REVIEWS_PROFILE = "Review.getReviewsByProfile";
    public static final String GET_ALL_REVIEWS_Movie = "Review.getReviewsByMovie";

    public static final String GET_REVIEW_BY_ID = "Review.getReviewById";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "review_seq")
    private Integer id;
    private Integer rating;
    private String reviewText;
    private Date date;
    @ManyToOne
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Profile profile;
    @ManyToOne
    private Movie movie;

}