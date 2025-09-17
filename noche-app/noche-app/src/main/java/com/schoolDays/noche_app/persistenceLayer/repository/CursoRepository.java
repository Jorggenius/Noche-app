package com.schoolDays.noche_app.persistenceLayer.repository;

import com.schoolDays.noche_app.persistenceLayer.entity.CursoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CursoRepository extends JpaRepository<CursoEntity, Integer> {

    List<CursoEntity> findByCreadoPor_IdUsuario(Integer idUsuario);

    List<CursoEntity> findByNivel(CursoEntity.Nivel nivel);

    List<CursoEntity> findByTituloContainingIgnoreCase(String titulo);

    List<CursoEntity> findByFechaCreacionBetween(LocalDate fechaInicio, LocalDate fechaFin);

    List<CursoEntity> findByDuracionLessThanEqual(Integer duracionMaxima);

    List<CursoEntity> findAllByOrderByFechaCreacionDesc();

    @Query("SELECT COUNT(c) FROM CursoEntity c WHERE c.nivel = :nivel")
    long countByNivel(@Param("nivel") CursoEntity.Nivel nivel);

    @Query("SELECT c FROM CursoEntity c LEFT JOIN c.inscripciones i GROUP BY c ORDER BY COUNT(i) DESC")
    List<CursoEntity> findCursosMasPopulares();

    @Query("SELECT DISTINCT c FROM CursoEntity c WHERE SIZE(c.modulos) > 0")
    List<CursoEntity> findCursosConModulos();

    List<CursoEntity> findByCreadoPor_IdUsuarioOrderByFechaCreacionDesc(Integer idUsuario);

    List<CursoEntity> findByNivelOrderByTituloAsc(CursoEntity.Nivel nivel);
}
