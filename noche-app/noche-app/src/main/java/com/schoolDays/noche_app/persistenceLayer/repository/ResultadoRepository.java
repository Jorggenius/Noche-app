package com.schoolDays.noche_app.persistenceLayer.repository;

import com.schoolDays.noche_app.persistenceLayer.entity.ResultadoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ResultadoRepository extends JpaRepository<ResultadoEntity, Integer> {

    List<ResultadoEntity> findByUsuario_IdUsuario(Integer idUsuario);
    List<ResultadoEntity> findByEvaluacion_IdEvaluacion(Integer idEvaluacion);
    Optional<ResultadoEntity> findByUsuario_IdUsuarioAndEvaluacion_IdEvaluacion(Integer idUsuario, Integer idEvaluacion);
    boolean existsByUsuario_IdUsuarioAndEvaluacion_IdEvaluacion(Integer idUsuario, Integer idEvaluacion);
    List<ResultadoEntity> findByPuntajeBetween(BigDecimal puntajeMin, BigDecimal puntajeMax);
    List<ResultadoEntity> findByFechaRealizacion(LocalDate fecha);
    List<ResultadoEntity> findByFechaRealizacionBetween(LocalDate fechaInicio, LocalDate fechaFin);
    List<ResultadoEntity> findAllByOrderByPuntajeDesc();
    List<ResultadoEntity> findByEvaluacion_IdEvaluacionOrderByPuntajeDesc(Integer idEvaluacion);
    long countByEvaluacion_IdEvaluacion(Integer idEvaluacion);

    @Query("SELECT AVG(r.puntaje) FROM ResultadoEntity r WHERE r.evaluacion.idEvaluacion = :evaluacionId")
    BigDecimal findPromedioPuntajeByEvaluacion(@Param("evaluacionId") Integer evaluacionId);

    @Query("SELECT r FROM ResultadoEntity r WHERE r.puntaje >= :puntajeMinimo")
    List<ResultadoEntity> findResultadosAprobatorios(@Param("puntajeMinimo") BigDecimal puntajeMinimo);

    @Query("SELECT r FROM ResultadoEntity r WHERE r.evaluacion.modulo.curso.idCurso = :cursoId ORDER BY r.puntaje DESC")
    List<ResultadoEntity> findMejoresResultadosByCurso(@Param("cursoId") Integer cursoId);

    @Query("SELECT r FROM ResultadoEntity r WHERE r.usuario.idUsuario = :usuarioId AND r.evaluacion.modulo.curso.idCurso = :cursoId")
    List<ResultadoEntity> findResultadosUsuarioEnCurso(@Param("usuarioId") Integer usuarioId, @Param("cursoId") Integer cursoId);

    @Query("SELECT AVG(r.puntaje) FROM ResultadoEntity r WHERE r.usuario.idUsuario = :usuarioId")
    BigDecimal findPromedioPuntajeByUsuario(@Param("usuarioId") Integer usuarioId);
}
