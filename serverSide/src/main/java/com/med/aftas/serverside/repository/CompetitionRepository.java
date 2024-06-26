package com.med.aftas.serverside.repository;

import com.med.aftas.serverside.model.Competition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CompetitionRepository extends JpaRepository<Competition, String> {

    List<Competition> findCompetitionByDate(LocalDate date);

    Page<Competition> findCompetitionsByDate(LocalDate date, Pageable pageable);

    Page<Competition> findCompetitionsByDateBefore(LocalDate date, Pageable pageable);

    Page<Competition> findCompetitionsByDateAfter(LocalDate date, Pageable pageable);
}
