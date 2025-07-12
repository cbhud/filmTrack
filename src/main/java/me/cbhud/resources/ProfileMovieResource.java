package me.cbhud.resources;

import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import me.cbhud.exception.ProfileException;
import me.cbhud.model.Movie;
import me.cbhud.model.Profile;
import me.cbhud.model.ProfileMovie;
import me.cbhud.repository.MovieRepository;
import me.cbhud.repository.ProfileMovieRepository;
import me.cbhud.repository.ProfileRepository;

import java.util.List;

@Path("/watched")
@Authenticated
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProfileMovieResource {

    @Inject
    SecurityIdentity securityIdentity;

    @Inject
    ProfileMovieRepository profileMovieRepository;

    @Inject
    ProfileRepository profileRepository;

    @Inject
    MovieRepository movieRepository;

    @POST
    @Path("add")
    public Response addMovie(@QueryParam("movieId") Integer movieId) {
        String loggedInUsername = securityIdentity.getPrincipal().getName();

        try {
            Profile profile = profileRepository.getProfileByUsername(loggedInUsername).get(0);
            Movie movie = movieRepository.getMovieById(movieId);

            ProfileMovie profileMovie = new ProfileMovie();
            profileMovie.setProfile(profile);
            profileMovie.setMovie(movie);

            ProfileMovie saved = profileMovieRepository.addMovie(profileMovie);
            return Response.ok(saved).build();

        } catch (ProfileException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("remove")
    public Response removeMovie(@QueryParam("movieId") Integer movieId) {
        String loggedInUsername = securityIdentity.getPrincipal().getName();

        try {
            profileMovieRepository.removeMovie(loggedInUsername, movieId);
            return Response.ok("Movie removed from watched list").build();
        } catch (ProfileException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("get")
    public Response getWatchedMovies() {
        String loggedInUsername = securityIdentity.getPrincipal().getName();

        try {
            List<ProfileMovie> watched = profileMovieRepository.getAllWatchedMoviesByUsername(loggedInUsername);
            return Response.ok(watched).build();
        } catch (ProfileException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }
}
