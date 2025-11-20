package com.Curso.CapacitacionInterna.service;

import com.Curso.CapacitacionInterna.model.*;
import com.Curso.CapacitacionInterna.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProgresoModuloService {

    @Autowired
    private ProgresoModuloRepository progresoModuloRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private ModuloRepository moduloRepository;
    
    @Autowired
    private InscripcionRepository inscripcionRepository;
    
    @Autowired
    private CertificadoService certificadoService;

    // Marcar módulo como terminado
    @Transactional
    public void completarModulo(Long usuarioId, Long moduloId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        Modulo modulo = moduloRepository.findById(moduloId)
                .orElseThrow(() -> new RuntimeException("Módulo no encontrado"));

        // Verificar que el usuario esté inscrito en el curso
        if (!inscripcionRepository.existsByUsuarioIdAndCursoId(usuarioId, modulo.getCurso().getId())) {
            throw new RuntimeException("El usuario no está inscrito en este curso");
        }

        // Buscar o crear progreso del módulo
        ProgresoModulo progreso = progresoModuloRepository.findByUsuarioIdAndModuloId(usuarioId, moduloId)
                .orElse(new ProgresoModulo());

        if (progreso.getId() == null) {
            // Crear nuevo progreso
            progreso.setUsuario(usuario);
            progreso.setModulo(modulo);
            progreso.setEstado(ProgresoModulo.Estado.TERMINADO);
            progreso.setFechaInicio(LocalDateTime.now());
            progreso.setFechaCompletado(LocalDateTime.now());
        } else {
            // Actualizar existente
            progreso.setEstado(ProgresoModulo.Estado.TERMINADO);
            progreso.setFechaCompletado(LocalDateTime.now());
        }

        progresoModuloRepository.save(progreso);

        // Actualizar progreso de la inscripción
        actualizarProgresoInscripcion(usuarioId, modulo.getCurso().getId());
    }

    // Calcular y actualizar progreso automáticamente
    private void actualizarProgresoInscripcion(Long usuarioId, Long cursoId) {
        // Contar módulos totales del curso
        List<Modulo> modulosTotales = moduloRepository.findByCursoIdOrderByOrden(cursoId);
        int totalModulos = modulosTotales.size();

        if (totalModulos == 0) {
            return; // No hay módulos, no actualizar progreso
        }

        // Contar módulos terminados por el usuario
        long modulosTerminados = progresoModuloRepository.countModulosTerminadosPorUsuarioYCurso(usuarioId, cursoId);

        // Calcular porcentaje
        int porcentajeProgreso = (int) Math.round((double) modulosTerminados / totalModulos * 100);

        // Actualizar inscripción
        Inscripcion inscripcion = inscripcionRepository.findByUsuarioIdAndCursoId(usuarioId, cursoId)
                .orElseThrow(() -> new RuntimeException("Inscripción no encontrada"));

        inscripcion.setProgreso(porcentajeProgreso);

        // Actualizar estado según progreso
        if (porcentajeProgreso == 0) {
            inscripcion.setEstado(Inscripcion.Estado.INSCRITO);
        } else if (porcentajeProgreso > 0 && porcentajeProgreso < 100) {
            inscripcion.setEstado(Inscripcion.Estado.EN_PROGRESO);
        } else if (porcentajeProgreso == 100) {
            inscripcion.setEstado(Inscripcion.Estado.COMPLETADO);
            
            // Generar certificado automáticamente al completar el curso
            try {
                certificadoService.generarCertificado(usuarioId, cursoId);
            } catch (RuntimeException e) {
                // El certificado ya existe, no hacer nada
                System.out.println("Certificado ya existe para usuario " + usuarioId + " y curso " + cursoId);
            }
        }

        inscripcionRepository.save(inscripcion);
    }

    // Obtener progreso de módulos por usuario y curso
    public List<ProgresoModulo> obtenerProgresoModulosPorUsuarioYCurso(Long usuarioId, Long cursoId) {
        return progresoModuloRepository.findByUsuarioIdAndModuloCursoId(usuarioId, cursoId);
    }
}