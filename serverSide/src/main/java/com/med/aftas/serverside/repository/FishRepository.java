package com.med.aftas.serverside.repository;

import com.med.aftas.serverside.model.Fish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FishRepository extends JpaRepository<Fish, String> {
}
