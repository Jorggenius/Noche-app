package com.schoolDays.noche_app.persistenceLayer.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "Badge")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BadgeEntity extends BaseEntity { // AGREGADO: extends BaseEntity

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idBadge;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String criterio;

    @Column(length = 255)
    private String icono;

    @OneToMany(mappedBy = "badge", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UsuarioBadgeEntity> usuariosBadges;
}
