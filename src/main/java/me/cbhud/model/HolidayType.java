package me.cbhud.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class HolidayType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String type;

    @ManyToOne
    @JoinColumn(name = "holiday_id")
    @JsonIgnore
    private Holiday holiday;
}
