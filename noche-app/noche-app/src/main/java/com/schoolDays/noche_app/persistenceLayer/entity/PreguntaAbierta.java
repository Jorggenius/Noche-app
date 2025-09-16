package com.schoolDays.noche_app.persistenceLayer.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PreguntaAbierta {

    private Long id;

    @Column(nullable = false, length = 500)
    private String respuesta;

    @OneToOne
    @JoinColumn(name = "idevaluacion", nullable = false)
    private EvaluacionEntity evaluacion;
}
