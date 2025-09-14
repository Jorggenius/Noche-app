package com.schoolDays.noche_app.persistenceLayer.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "UsuarioBadge")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class UsuarioBadgeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idUsuarioBadge;

    @ManyToOne
    @JoinColumn(name = "idUsuario", nullable = false)
    private UsuarioEntity usuario;

    @ManyToOne
    @JoinColumn(name = "idBadge", nullable = false)
    private BadgeEntity badge;

    @Column(nullable = false)
    private LocalDate fechaOtorgado;
}