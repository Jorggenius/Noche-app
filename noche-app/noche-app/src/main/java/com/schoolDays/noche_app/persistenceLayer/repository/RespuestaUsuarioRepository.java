package com.schoolDays.noche_app.persistenceLayer.repository;

import com.schoolDays.noche_app.persistenceLayer.entity.RespuestaUsuarioEntity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository

public interface RespuestaUsuarioRepository extends JpaRepository<RespuestaUsuarioEntity, Integer> {

    List<RespuestaUsuarioEntity> findByUsuario_IdUsuario(Integer idUsuario);

    List<RespuestaUsuarioEntity> findByPregunta_IdPregunta(Integer idPregunta);

    List<RespuestaUsuarioEntity> findByUsuario_IdUsuarioAndPregunta_IdPregunta(Integer idUsuario, Integer idPregunta);

    List<RespuestaUsuarioEntity> findByUsuario_IdUsuarioAndCorrectaTrue(Integer idUsuario);

    List<RespuestaUsuarioEntity> findByUsuario_IdUsuarioAndCorrectaFalse(Integer idUsuario);

    List<RespuestaUsuarioEntity> findByFecha(LocalDate fecha);

    List<RespuestaUsuarioEntity> findByEvaluacion_IdEvaluacion(Integer idEvaluacion);

    @Query("SELECT COUNT(ru) FROM RespuestaUsuarioEntity ru WHERE ru.usuario.idUsuario = :usuarioId AND ru.correcta = true")
    long countRespuestasCorrectasByUsuario(@Param("usuarioId") Integer usuarioId);

    @Query("SELECT ru FROM RespuestaUsuarioEntity ru WHERE ru.usuario.idUsuario = :usuarioId AND ru.evaluacion.idEvaluacion = :evaluacionId")
    List<RespuestaUsuarioEntity> findRespuestasUsuarioEnEvaluacion(@Param("usuarioId") Integer usuarioId, @Param("evaluacionId") Integer evaluacionId);

    long countByEvaluacion_IdEvaluacion(Integer idEvaluacion);
}
