package com.med.aftas.serverside.repository;

import com.med.aftas.serverside.model.Ranking;
import com.med.aftas.serverside.model.RankingId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RankingRepository extends JpaRepository<Ranking, RankingId> {
    List<Ranking> findRankingsByCompetitionCode(String code);

    List<Ranking> findRankingsByCompetitionCodeOrderByScoreDesc(String code);

}
