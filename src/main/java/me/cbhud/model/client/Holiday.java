package me.cbhud.model.client;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity

public class Holiday {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "holiday_seq")
    private Integer id;
    private String localName;
    private String name;
    private String countryCode;
    private boolean global;
    @ElementCollection
    private List<String> counties;
    private int launchYear;
    @OneToMany(mappedBy = "holiday", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HolidayType> types;

}
