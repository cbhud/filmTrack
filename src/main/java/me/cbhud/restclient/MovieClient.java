package me.cbhud.restclient;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.QueryParam;
import me.cbhud.dto.client.MovieDto;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "movie-api")
public interface MovieClient {

    @GET
    MovieDto getMovieByTitle(@QueryParam("t") String title, @QueryParam("apikey") String apiKey);

//    @GET
//    @Path("?apikey={apikey}&i={imdbID}")
//    MovieDto getMovieByImdbID(@QueryParam("apikey") String apikey, @QueryParam("imdbID") String imdbID);


}
