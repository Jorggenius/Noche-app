package com.schoolDays.noche_app.persistenceLayer.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Entidad que representa la tabla certificados
 */
@Entity
@Table(name = "Certificado")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CertificadoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCertificado;

    @ManyToOne
    @JoinColumn(name = "idUsuario", nullable = false)
    private UsuarioEntity usuario;

    @ManyToOne
    @JoinColumn(name = "idCurso", nullable = false)
    private CursoEntity curso;

    @Column(nullable = false)
    private LocalDate fechaEmision;

    @Column(nullable = false, unique = true)
    private String hash;
}