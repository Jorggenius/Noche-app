package com.Curso.CapacitacionInterna.repository;

import com.Curso.CapacitacionInterna.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    Optional<Usuario> findByEmail(String email);
    
    List<Usuario> findByRol(Usuario.Rol rol);
    
    List<Usuario> findByDepartamento(String departamento);
    
    boolean existsByEmail(String email);
}