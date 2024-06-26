package com.med.aftas.serverside.repository;

import com.med.aftas.serverside.model.Level;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LevelRepository extends JpaRepository<Level, Integer> {
    Optional<Level> findTopByCodeLessThanOrderByCodeDesc(Integer code);

    Optional<Level> findTopByCodeGreaterThanOrderByCodeDesc(Integer code);
}
