package com.schoolDays.noche_app.persistenceLayer.repository;

import com.schoolDays.noche_app.persistenceLayer.entity.UsuarioBadgeEntity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@NoArgsConstructor
@AllArgsConstructor
public interface UsuarioBadgeRepository extends JpaRepository<UsuarioBadgeEntity, Integer> {

    List<UsuarioBadgeEntity> findByUsuario_IdUsuario(Integer idUsuario);

    List<UsuarioBadgeEntity> findByBadge_IdBadge(Integer idBadge);

    List<UsuarioBadgeEntity> findByFechaOtorgado(LocalDate fecha);

    List<UsuarioBadgeEntity> findByFechaOtorgadoBetween(LocalDate fechaInicio, LocalDate fechaFin);

    List<UsuarioBadgeEntity> findAllByOrderByFechaOtorgadoDesc();

    boolean existsByUsuario_IdUsuarioAndBadge_IdBadge(Integer idUsuario, Integer idBadge);

    long countByUsuario_IdUsuario(Integer idUsuario);

    long countByBadge_IdBadge(Integer idBadge);

    @Query("SELECT ub.usuario.idUsuario, COUNT(ub) FROM UsuarioBadgeEntity ub GROUP BY ub.usuario.idUsuario ORDER BY COUNT(ub) DESC")
    List<Object[]> findUsuariosConMasBadges();

    @Query("SELECT ub FROM UsuarioBadgeEntity ub WHERE ub.usuario.departamento = :departamento ORDER BY ub.fechaOtorgado DESC")
    List<UsuarioBadgeEntity> findByUsuarioDepartamento(@Param("departamento") String departamento);
}