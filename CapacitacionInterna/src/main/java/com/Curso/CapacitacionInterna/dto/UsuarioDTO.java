package com.Curso.CapacitacionInterna.dto;

import com.Curso.CapacitacionInterna.model.Usuario;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {
    private Long id;
    private String nombre;
    private String email;
    private Usuario.Rol rol;
    private String departamento;
}