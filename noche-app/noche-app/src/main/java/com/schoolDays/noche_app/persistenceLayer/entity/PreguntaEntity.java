package com.schoolDays.noche_app.persistenceLayer.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "Pregunta")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PreguntaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPregunta;

    @Lob
    @Column(nullable = false)
    private String enunciado;

    @ManyToOne
    @JoinColumn(name = "idEvaluacion", nullable = false)
    private EvaluacionEntity evaluacion;

    @OneToMany(mappedBy = "pregunta")
    private List<RespuestaEntity> respuestas;
}