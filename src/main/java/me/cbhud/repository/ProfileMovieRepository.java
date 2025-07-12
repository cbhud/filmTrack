package me.cbhud.repository;

import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import me.cbhud.exception.ProfileException;
import me.cbhud.model.Profile;
import me.cbhud.model.ProfileMovie;

import java.util.List;

@Dependent
public class ProfileMovieRepository {

    @Inject
    EntityManager em;

    @Transactional
    public ProfileMovie addMovie(ProfileMovie profileMovie) {
        return em.merge(profileMovie);
    }

    @Transactional
    public void removeMovie(String username, Integer movieId) throws ProfileException {
        Profile profile = getProfileByUsername(username);
        List<ProfileMovie> watched = em.createQuery(
                        "SELECT pm FROM ProfileMovie pm WHERE pm.profile.id = :profileId AND pm.movie.id = :movieId", ProfileMovie.class)
                .setParameter("profileId", profile.getId())
                .setParameter("movieId", movieId)
                .getResultList();

        for (ProfileMovie pm : watched) {
            em.remove(pm);
        }
    }

    @Transactional
    public List<ProfileMovie> getAllWatchedMoviesByUsername(String username) throws ProfileException {
        Profile profile = getProfileByUsername(username);
        return em.createNamedQuery(ProfileMovie.GET_ALL_WATCHED_MOVIES, ProfileMovie.class)
                .setParameter("profileId", profile.getId())
                .getResultList();
    }

    @Transactional
    public Profile getProfileByUsername(String username) throws ProfileException {
        List<Profile> profiles = em.createNamedQuery(Profile.GET_PROFILE_BY_NAME, Profile.class)
                .setParameter("username", username)
                .getResultList();
        if (profiles.isEmpty()) {
            throw new ProfileException("No profile found with username: " + username);
        }
        return profiles.get(0);
    }
}
