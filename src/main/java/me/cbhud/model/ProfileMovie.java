package me.cbhud.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ProfileMovie {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "profilemovie_seq")
    private Integer id;

    @ManyToOne(cascade =  CascadeType.ALL)
    Profile profile;

    @ManyToOne(cascade =  CascadeType.ALL)
    Movie movie;
}
