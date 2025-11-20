package com.Curso.CapacitacionInterna.repository;

import com.Curso.CapacitacionInterna.model.Inscripcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {
    
    List<Inscripcion> findByUsuarioId(Long usuarioId);
    
    List<Inscripcion> findByCursoId(Long cursoId);
    
    Optional<Inscripcion> findByUsuarioIdAndCursoId(Long usuarioId, Long cursoId);
    
    List<Inscripcion> findByEstado(Inscripcion.Estado estado);
    
    boolean existsByUsuarioIdAndCursoId(Long usuarioId, Long cursoId);
}