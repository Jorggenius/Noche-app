package com.schoolDays.noche_app.persistenceLayer.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

/**
 * Entidad que representa la tabla usuarios
 */
@Entity
@Table(name = "Usuario")
@Getter
@Setter
@NoArgsConstructor @AllArgsConstructor
public class UsuarioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idUsuario;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String apellido;

    @Column(nullable = false, unique = true, length = 150)
    private String correo;

    @Column(nullable = false)
    private String contrasena;

    private String departamento;

    @ManyToOne
    @JoinColumn(name = "idRol", nullable = false)
    private RolEntity rol;

    @OneToMany(mappedBy = "creadoPor")
    private List<CursoEntity> cursosCreados;
}
