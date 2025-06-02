package me.cbhud.model.client;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import me.cbhud.model.Profile;

import java.io.Serializable;
import java.util.List;
@Data
@NoArgsConstructor
@Entity
@EqualsAndHashCode

@NamedQuery(name = Weather.GET_ALL_WEATHER, query = "SELECT w FROM Weather w WHERE w.city = :city")

public class Weather implements Serializable {
    public static final String GET_ALL_WEATHER = "Weather.getAllWeather";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "weather_seq_gen")
    private Integer id;

    private String city;

    private String temperature;

    private String wind;

    private String description;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Forecast> forecast;

}
