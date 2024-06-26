package com.med.aftas.serverside.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Ranking {

    @EmbeddedId
    private RankingId id;

    private Integer rank;

    private Integer score;

    @ManyToOne
    @MapsId("userNum")
    @JoinColumn(name = "user_num")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToOne
    @MapsId("competitionCode")
    @JoinColumn(name = "competition_code")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Competition competition;
}
