package com.Curso.CapacitacionInterna.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "inscripciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Inscripcion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
    
    @ManyToOne
    @JoinColumn(name = "curso_id", nullable = false)
    private Curso curso;
    
    @Column(nullable = false)
    private Integer progreso = 0; // Porcentaje de 0 a 100
    
    @Column(name = "fecha_inscripcion", nullable = false)
    private LocalDateTime fechaInscripcion;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Estado estado;
    
    @PrePersist
    public void prePersist() {
        if (fechaInscripcion == null) {
            fechaInscripcion = LocalDateTime.now();
        }
        if (estado == null) {
            estado = Estado.INSCRITO;
        }
    }
    
    public enum Estado {
        INSCRITO, EN_PROGRESO, COMPLETADO
    }
}