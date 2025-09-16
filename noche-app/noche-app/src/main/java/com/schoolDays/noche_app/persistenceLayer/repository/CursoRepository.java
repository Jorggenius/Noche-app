package com.schoolDays.noche_app.persistenceLayer.repository;



import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CursoRepository extends CrudRepository<CursoEntity, Long> {

}
