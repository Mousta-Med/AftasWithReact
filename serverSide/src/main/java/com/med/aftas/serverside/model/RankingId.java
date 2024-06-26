package com.med.aftas.serverside.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
@Builder
public class RankingId implements Serializable {

    @Column(name = "user_num")
    private Integer userNum;

    @Column(name = "competition_code")
    private String competitionCode;

}
