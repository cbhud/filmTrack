package me.cbhud.repository;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import me.cbhud.exception.ReviewException;
import me.cbhud.model.Review;

import java.util.List;

@Dependent
public class ReviewRepository {

    @Inject
    EntityManager em;

    @Transactional
    public Review createReview(Review r) {
        em.persist(r);
        return r;
    }

    @Transactional
    public Review getReviewById(int id) throws ReviewException {
        Review review = em.createNamedQuery(Review.GET_REVIEW_BY_ID, Review.class)
                .setParameter("id", id).getSingleResult();
        if (review == null) {
            throw new ReviewException("No review found with id: " + id);
        }
        return review;
    }

    @Transactional
    public Review updateReview(Review r){
        return em.merge(r);
    }


    @Transactional
    public List<Review> getReviewByUsername(String username) throws ReviewException {
        int id = em.createQuery("SELECT p.id FROM Profile p WHERE p.username = :username", Integer.class)
                .setParameter("username", username).getSingleResult();
        List<Review> reviews = em.createNamedQuery(Review.GET_ALL_REVIEWS_PROFILE, Review.class)
                .setParameter("id", id).getResultList();
        if (reviews == null || reviews.isEmpty()) {
            throw new ReviewException("No review found for user: " + username);
        }
        return reviews;
    }

}