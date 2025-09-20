package com.schoolDays.noche_app.persistenceLayer.repository;

import com.schoolDays.noche_app.persistenceLayer.entity.InscripcionEntity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository

public interface InscripcionRepository extends JpaRepository<InscripcionEntity, Integer> {

    List<InscripcionEntity> findByUsuario_IdUsuario(Integer idUsuario);

    List<InscripcionEntity> findByCurso_IdCurso(Integer idCurso);

    Optional<InscripcionEntity> findByUsuario_IdUsuarioAndCurso_IdCurso(Integer idUsuario, Integer idCurso);

    List<InscripcionEntity> findByEstado(InscripcionEntity.Estado estado);

    List<InscripcionEntity> findByUsuario_IdUsuarioAndEstado(Integer idUsuario, InscripcionEntity.Estado estado);

    List<InscripcionEntity> findByProgresoGreaterThanEqual(BigDecimal progresoMinimo);

    List<InscripcionEntity> findByFechaInscripcionBetween(LocalDate fechaInicio, LocalDate fechaFin);

    List<InscripcionEntity> findAllByOrderByFechaInscripcionDesc();

    long countByCurso_IdCurso(Integer idCurso);

    @Query("SELECT COUNT(i) FROM InscripcionEntity i WHERE i.usuario.idUsuario = :usuarioId AND i.estado = 'COMPLETADO'")
    long countCursosCompletadosByUsuario(@Param("usuarioId") Integer usuarioId);

    boolean existsByUsuario_IdUsuarioAndCurso_IdCurso(Integer idUsuario, Integer idCurso);

    @Query("SELECT i FROM InscripcionEntity i WHERE i.estado = 'EN_PROGRESO' ORDER BY i.progreso DESC")
    List<InscripcionEntity> findInscripcionesEnProgreso();

    @Query("SELECT i FROM InscripcionEntity i WHERE i.curso.idCurso = :cursoId AND i.progreso >= :progresoMinimo")
    List<InscripcionEntity> findByCursoAndProgresoMinimo(@Param("cursoId") Integer cursoId, @Param("progresoMinimo") BigDecimal progresoMinimo);

    @Query("SELECT i FROM InscripcionEntity i WHERE i.usuario.departamento = :departamento")
    List<InscripcionEntity> findByUsuarioDepartamento(@Param("departamento") String departamento);
}
