package com.Curso.CapacitacionInterna.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InscripcionCreateDTO {
    private Long usuarioId;
    private Long cursoId;
}