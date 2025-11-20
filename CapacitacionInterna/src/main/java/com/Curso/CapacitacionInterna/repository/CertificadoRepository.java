package com.Curso.CapacitacionInterna.repository;

import com.Curso.CapacitacionInterna.model.Certificado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CertificadoRepository extends JpaRepository<Certificado, Long> {
    
    List<Certificado> findByUsuarioId(Long usuarioId);
    
    List<Certificado> findByCursoId(Long cursoId);
    
    Optional<Certificado> findByUsuarioIdAndCursoId(Long usuarioId, Long cursoId);
    
    Optional<Certificado> findByHash(String hash);
    
    boolean existsByUsuarioIdAndCursoId(Long usuarioId, Long cursoId);
}