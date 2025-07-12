package me.cbhud.resources;

import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import me.cbhud.exception.ReviewException;
import me.cbhud.model.Profile;
import me.cbhud.model.Review;
import me.cbhud.repository.ProfileRepository;
import me.cbhud.repository.ReviewRepository;

import java.util.List;

@Path("/review")
@Authenticated
public class ReviewResource {

    @Inject
    private ReviewRepository reviewRepository;

    @Inject
    private ProfileRepository profileRepository;

    @Inject
    SecurityIdentity securityIdentity;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("create")
    public Response createReview(Review r) throws ReviewException {
        String username = securityIdentity.getPrincipal().getName();

        // Get the profile of the currently logged-in user
        Profile profile;
        try {
            profile = profileRepository.getProfileByUsername(username).get(0);
        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid user.").build();
        }

        // Validation
        if (r.getRating() < 1 || r.getRating() > 5) {
            throw new ReviewException("Rating must be between 1 and 5");
        }
        if (r.getReviewText() == null || r.getReviewText().isEmpty() || r.getReviewText().length() > 255) {
            throw new ReviewException("Review text cannot be empty or exceed 255 characters");
        }
        if (r.getMovie() == null) {
            throw new ReviewException("Movie must not be null");
        }

        // Override profile with authenticated one
        r.setProfile(profile);

        Review review = reviewRepository.createReview(r);

        return Response.ok().entity(review).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("get")
    public Response getReviewsByUsername(@QueryParam("username") String username) {
        List<Review> r;
        try {
            r = reviewRepository.getReviewByUsername(username);
        } catch (ReviewException e) {
            return Response.ok().entity(e.getMessage()).build();
        }
        return Response.ok().entity(r).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("get/{id}")
    public Response getReviewById(@PathParam("id") int id) throws ReviewException {
        Review r = reviewRepository.getReviewById(id);
        return Response.ok().entity(r).build();
    }
}
