package me.cbhud.repository;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import me.cbhud.exception.ReviewException;
import me.cbhud.model.Review;

@Dependent
public class ReviewRepository {

    @Inject
    EntityManager em;

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
}