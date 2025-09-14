package com.schoolDays.noche_app.persistenceLayer.repository;


import com.schoolDays.noche_app.persistenceLayer.entity.ModuloEntity;
import com.schoolDays.noche_app.persistenceLayer.entity.UsuarioEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends CrudRepository<UsuarioEntity, Long> {

}