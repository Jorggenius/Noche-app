package com.Curso.CapacitacionInterna.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "progreso_modulos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProgresoModulo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
    
    @ManyToOne
    @JoinColumn(name = "modulo_id", nullable = false)
    private Modulo modulo;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Estado estado;
    
    @Column(name = "fecha_inicio")
    private LocalDateTime fechaInicio;
    
    @Column(name = "fecha_completado")
    private LocalDateTime fechaCompletado;
    
    @PrePersist
    public void prePersist() {
        if (estado == null) {
            estado = Estado.INICIADO;
        }
        if (fechaInicio == null) {
            fechaInicio = LocalDateTime.now();
        }
    }
    
    public enum Estado {
        INICIADO, TERMINADO
    }
}