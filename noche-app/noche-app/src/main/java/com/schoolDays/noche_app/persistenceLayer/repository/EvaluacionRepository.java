package com.schoolDays.noche_app.persistenceLayer.repository;

import com.schoolDays.noche_app.persistenceLayer.entity.EvaluacionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface EvaluacionRepository extends JpaRepository<EvaluacionEntity, Integer> {

    List<EvaluacionEntity> findByModulo_IdModulo(Integer idModulo);

    List<EvaluacionEntity> findByTipo(EvaluacionEntity.TipoEvaluacion tipo);

    List<EvaluacionEntity> findByModulo_IdModuloAndTipo(Integer idModulo, EvaluacionEntity.TipoEvaluacion tipo);

    List<EvaluacionEntity> findByPuntajeBetween(BigDecimal puntajeMin, BigDecimal puntajeMax);

    long countByModulo_IdModulo(Integer idModulo);

    @Query("SELECT e FROM EvaluacionEntity e WHERE e.modulo.curso.idCurso = :cursoId")
    List<EvaluacionEntity> findEvaluacionesByCurso(@Param("cursoId") Integer cursoId);

    @Query("SELECT DISTINCT e FROM EvaluacionEntity e WHERE SIZE(e.preguntas) > 0")
    List<EvaluacionEntity> findEvaluacionesConPreguntas();

    @Query("SELECT e FROM EvaluacionEntity e WHERE e.modulo.curso.idCurso = :cursoId AND e.tipo = 'MCQ'")
    List<EvaluacionEntity> findEvaluacionesMCQByCurso(@Param("cursoId") Integer cursoId);

    List<EvaluacionEntity> findAllByOrderByPuntajeDesc();

    List<EvaluacionEntity> findByTituloContainingIgnoreCase(String titulo);
}
