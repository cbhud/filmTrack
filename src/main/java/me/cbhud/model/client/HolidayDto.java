package me.cbhud.model.client;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class HolidayDto {
    public LocalDate date;
    public String localName;
    public String name;
    public String countryCode;
    public boolean global;
    public List<String> counties;
    public Integer launchYear;
    public List<String> types;
}