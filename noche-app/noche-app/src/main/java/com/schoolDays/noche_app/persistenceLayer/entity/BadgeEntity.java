package com.schoolDays.noche_app.persistenceLayer.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BadgeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String criterio;

    @Column(nullable = false)
    private String icono;

    @OneToOne
    @JoinColumn(name = "idusuario", nullable = false)
    private UsuarioEntity usuario;
}
