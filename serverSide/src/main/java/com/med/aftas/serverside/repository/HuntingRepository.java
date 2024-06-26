package com.med.aftas.serverside.repository;

import com.med.aftas.serverside.model.Hunting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HuntingRepository extends JpaRepository<Hunting, Integer> {
    Optional<Hunting> getHuntingByCompetitionCodeAndFishNameAndUserNum(String competitionCode, String fishName, Integer userNum);

    List<Hunting> getHuntingsByCompetitionCodeAndUserNum(String competitionCode, Integer userNum);

}
