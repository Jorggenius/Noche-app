package com.schoolDays.noche_app.presentationLayer.controller;

import com.schoolDays.noche_app.businessLayer.dto.RespuestaUsuarioDTO;
import com.schoolDays.noche_app.businessLayer.service.RespuestaUsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/respuestas-usuario")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Respuestas Usuario", description = "Gestión de respuestas de usuarios en evaluaciones")
@CrossOrigin(origins = "*")
public class RespuestaUsuarioController {

    private final RespuestaUsuarioService respuestaUsuarioService;

    @PostMapping
    @Operation(summary = "Registrar respuesta", description = "Registra la respuesta de un usuario a una pregunta")
    public ResponseEntity<RespuestaUsuarioDTO> registrarRespuesta(@RequestBody RespuestaUsuarioDTO respuestaUsuarioDTO) {
        try {
            RespuestaUsuarioDTO created = respuestaUsuarioService.registrarRespuesta(respuestaUsuarioDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/usuario/{usuarioId}/evaluacion/{evaluacionId}")
    @Operation(summary = "Respuestas en evaluación", description = "Obtiene todas las respuestas de un usuario en una evaluación específica")
    public ResponseEntity<List<RespuestaUsuarioDTO>> getRespuestasUsuarioEnEvaluacion(
            @PathVariable Integer usuarioId,
            @PathVariable Integer evaluacionId) {
        List<RespuestaUsuarioDTO> respuestas = respuestaUsuarioService.getRespuestasUsuarioEnEvaluacion(usuarioId, evaluacionId);
        return ResponseEntity.ok(respuestas);
    }

    @PutMapping("/{id}/calificar-manual")
    @Operation(summary = "Calificar manualmente", description = "Califica manualmente una respuesta abierta")
    public ResponseEntity<RespuestaUsuarioDTO> calificarRespuestaManual(
            @PathVariable Integer id,
            @RequestParam BigDecimal puntuacion,
            @RequestParam boolean correcta) {
        try {
            RespuestaUsuarioDTO calificada = respuestaUsuarioService.calificarRespuestaManual(id, puntuacion, correcta);
            return ResponseEntity.ok(calificada);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/usuario/{usuarioId}/correctas")
    @Operation(summary = "Respuestas correctas por usuario")
    public ResponseEntity<List<RespuestaUsuarioDTO>> getRespuestasCorrectasByUsuario(@PathVariable Integer usuarioId) {
        List<RespuestaUsuarioDTO> correctas = respuestaUsuarioService.getRespuestasCorrectasByUsuario(usuarioId);
        return ResponseEntity.ok(correctas);
    }
}
