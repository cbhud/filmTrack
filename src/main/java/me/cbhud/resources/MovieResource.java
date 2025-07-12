package me.cbhud.resources;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import me.cbhud.exception.MovieException;
import me.cbhud.model.Movie;
import me.cbhud.dto.client.MovieDto;
import me.cbhud.repository.MovieRepository;
import me.cbhud.restclient.MovieClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.ArrayList;
import java.util.List;

@Path("/movie")
public class MovieResource {

    @Inject
    @RestClient
    MovieClient movieClient;

    @Inject
    MovieRepository movieRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/get")
    public Response getMovie(@QueryParam("title") String title) throws MovieException {
        if (title == null || title.isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Missing title").build();
        }

        Movie m;
        try {
            m = movieRepository.getMovieByTitle(title);
        } catch (MovieException e) {
            m = null;
        }

        if (m != null) {
            return Response.ok(m).build();
        } else {

            MovieDto md = getMovieFromApi(title);
            if (md == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("Movie not found").build();
            }

            if(movieRepository.checkIfMovieExists(md.getImdbID())){
                m = movieRepository.getMovieByIMDBId(md.getImdbID());
                    return Response.ok(m).build();
            }

            m = new Movie();
            m.setTitle(md.getTitle());
            m.setGenre(md.getGenre());
            m.setDirector(md.getDirector());
            m.setWriter(md.getWriter());
            m.setActors(md.getActors());
            m.setReleaseYear(md.getReleased());
            m.setPlot(md.getPlot());
            m.setImdbId(md.getImdbID());
            m.setImdbRating(md.getImdbRating());
            m.setRuntime(md.getRuntime());
            m.setRated(md.getRated());
            m.setPosterUrl(md.getPoster());
            m.setType(md.getType());
            m.setReviews(new ArrayList<>());



            movieRepository.createMovie(m);

            return Response.ok(m).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getFromApi")
    public MovieDto getMovieFromApi(@QueryParam("title") String title) {
        String API_KEY = "620117e5";
        MovieDto movieDto = movieClient.getMovieByTitle(title, API_KEY);
        return movieDto;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getFromApiById")
    public MovieDto getMovieFromApiById(@QueryParam("imdbId") String imdbId) {
        String API_KEY = "620117e5";
        MovieDto movieDto = movieClient.getMovieById(imdbId, API_KEY);
        return movieDto;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getAll")
    public List<Movie> getAllMovies() {
        List<Movie> movies = movieRepository.getAllMovies();
        if (movies == null || movies.isEmpty()) {
            return new ArrayList<>();
        }
        for (Movie m : movies) {
            m.setReviews(null);
        }
        return movies;
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response getMovieById(@PathParam("id") int id) {
    Movie movie = movieRepository.getMovieById(id);
    return movie != null ? Response.ok(movie).build() : Response.status(Response.Status.NOT_FOUND).build();
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/search")
    public Response searchMovies(@QueryParam("hint") String hint) throws MovieException {
        if (hint == null || hint.isBlank()) {
            return Response.ok(List.of()).build();
        }
        List<Movie> results = movieRepository.findByNameHint(hint);
        if (results.isEmpty()){
            Movie m = getMovieForSearch(hint);
            m.setReviews(null);
            if (m != null) {
                results.add(m);
            }
        }
        return Response.ok(results).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/get")
    public Movie getMovieForSearch(@QueryParam("title") String title) throws MovieException {
        if (title == null || title.isBlank()) {
            return null;
        }

            Movie m;
            MovieDto md;
            if (title.startsWith("tt")){
            md = getMovieFromApiById(title);
            }else {
                md = getMovieFromApi(title);
            }
            if (md.getImdbID() == null) {
                return null;
            }

            m = new Movie();
            m.setTitle(md.getTitle());
            m.setGenre(md.getGenre());
            m.setDirector(md.getDirector());
            m.setWriter(md.getWriter());
            m.setActors(md.getActors());
            m.setReleaseYear(md.getReleased());
            m.setPlot(md.getPlot());
            m.setImdbId(md.getImdbID());
            m.setImdbRating(md.getImdbRating());
            m.setRuntime(md.getRuntime());
            m.setRated(md.getRated());
            m.setPosterUrl(md.getPoster());
            m.setType(md.getType());
            m.setReviews(new ArrayList<>());

            if (m!= null){
            movieRepository.createMovie(m);

            return movieRepository.getMovieByTitle(m.getTitle());
            } else {
                return null;
        }
    }

}




