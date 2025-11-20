package com.Curso.CapacitacionInterna.repository;

import com.Curso.CapacitacionInterna.model.Curso;
import com.Curso.CapacitacionInterna.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {
    
    List<Curso> findByActivoTrue();
    
    List<Curso> findByNivel(Curso.Nivel nivel);
    
    List<Curso> findByInstructor(Usuario instructor);
    
    List<Curso> findByInstructorId(Long instructorId);
    
    List<Curso> findByTituloContainingIgnoreCase(String titulo);
    
    List<Curso> findByActivoTrueAndNivel(Curso.Nivel nivel);
}