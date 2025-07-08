package me.cbhud.repository;

import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import me.cbhud.exception.MovieException;
import me.cbhud.model.Movie;
import me.cbhud.model.Review;

import java.util.ArrayList;
import java.util.List;

@Dependent
public class MovieRepository {

    @Inject
    EntityManager em;

    @Transactional
    public Movie getMovieByTitle(String title) throws MovieException {

        try {
            Movie movie = em.createNamedQuery(Movie.GET_MOVIE_BY_TITLE, Movie.class)
                    .setParameter("title", title).getSingleResult();
            List<Review> reviews = em.createNamedQuery(Review.GET_ALL_REVIEWS_Movie, Review.class)
                    .setParameter("id", movie.getId()).getResultList();
            movie.setReviews(reviews);
            return movie;
        } catch (NoResultException e) {
            throw new MovieException("No movie found with title: " + title);
        }

    }

    @Transactional
    public Movie createMovie(Movie movie) {
        return em.merge(movie);
    }


    public List<Movie> getAllMovies() {
        List<Movie> movies = em.createQuery("select m from Movie m", Movie.class).getResultList();
        return new ArrayList<>(movies);    }

    public Movie getMovieById(int id) {
        try {
            Movie movie = em.createNamedQuery(Movie.GET_MOVIE_BY_ID, Movie.class)
                    .setParameter("id", id).getSingleResult();
            List<Review> reviews = em.createNamedQuery(Review.GET_ALL_REVIEWS_Movie, Review.class)
                    .setParameter("id", movie.getId()).getResultList();
            movie.setReviews(reviews);
            return movie;
        } catch (NoResultException e) {
            return null; // or throw an exception if preferred
        }
    }
}
