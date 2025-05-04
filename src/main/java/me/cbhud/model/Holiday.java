package me.cbhud.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
public class Holiday {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDate date;
    private String localName;
    private String name;
    private String countryCode;
    private boolean global;
    private Integer launchYear;

    //@ElementCollection(fetch = FetchType.EAGER)
    private List<String> counties;

    @OneToMany(mappedBy = "holiday", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<HolidayType> types;
}