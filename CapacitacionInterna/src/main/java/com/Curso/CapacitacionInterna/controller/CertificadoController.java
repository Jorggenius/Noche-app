package com.Curso.CapacitacionInterna.controller;

import com.Curso.CapacitacionInterna.dto.CertificadoDTO;
import com.Curso.CapacitacionInterna.service.CertificadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/certificados")
@CrossOrigin(origins = "*")
@Tag(name = "Certificado", description = "Controlador para gestión de certificados")
public class CertificadoController {

    @Autowired
    private CertificadoService certificadoService;

    @GetMapping
    @Operation(summary = "Obtener todos los certificados", description = "Retorna todos los certificados emitidos")
    public ResponseEntity<List<CertificadoDTO>> obtenerTodosLosCertificados() {
        List<CertificadoDTO> certificados = certificadoService.obtenerTodosLosCertificados();
        return ResponseEntity.ok(certificados);
    }

    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Obtener certificados por usuario", description = "Retorna todos los certificados de un usuario")
    public ResponseEntity<List<CertificadoDTO>> obtenerCertificadosPorUsuario(@PathVariable Long usuarioId) {
        List<CertificadoDTO> certificados = certificadoService.obtenerCertificadosPorUsuario(usuarioId);
        return ResponseEntity.ok(certificados);
    }

    @GetMapping("/curso/{cursoId}")
    @Operation(summary = "Obtener certificados por curso", description = "Retorna todos los certificados emitidos para un curso")
    public ResponseEntity<List<CertificadoDTO>> obtenerCertificadosPorCurso(@PathVariable Long cursoId) {
        List<CertificadoDTO> certificados = certificadoService.obtenerCertificadosPorCurso(cursoId);
        return ResponseEntity.ok(certificados);
    }

    @GetMapping("/verificar/{hash}")
    @Operation(summary = "Verificar certificado", description = "Verifica la autenticidad de un certificado por su hash")
    public ResponseEntity<CertificadoDTO> verificarCertificado(@PathVariable String hash) {
        Optional<CertificadoDTO> certificado = certificadoService.verificarCertificado(hash);
        return certificado.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}