package me.cbhud.resources;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import jakarta.ws.rs.core.Response;
import me.cbhud.model.client.Weather;
import me.cbhud.repository.WeatherRepository;
import me.cbhud.restclient.WeatherClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@Path("/getWeather")
public class WeatherResource {

    @Inject
    WeatherRepository weatherRepository;

    @Inject
    @RestClient
    WeatherClient weatherClient;


    @GET
    @Path("/getForecast")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAndSaveForecast(@QueryParam("city") String city) {
        Weather w = weatherClient.getForecast(city);
        w.setCity(city);
        w = weatherRepository.save(w);
        return Response.ok(w).build();
    }
}
