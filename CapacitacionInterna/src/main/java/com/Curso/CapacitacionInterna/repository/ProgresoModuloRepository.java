package com.Curso.CapacitacionInterna.repository;

import com.Curso.CapacitacionInterna.model.ProgresoModulo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProgresoModuloRepository extends JpaRepository<ProgresoModulo, Long> {
    
    Optional<ProgresoModulo> findByUsuarioIdAndModuloId(Long usuarioId, Long moduloId);
    
    List<ProgresoModulo> findByUsuarioIdAndModuloCursoId(Long usuarioId, Long cursoId);
    
    @Query("SELECT COUNT(pm) FROM ProgresoModulo pm WHERE pm.usuario.id = ?1 AND pm.modulo.curso.id = ?2 AND pm.estado = 'TERMINADO'")
    long countModulosTerminadosPorUsuarioYCurso(Long usuarioId, Long cursoId);
    
    boolean existsByUsuarioIdAndModuloId(Long usuarioId, Long moduloId);
}