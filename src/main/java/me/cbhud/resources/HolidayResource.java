package me.cbhud.resources;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import me.cbhud.model.Holiday;
import me.cbhud.model.HolidayType;
import me.cbhud.model.client.Country;
import me.cbhud.model.client.HolidayDto;
import me.cbhud.repository.HolidayRepository;
import me.cbhud.restclient.HolidayClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HolidayResource {

    @Inject
    @RestClient
    HolidayClient holidayClient;

    @Inject
    HolidayRepository holidayRepository;

    @GET
    @Path("/getAvailableCountries")
    public List<Country> getCountries() {
        return holidayClient.getAvailableCountries();
    }

    @GET
    @Path("/getHolidays/{countryCode}")
    public List<Holiday> getHolidays(@PathParam("countryCode") String countryCode) {
        List<HolidayDto> holidayDtos = holidayClient.getNextPublicHolidays(countryCode);
        List<Holiday> result = new ArrayList<>();

        for (HolidayDto dto : holidayDtos) {
            boolean exists = holidayRepository
                    .findByDateAndCountryCode(dto.getDate(), dto.getCountryCode())
                    .isPresent();

            if (!exists) {
                Holiday holiday = new Holiday();
                holiday.setDate(dto.getDate());
                holiday.setLocalName(dto.getLocalName());
                holiday.setName(dto.getName());
                holiday.setCountryCode(dto.getCountryCode());
                holiday.setGlobal(dto.isGlobal());
                holiday.setLaunchYear(dto.getLaunchYear());
                holiday.setCounties(dto.getCounties());

                List<HolidayType> types = dto.getTypes().stream().map(typeStr -> {
                    HolidayType type = new HolidayType();
                    type.setType(typeStr);
                    type.setHoliday(holiday);
                    return type;
                }).collect(Collectors.toList());

                holiday.setTypes(types);

                holidayRepository.persist(holiday);
                result.add(holiday);
            } else {
                holidayRepository.findByDateAndCountryCode(dto.getDate(), dto.getCountryCode())
                        .ifPresent(result::add);
            }
        }

        return result;
    }
}