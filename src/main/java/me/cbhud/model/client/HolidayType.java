package me.cbhud.model.client;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class HolidayType {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "holiday_type_seq")
    private Long id;

    private String type;

    @ManyToOne
    @JoinColumn(name = "holiday_id")
    @JsonIgnore
    private Holiday holiday;
}
