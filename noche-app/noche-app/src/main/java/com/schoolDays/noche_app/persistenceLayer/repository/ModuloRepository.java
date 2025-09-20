package com.schoolDays.noche_app.persistenceLayer.repository;

import com.schoolDays.noche_app.persistenceLayer.entity.ModuloEntity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface ModuloRepository extends JpaRepository<ModuloEntity, Integer> {

    List<ModuloEntity> findByCurso_IdCursoOrderByOrdenAsc(Integer idCurso);

    List<ModuloEntity> findByTipo(ModuloEntity.TipoModulo tipo);

    List<ModuloEntity> findByCurso_IdCursoAndTipo(Integer idCurso, ModuloEntity.TipoModulo tipo);

    Optional<ModuloEntity> findTopByCurso_IdCursoOrderByOrdenDesc(Integer idCurso);

    Optional<ModuloEntity> findTopByCurso_IdCursoOrderByOrdenAsc(Integer idCurso);

    long countByCurso_IdCurso(Integer idCurso);

    @Query("SELECT DISTINCT m FROM ModuloEntity m WHERE SIZE(m.evaluaciones) > 0")
    List<ModuloEntity> findModulosConEvaluaciones();

    boolean existsByCurso_IdCursoAndOrden(Integer idCurso, Integer orden);

    List<ModuloEntity> findByCurso_IdCursoAndOrdenBetweenOrderByOrdenAsc(Integer idCurso, Integer ordenInicio, Integer ordenFin);

    @Query("SELECT m FROM ModuloEntity m WHERE m.curso.idCurso = :idCurso AND m.tipo = 'QUIZ' ORDER BY m.orden")
    List<ModuloEntity> findQuizzesByCurso(@Param("idCurso") Integer idCurso);
}
