package com.schoolDays.noche_app.persistenceLayer.repository;

import com.schoolDays.noche_app.persistenceLayer.entity.RolEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolRepository extends JpaRepository<RolEntity, Integer> {

    Optional<RolEntity> findByNombreRol(String nombreRol);

    boolean existsByNombreRol(String nombreRol);
}
