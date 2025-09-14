package com.schoolDays.noche_app.persistenceLayer.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

/**
 * Entidad que representa la tabla badges
 */
@Entity
@Table(name = "Badge")
@Getter
@Setter
@NoArgsConstructor @AllArgsConstructor
public class BadgeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idBadge;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Lob
    @Column(nullable = false)
    private String criterio;

    private String icono;

    @OneToMany(mappedBy = "badge")
    private List<UsuarioBadgeEntity> usuarios;
}
