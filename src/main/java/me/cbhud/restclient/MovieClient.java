package me.cbhud.restclient;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import me.cbhud.dto.client.MovieDto;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "movie-api")
public interface MovieClient {

    @GET
    MovieDto getMovieByTitle(@QueryParam("t") String title, @QueryParam("apikey") String apiKey);

    @GET
    MovieDto getMovieById(@QueryParam("i") String imdbId, @QueryParam("apikey") String apiKey);

}
