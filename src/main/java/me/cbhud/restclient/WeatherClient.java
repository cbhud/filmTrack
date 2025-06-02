package me.cbhud.restclient;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import me.cbhud.model.client.Weather;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/weather/")
@RegisterRestClient(configKey = "weather-api")
public interface WeatherClient {

    @GET
    @Path("{city}")
    Weather getForecast(@PathParam("city") String city);

}
