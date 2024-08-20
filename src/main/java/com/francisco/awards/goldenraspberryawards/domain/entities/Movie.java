package com.francisco.awards.goldenraspberryawards.domain.entities;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int movieYear;
    private String title;
    private String studios;
    private String producers;
    private boolean winner;
}