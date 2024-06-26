package com.med.aftas.serverside.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Level {

    @Id
    private Integer code;

    private String description;

    private Integer points;

    @OneToMany(mappedBy = "level", fetch = FetchType.LAZY)
    private List<Fish> fishes;
}
