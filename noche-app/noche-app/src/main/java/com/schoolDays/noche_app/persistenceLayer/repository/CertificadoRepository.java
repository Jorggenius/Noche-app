package com.schoolDays.noche_app.persistenceLayer.repository;

import com.schoolDays.noche_app.persistenceLayer.entity.CertificadoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CertificadoRepository extends JpaRepository<CertificadoEntity, Integer> {

    List<CertificadoEntity> findByUsuario_IdUsuario(Integer idUsuario);

    List<CertificadoEntity> findByCurso_IdCurso(Integer idCurso);

    Optional<CertificadoEntity> findByUsuario_IdUsuarioAndCurso_IdCurso(Integer idUsuario, Integer idCurso);

    Optional<CertificadoEntity> findByHash(String hash);

    List<CertificadoEntity> findByFechaEmision(LocalDate fechaEmision);

    List<CertificadoEntity> findByFechaEmisionBetween(LocalDate fechaInicio, LocalDate fechaFin);

    List<CertificadoEntity> findAllByOrderByFechaEmisionDesc();

    long countByUsuario_IdUsuario(Integer idUsuario);

    long countByCurso_IdCurso(Integer idCurso);

    boolean existsByUsuario_IdUsuarioAndCurso_IdCurso(Integer idUsuario, Integer idCurso);

    boolean existsByHash(String hash);

    @Query("SELECT c FROM CertificadoEntity c WHERE c.usuario.departamento = :departamento ORDER BY c.fechaEmision DESC")
    List<CertificadoEntity> findByUsuarioDepartamento(@Param("departamento") String departamento);
}
