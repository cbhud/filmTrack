package me.cbhud.model.client;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@EqualsAndHashCode
@Entity
public class Forecast implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "forecast_seq_gen")
    private Integer id;
    public String day;
    public String temperature;
    public String wind;
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "weather_id")
    private Weather weather;
}