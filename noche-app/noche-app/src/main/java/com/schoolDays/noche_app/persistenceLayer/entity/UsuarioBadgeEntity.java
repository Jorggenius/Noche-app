package com.schoolDays.noche_app.persistenceLayer.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Entidad que representa la relación muchos a muchos entre usuarios y badges
 */
@Entity
@Table(name = "usuario_badge")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioBadgeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idUsuarioBadge;

    @Column(nullable = false)
    private LocalDate fechaOtorgado = LocalDate.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_idusuario", nullable = false)
    private UsuarioEntity usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "badge_idbadge", nullable = false)
    private BadgeEntity badge;
}