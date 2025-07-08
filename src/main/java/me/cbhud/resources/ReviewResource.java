package me.cbhud.resources;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import me.cbhud.exception.ReviewException;
import me.cbhud.model.Review;
import me.cbhud.repository.ReviewRepository;

import java.util.List;

@Path("/review")
@Authenticated
public class ReviewResource {
    @Inject
    private ReviewRepository reviewRepository;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("create")
    public Response createReview(Review r) throws ReviewException {
        if (r.getProfile() == null) {
            throw new ReviewException("Profile must not be null");
        }

        if (r.getRating() < 1 || r.getRating() > 5) {
            throw new ReviewException("Rating must be between 1 and 5");
        }
        if (r.getReviewText() == null || r.getReviewText().isEmpty() || r.getReviewText().length() > 255) {
            throw new ReviewException("Review text cannot be empty or exceed 255 characters");
        }

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
        Review r;
        r = reviewRepository.getReviewById(id);
        return Response.ok().entity(r).build();
    }






}
