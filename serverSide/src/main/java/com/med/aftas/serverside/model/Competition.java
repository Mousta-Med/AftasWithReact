package com.med.aftas.serverside.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Competition {

    @Id
    private String code;

    private LocalDate date;

    private LocalTime startTime;

    private LocalTime endTime;

    private Integer numberOfParticipants;

    private String location;

    private Double amount;

    @OneToMany(mappedBy = "competition", fetch = FetchType.LAZY)
    private List<Ranking> rankings;

    @OneToMany(mappedBy = "competition", fetch = FetchType.LAZY)
    private List<Hunting> huntings;
}
