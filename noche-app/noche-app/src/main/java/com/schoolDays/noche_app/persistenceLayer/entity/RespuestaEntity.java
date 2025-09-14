package com.schoolDays.noche_app.persistenceLayer.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Entidad que representa la tabla respuestas
 */
@Entity
@Table(name = "Respuesta")
@Getter
@Setter
@NoArgsConstructor @AllArgsConstructor
public class RespuestaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idRespuesta;

    @Lob
    @Column(nullable = false)
    private String contenido;

    @Column(nullable = false)
    private Boolean esCorrecta;

    @ManyToOne
    @JoinColumn(name = "idPregunta", nullable = false)
    private PreguntaEntity pregunta;
}