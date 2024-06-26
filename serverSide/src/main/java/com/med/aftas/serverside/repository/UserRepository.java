package com.med.aftas.serverside.repository;

import com.med.aftas.serverside.enums.Role;
import com.med.aftas.serverside.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String username);
    List<User> findUserByNameContainingIgnoreCaseOrFamilyNameContainingIgnoreCase(String name, String familyName);
    List<User> findByRoleIsNot(Role role);
}
