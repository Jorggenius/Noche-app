package com.Curso.CapacitacionInterna.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "cursos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Curso {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String titulo;
    
    @Column(length = 1000)
    private String descripcion;
    
    @Column(name = "duracion_estimada")
    private Integer duracionEstimada; // en horas
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Nivel nivel;
    
    @ManyToOne
    @JoinColumn(name = "instructor_id")
    private Usuario instructor;
    
    private Boolean activo = true;
    
    public enum Nivel {
        BASICO, INTERMEDIO, AVANZADO
    }
}