package com.schoolDays.noche_app.persistenceLayer.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModuloEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String titulo;

    @Column(nullable = false, length = 100)
    private String tipo;

    @Column(nullable = false, length = 100)
    private String orden;

    @ManyToOne
    @JoinColumn(name = "idcurso", nullable = false)
    private CursoEntity curso;

}
