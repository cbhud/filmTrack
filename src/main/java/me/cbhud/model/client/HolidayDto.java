package me.cbhud.model.client;

import lombok.Data;

import java.util.List;

@Data
public class HolidayDto {
    private String localName;
    private String name;
    private String countryCode;
    private boolean global;
    private List<String> counties;
    private int launchYear;
    private List<String> types;
}
