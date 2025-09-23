package com.schoolDays.noche_app.persistenceLayer.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "UsuarioBadge")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioBadgeEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idUsuarioBadge;

    @Column(nullable = false)
    private LocalDate fechaOtorgado = LocalDate.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idUsuario", nullable = false) // CORREGIDO: era 'usuario_idusuario'
    private UsuarioEntity usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idBadge", nullable = false) // CORREGIDO: era 'badge_idbadge'
    private BadgeEntity badge;
}