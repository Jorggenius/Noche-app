package com.schoolDays.noche_app.persistenceLayer.repository;

import com.schoolDays.noche_app.persistenceLayer.entity.BadgeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BadgeRepository extends JpaRepository<BadgeEntity, Integer> {

    List<BadgeEntity> findByNombreContainingIgnoreCase(String nombre);

    List<BadgeEntity> findByCriterioContainingIgnoreCase(String criterio);

    boolean existsByNombre(String nombre);

    @Query("SELECT b FROM BadgeEntity b LEFT JOIN b.usuariosBadges ub GROUP BY b ORDER BY COUNT(ub) DESC")
    List<BadgeEntity> findBadgesMasOtorgados();

    @Query("SELECT DISTINCT b FROM BadgeEntity b WHERE SIZE(b.usuariosBadges) > 0")
    List<BadgeEntity> findBadgesConAsignaciones();

    @Query("SELECT b FROM BadgeEntity b WHERE SIZE(b.usuariosBadges) = 0")
    List<BadgeEntity> findBadgesSinAsignaciones();
}
