package com.schoolDays.noche_app.persistenceLayer.repository;

import com.schoolDays.noche_app.persistenceLayer.entity.InscripcionEntity;
import com.schoolDays.noche_app.persistenceLayer.entity.ModuloEntity;
import org.springframework.data.repository.CrudRepository;

public interface InscripciónRepository extends CrudRepository<InscripcionEntity, Long> {

}
