package com.Curso.CapacitacionInterna.service;

import com.Curso.CapacitacionInterna.dto.CertificadoDTO;
import com.Curso.CapacitacionInterna.model.Certificado;
import com.Curso.CapacitacionInterna.model.Curso;
import com.Curso.CapacitacionInterna.model.Usuario;
import com.Curso.CapacitacionInterna.repository.CertificadoRepository;
import com.Curso.CapacitacionInterna.repository.CursoRepository;
import com.Curso.CapacitacionInterna.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CertificadoService {

    @Autowired
    private CertificadoRepository certificadoRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private CursoRepository cursoRepository;

    // Generar certificado automáticamente
    public CertificadoDTO generarCertificado(Long usuarioId, Long cursoId) {
        // Verificar que no exista ya un certificado
        if (certificadoRepository.existsByUsuarioIdAndCursoId(usuarioId, cursoId)) {
            throw new RuntimeException("Ya existe un certificado para este usuario y curso");
        }
        
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        Curso curso = cursoRepository.findById(cursoId)
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"));

        Certificado certificado = new Certificado();
        certificado.setUsuario(usuario);
        certificado.setCurso(curso);

        Certificado certificadoGuardado = certificadoRepository.save(certificado);
        return convertirADTO(certificadoGuardado);
    }

    // Obtener certificados por usuario
    public List<CertificadoDTO> obtenerCertificadosPorUsuario(Long usuarioId) {
        return certificadoRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Obtener certificados por curso
    public List<CertificadoDTO> obtenerCertificadosPorCurso(Long cursoId) {
        return certificadoRepository.findByCursoId(cursoId)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Verificar certificado por hash
    public Optional<CertificadoDTO> verificarCertificado(String hash) {
        return certificadoRepository.findByHash(hash)
                .map(this::convertirADTO);
    }

    // Obtener todos los certificados
    public List<CertificadoDTO> obtenerTodosLosCertificados() {
        return certificadoRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Método auxiliar para convertir entidad a DTO
    private CertificadoDTO convertirADTO(Certificado certificado) {
        return new CertificadoDTO(
                certificado.getId(),
                certificado.getUsuario().getId(),
                certificado.getUsuario().getNombre(),
                certificado.getCurso().getId(),
                certificado.getCurso().getTitulo(),
                certificado.getFechaEmision(),
                certificado.getHash()
        );
    }
}