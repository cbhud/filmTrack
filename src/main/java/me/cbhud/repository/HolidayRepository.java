package me.cbhud.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import me.cbhud.model.Holiday;

import java.time.LocalDate;
import java.util.Optional;

@ApplicationScoped
public class HolidayRepository {

    @Inject
    EntityManager em;

    @Transactional
    public void persist(Holiday holiday) {
        em.persist(holiday);
    }

    @Transactional
    public Optional<Holiday> findByDateAndCountryCode(LocalDate date, String countryCode) {
        TypedQuery<Holiday> query = em.createQuery(
                "SELECT h FROM Holiday h WHERE h.date = :date AND h.countryCode = :countryCode",
                Holiday.class
        );
        query.setParameter("date", date);
        query.setParameter("countryCode", countryCode);
        return query.getResultStream().findFirst();
    }
}
