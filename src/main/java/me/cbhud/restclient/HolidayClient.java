package me.cbhud.restclient;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import java.util.List;

import me.cbhud.model.client.HolidayDto;
import me.cbhud.model.client.Country;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/api/v3")
@RegisterRestClient(configKey = "holiday-api")
public interface HolidayClient {

    @GET
    @Path("/AvailableCountries")
    List<Country> getAvailableCountries();

    @GET
    @Path("/NextPublicHolidays/{countryCode}")
    List<HolidayDto> getNextPublicHolidays(@PathParam("countryCode") String countryCode);
}
