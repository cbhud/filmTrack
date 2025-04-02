package me.cbhud.repository;

import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import me.cbhud.exception.ProfileException;
import me.cbhud.model.Profile;
import me.cbhud.model.Review;

import java.util.List;

@Dependent
public class ProfileRepository {
    @Inject
    EntityManager em;

    @Transactional
    public Profile createProfile(Profile profile) {
       return em.merge(profile);
    }

    @Transactional
    public List<Profile> getAllProfiles() {
    List<Profile> profiles = em.createNamedQuery(Profile.GET_ALL_PROFILES, Profile.class).getResultList();
    for (Profile p : profiles) {
        List<Review> reviews = em.createNamedQuery(Review.GET_ALL_REVIEWS_PROFILE, Review.class)
                .setParameter("id", p.getId()).getResultList();
        p.setReviews(reviews);
    }

    return profiles;
    }

    @Transactional
    public List<Profile> getProfileByUsername(String username) throws ProfileException {

        List<Profile> profiles = em.createNamedQuery(Profile.GET_PROFILE_BY_NAME, Profile.class)
                .setParameter("username", username).getResultList();

        if (profiles.isEmpty()) {
            throw new ProfileException("No profile found with username: " + username);
        }

        for (Profile p : profiles) {
            List<Review> reviews = em.createNamedQuery(Review.GET_ALL_REVIEWS_PROFILE, Review.class)
                    .setParameter("id", p.getId()).getResultList();
            p.setReviews(reviews);
        }

        return profiles;
    }
}
