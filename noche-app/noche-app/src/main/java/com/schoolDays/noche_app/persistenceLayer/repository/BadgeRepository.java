package com.schoolDays.noche_app.persistenceLayer.repository;

import com.schoolDays.noche_app.persistenceLayer.entity.BadgeEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BadgeRepository extends CrudRepository<BadgeEntity, Long> {
    // Métodos CRUD ya disponibles:
    // save(BadgeEntity entity)
    // saveAll(Iterable<BadgeEntity> entities)
    // findById(Long id)
    // existsById(Long id)
    // findAll()
    // findAllById(Iterable<Long> ids)
    // count()
    // deleteById(Long id)
    // delete(BadgeEntity entity)
    // deleteAll(Iterable<? extends BadgeEntity> entities)
    // deleteAll()
    //:)))) 
}
