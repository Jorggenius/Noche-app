package com.schoolDays.noche_app.presentationLayer.controller;

import com.schoolDays.noche_app.businessLayer.dto.CertificadoDTO;
import com.schoolDays.noche_app.businessLayer.service.CertificadoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/certificados")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Certificados", description = "Gestión de certificados de finalización")
@CrossOrigin(origins = "*")
public class CertificadoController {

    private final CertificadoService certificadoService;

    @PostMapping
    @Operation(
            summary = "Generar certificado",
            description = "Genera un certificado automáticamente para un usuario que completó un curso"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Certificado generado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CertificadoDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Usuario no ha completado el curso o ya tiene certificado"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario o curso no encontrado"
            )
    })
    public ResponseEntity<CertificadoDTO> generarCertificado(
            @Parameter(description = "ID del usuario", required = true, example = "1")
            @RequestParam Integer idUsuario,
            @Parameter(description = "ID del curso", required = true, example = "1")
            @RequestParam Integer idCurso
    ) {
        log.info("POST /api/v1/certificados?idUsuario={}&idCurso={} - Generando certificado", idUsuario, idCurso);

        try {
            CertificadoDTO certificado = certificadoService.generarCertificado(idUsuario, idCurso);
            log.info("Certificado generado exitosamente con ID: {}", certificado.getIdCertificado());
            return ResponseEntity.status(HttpStatus.CREATED).body(certificado);
        } catch (IllegalArgumentException e) {
            log.warn("Error al generar certificado: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            log.warn("Usuario o curso no encontrado: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar certificado por ID",
            description = "Obtiene información completa de un certificado específico"
    )
    public ResponseEntity<CertificadoDTO> getCertificadoById(
            @Parameter(description = "ID del certificado", required = true, example = "1")
            @PathVariable Integer id
    ) {
        log.debug("GET /api/v1/certificados/{} - Buscando certificado", id);

        try {
            CertificadoDTO certificado = certificadoService.getCertificadoById(id);
            return ResponseEntity.ok(certificado);
        } catch (RuntimeException e) {
            log.warn("Certificado no encontrado con ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    @Operation(
            summary = "Listar todos los certificados",
            description = "Obtiene lista completa de certificados emitidos"
    )
    public ResponseEntity<List<CertificadoDTO>> getAllCertificados() {
        log.debug("GET /api/v1/certificados - Obteniendo todos los certificados");

        List<CertificadoDTO> certificados = certificadoService.getAllCertificados();
        log.debug("Se encontraron {} certificados", certificados.size());
        return ResponseEntity.ok(certificados);
    }

    @GetMapping("/usuario/{usuarioId}")
    @Operation(
            summary = "Certificados por usuario",
            description = "Obtiene todos los certificados de un usuario específico"
    )
    public ResponseEntity<List<CertificadoDTO>> getCertificadosByUsuario(
            @Parameter(description = "ID del usuario", required = true, example = "1")
            @PathVariable Integer usuarioId
    ) {
        log.debug("GET /api/v1/certificados/usuario/{} - Certificados por usuario", usuarioId);

        List<CertificadoDTO> certificados = certificadoService.getCertificadosByUsuario(usuarioId);
        return ResponseEntity.ok(certificados);
    }

    @GetMapping("/curso/{cursoId}")
    @Operation(
            summary = "Certificados por curso",
            description = "Obtiene todos los certificados emitidos para un curso específico"
    )
    public ResponseEntity<List<CertificadoDTO>> getCertificadosByCurso(
            @Parameter(description = "ID del curso", required = true, example = "1")
            @PathVariable Integer cursoId
    ) {
        log.debug("GET /api/v1/certificados/curso/{} - Certificados por curso", cursoId);

        List<CertificadoDTO> certificados = certificadoService.getCertificadosByCurso(cursoId);
        return ResponseEntity.ok(certificados);
    }

    @GetMapping("/verificar/{hash}")
    @Operation(
            summary = "Verificar certificado",
            description = "Verifica la autenticidad de un certificado mediante su hash"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Certificado válido",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CertificadoDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Certificado no válido o no encontrado"
            )
    })
    public ResponseEntity<CertificadoDTO> verificarCertificado(
            @Parameter(description = "Hash del certificado", required = true, example = "abc123def456")
            @PathVariable String hash
    ) {
        log.debug("GET /api/v1/certificados/verificar/{} - Verificando certificado", hash);

        try {
            CertificadoDTO certificado = certificadoService.verificarCertificado(hash);
            return ResponseEntity.ok(certificado);
        } catch (RuntimeException e) {
            log.warn("Certificado no válido con hash: {}", hash);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/recientes")
    @Operation(
            summary = "Certificados recientes",
            description = "Obtiene los certificados más recientes ordenados por fecha de emisión"
    )
    public ResponseEntity<List<CertificadoDTO>> getCertificadosRecientes() {
        log.debug("GET /api/v1/certificados/recientes - Certificados recientes");

        List<CertificadoDTO> certificados = certificadoService.getCertificadosRecientes();
        return ResponseEntity.ok(certificados);
    }

    @GetMapping("/departamento/{departamento}")
    @Operation(
            summary = "Certificados por departamento",
            description = "Obtiene certificados de usuarios de un departamento específico"
    )
    public ResponseEntity<List<CertificadoDTO>> getCertificadosByDepartamento(
            @Parameter(description = "Nombre del departamento", required = true, example = "IT")
            @PathVariable String departamento
    ) {
        log.debug("GET /api/v1/certificados/departamento/{} - Certificados por departamento", departamento);

        List<CertificadoDTO> certificados = certificadoService.getCertificadosByDepartamento(departamento);
        return ResponseEntity.ok(certificados);
    }

    @GetMapping("/puede-generar")
    @Operation(
            summary = "Verificar si puede generar certificado",
            description = "Verifica si un usuario puede obtener certificado para un curso"
    )
    public ResponseEntity<Boolean> puedeGenerarCertificado(
            @Parameter(description = "ID del usuario", required = true, example = "1")
            @RequestParam Integer idUsuario,
            @Parameter(description = "ID del curso", required = true, example = "1")
            @RequestParam Integer idCurso
    ) {
        log.debug("GET /api/v1/certificados/puede-generar?idUsuario={}&idCurso={}", idUsuario, idCurso);

        boolean puede = certificadoService.puedeGenerarCertificado(idUsuario, idCurso);
        return ResponseEntity.ok(puede);
    }

    @PutMapping("/{id}/regenerar")
    @Operation(
            summary = "Regenerar certificado",
            description = "Genera un nuevo hash para un certificado existente"
    )
    public ResponseEntity<CertificadoDTO> regenerarCertificado(
            @Parameter(description = "ID del certificado", required = true, example = "1")
            @PathVariable Integer id
    ) {
        log.info("PUT /api/v1/certificados/{}/regenerar - Regenerando certificado", id);

        try {
            CertificadoDTO certificado = certificadoService.regenerarCertificado(id);
            log.info("Certificado regenerado exitosamente ID: {}", id);
            return ResponseEntity.ok(certificado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Revocar certificado",
            description = "Revoca/elimina un certificado del sistema"
    )
    public ResponseEntity<Void> revocarCertificado(
            @Parameter(description = "ID del certificado", required = true, example = "1")
            @PathVariable Integer id
    ) {
        log.info("DELETE /api/v1/certificados/{} - Revocando certificado", id);

        try {
            certificadoService.revocarCertificado(id);
            log.info("Certificado revocado exitosamente ID: {}", id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/pdf")
    @Operation(
            summary = "Descargar certificado en PDF",
            description = "Genera y descarga el certificado en formato PDF"
    )
    public ResponseEntity<byte[]> descargarCertificadoPDF(
            @Parameter(description = "ID del certificado", required = true, example = "1")
            @PathVariable Integer id
    ) {
        log.info("GET /api/v1/certificados/{}/pdf - Generando PDF", id);

        try {
            byte[] pdfBytes = certificadoService.generarCertificadoPDF(id);
            return ResponseEntity.ok()
                    .header("Content-Type", "application/pdf")
                    .header("Content-Disposition", "attachment; filename=certificado_" + id + ".pdf")
                    .body(pdfBytes);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
