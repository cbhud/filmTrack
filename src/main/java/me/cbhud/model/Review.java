package me.cbhud.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@NamedQuery(name = Review.GET_ALL_REVIEWS_PROFILE, query = "SELECT r from Review r where r.profile.id = :id")
public class Review {
    public static final String GET_ALL_REVIEWS_PROFILE = "Review.getReviewsByProfile";
    //public static final String GET_ALL_REVIEWS_MOVIE = "Review.findAllMovieReviews";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "review_seq")
    private Integer id;
    private Integer rating;
    private String reviewText;
    private Date date;
    @ManyToOne
    @JsonIgnore
    private Profile profile;
//    @ManyToOne
//    private Movie movie;

}