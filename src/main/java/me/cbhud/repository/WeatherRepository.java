package me.cbhud.repository;

import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import me.cbhud.model.client.Weather;

@Dependent
public class WeatherRepository {

    @Inject
    EntityManager em;

    @Transactional
    public Weather checkIfExists(String city) {
        return em.createNamedQuery(Weather.GET_ALL_WEATHER, Weather.class)
                .setParameter("city", city)
                .getSingleResult();
    }

    @Transactional
    public Weather save(Weather weather) {
        if (checkIfExists(weather.getCity()) == null) {
            return em.merge(weather);
        } else {
            return weather;
        }
    }
}