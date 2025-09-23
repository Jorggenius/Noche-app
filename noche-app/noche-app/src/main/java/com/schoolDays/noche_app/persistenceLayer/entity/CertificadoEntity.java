package com.schoolDays.noche_app.persistenceLayer.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "Certificado")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CertificadoEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCertificado;

    @Column(nullable = false)
    private LocalDate fechaEmision = LocalDate.now();

    @Column(nullable = false, unique = true, length = 255)
    private String hash;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idUsuario", nullable = false) // CORREGIDO: era 'usuario_idusuario'
    private UsuarioEntity usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idCurso", nullable = false) // CORREGIDO: era 'curso_idcurso'
    private CursoEntity curso;
}
