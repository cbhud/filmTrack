package me.cbhud.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import me.cbhud.model.client.Holiday;
import me.cbhud.model.client.HolidayDto;
import me.cbhud.model.client.HolidayType;

import java.util.ArrayList;
import java.util.List;

@Dependent
public class HolidayRepository {

    @Inject
    EntityManager em;

    public List<Holiday> fromDtoList(List<HolidayDto> dtoList) {
        List<Holiday> holidays = new ArrayList<>();
        for (HolidayDto dto : dtoList) {
            Holiday holiday = fromDto(dto);
            holidays.add(holiday);
        }
        return holidays;
    }

    public Holiday fromDto(HolidayDto dto) {
        Holiday holiday = new Holiday();
        holiday.setLocalName(dto.getLocalName());
        holiday.setName(dto.getName());
        holiday.setCountryCode(dto.getCountryCode());
        holiday.setGlobal(dto.isGlobal());
        holiday.setCounties(dto.getCounties());
        holiday.setLaunchYear(dto.getLaunchYear());

        List<HolidayType> typeEntities = new ArrayList<>();
        if (dto.getTypes() != null) {
            for (String type : dto.getTypes()) {
                HolidayType t = new HolidayType();
                t.setType(type);
                t.setHoliday(holiday);
                typeEntities.add(t);
            }
        }

        holiday.setTypes(typeEntities);
        return holiday;
    }

    public boolean existsByNameAndYear(String name, int launchYear) {
        Long count = em.createQuery(
                        "SELECT COUNT(h) FROM Holiday h WHERE h.name = :name AND h.launchYear = :year", Long.class)
                .setParameter("name", name)
                .setParameter("year", launchYear)
                .getSingleResult();

        return count > 0;
    }

    @Transactional
    public void saveIfNotExists(List<Holiday> holidays) {
        for (Holiday holiday : holidays) {
            if (!existsByNameAndYear(holiday.getName(), holiday.getLaunchYear())) {
                em.persist(holiday);
            }
        }
    }
}
