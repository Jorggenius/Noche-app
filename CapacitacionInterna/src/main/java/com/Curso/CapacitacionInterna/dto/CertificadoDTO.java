package com.Curso.CapacitacionInterna.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CertificadoDTO {
    private Long id;
    private Long usuarioId;
    private String usuarioNombre;
    private Long cursoId;
    private String cursoTitulo;
    private LocalDateTime fechaEmision;
    private String hash;
}