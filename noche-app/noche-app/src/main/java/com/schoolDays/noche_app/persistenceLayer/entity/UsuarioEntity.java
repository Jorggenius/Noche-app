package com.schoolDays.noche_app.persistenceLayer.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Entidad que representa los usuarios del sistema
 */
@Entity
@Table(name = "usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idUsuario;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String apellido;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = false)
    private String contrasena;

    @Column(length = 100)
    private String departamento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idRol", nullable = false)
    private RolEntity rol;

    @OneToMany(mappedBy = "creadoPor", cascade = CascadeType.ALL)
    private List<CursoEntity> cursosCreados;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<InscripcionEntity> inscripciones;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<CertificadoEntity> certificados;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<UsuarioBadgeEntity> badges;
}
