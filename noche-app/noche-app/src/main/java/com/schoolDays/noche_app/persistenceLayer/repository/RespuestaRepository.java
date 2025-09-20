package com.schoolDays.noche_app.persistenceLayer.repository;

import com.schoolDays.noche_app.persistenceLayer.entity.RespuestaEntity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.N;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@NoArgsConstructor
@AllArgsConstructor
public interface RespuestaRepository extends JpaRepository<RespuestaEntity, Integer> {

    List<RespuestaEntity> findByPregunta_IdPreguntaOrderByOrdenAsc(Integer idPregunta);

    List<RespuestaEntity> findByPregunta_IdPreguntaAndEsCorrectaTrue(Integer idPregunta);

    List<RespuestaEntity> findByPregunta_IdPreguntaAndEsCorrectaFalse(Integer idPregunta);

    @Query("SELECT COUNT(r) FROM RespuestaEntity r WHERE r.pregunta.idPregunta = :preguntaId AND r.esCorrecta = true")
    long countRespuestasCorrectasByPregunta(@Param("preguntaId") Integer preguntaId);

    List<RespuestaEntity> findByContenidoContainingIgnoreCase(String contenido);

    @Query("SELECT r FROM RespuestaEntity r WHERE r.pregunta.evaluacion.idEvaluacion = :evaluacionId AND r.esCorrecta = true")
    List<RespuestaEntity> findRespuestasCorrectasByEvaluacion(@Param("evaluacionId") Integer evaluacionId);
}
