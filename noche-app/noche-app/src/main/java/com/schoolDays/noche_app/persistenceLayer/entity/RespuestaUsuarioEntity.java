package com.schoolDays.noche_app.persistenceLayer.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "RespuestaUsuario")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class RespuestaUsuarioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idRespuestaUsuario;

    @ManyToOne
    @JoinColumn(name = "idUsuario", nullable = false)
    private UsuarioEntity usuario;

    @ManyToOne
    @JoinColumn(name = "idPregunta", nullable = false)
    private PreguntaEntity pregunta;

    @ManyToOne
    @JoinColumn(name = "idRespuesta")
    private RespuestaEntity respuesta;

    private String respuestaTexto;
    private Boolean correcta;

    @Column(nullable = false)
    private LocalDate fecha;
}