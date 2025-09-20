package com.schoolDays.noche_app.persistenceLayer.repository;

import com.schoolDays.noche_app.persistenceLayer.entity.PreguntaEntity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface PreguntaRepository extends JpaRepository<PreguntaEntity, Integer> {

    List<PreguntaEntity> findByEvaluacion_IdEvaluacionOrderByOrdenAsc(Integer idEvaluacion);

    List<PreguntaEntity> findByTipoPregunta(PreguntaEntity.TipoPregunta tipoPregunta);

    List<PreguntaEntity> findByEnunciadoContainingIgnoreCase(String texto);

    long countByEvaluacion_IdEvaluacion(Integer idEvaluacion);

    @Query("SELECT p FROM PreguntaEntity p WHERE p.evaluacion.modulo.curso.idCurso = :cursoId ORDER BY p.evaluacion.idEvaluacion, p.orden")
    List<PreguntaEntity> findPreguntasByCurso(@Param("cursoId") Integer cursoId);

    @Query("SELECT DISTINCT p FROM PreguntaEntity p WHERE SIZE(p.opciones) > 0")
    List<PreguntaEntity> findPreguntasConOpciones();

    @Query("SELECT p FROM PreguntaEntity p WHERE p.evaluacion.idEvaluacion = :evaluacionId AND p.tipoPregunta = 'MULTIPLE_CHOICE' ORDER BY p.orden")
    List<PreguntaEntity> findPreguntasMultipleChoiceByEvaluacion(@Param("evaluacionId") Integer evaluacionId);

    @Query("SELECT p FROM PreguntaEntity p WHERE p.evaluacion.idEvaluacion = :evaluacionId AND p.tipoPregunta = 'ABIERTA' ORDER BY p.orden")
    List<PreguntaEntity> findPreguntasAbiertasByEvaluacion(@Param("evaluacionId") Integer evaluacionId);
}
