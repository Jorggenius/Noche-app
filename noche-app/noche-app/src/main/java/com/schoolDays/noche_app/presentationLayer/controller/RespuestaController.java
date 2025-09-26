package com.schoolDays.noche_app.presentationLayer.controller;

import com.schoolDays.noche_app.businessLayer.dto.RespuestaDTO;
import com.schoolDays.noche_app.businessLayer.service.RespuestaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/respuestas")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Respuestas", description = "Gestión de opciones de respuesta para preguntas MCQ")
@CrossOrigin(origins = "*")
public class RespuestaController {

    private final RespuestaService respuestaService;

    @PostMapping
    @Operation(summary = "Crear opción de respuesta", description = "Crea una opción de respuesta para una pregunta MCQ")
    public ResponseEntity<RespuestaDTO> createRespuesta(@RequestBody RespuestaDTO respuestaDTO) {
        try {
            RespuestaDTO created = respuestaService.createRespuesta(respuestaDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar respuesta por ID")
    public ResponseEntity<RespuestaDTO> getRespuestaById(@PathVariable Integer id) {
        try {
            RespuestaDTO respuesta = respuestaService.getRespuestaById(id);
            return ResponseEntity.ok(respuesta);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/pregunta/{preguntaId}")
    @Operation(summary = "Opciones por pregunta", description = "Obtiene todas las opciones ordenadas de una pregunta")
    public ResponseEntity<List<RespuestaDTO>> getRespuestasByPregunta(@PathVariable Integer preguntaId) {
        List<RespuestaDTO> respuestas = respuestaService.getRespuestasByPreguntaOrdenadas(preguntaId);
        return ResponseEntity.ok(respuestas);
    }

    @GetMapping("/pregunta/{preguntaId}/correctas")
    @Operation(summary = "Respuestas correctas", description = "Obtiene solo las opciones correctas de una pregunta")
    public ResponseEntity<List<RespuestaDTO>> getRespuestasCorrectas(@PathVariable Integer preguntaId) {
        List<RespuestaDTO> correctas = respuestaService.getRespuestasCorrectas(preguntaId);
        return ResponseEntity.ok(correctas);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar respuesta")
    public ResponseEntity<RespuestaDTO> updateRespuesta(@PathVariable Integer id, @RequestBody RespuestaDTO respuestaDTO) {
        try {
            RespuestaDTO updated = respuestaService.updateRespuesta(id, respuestaDTO);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar respuesta")
    public ResponseEntity<Void> deleteRespuesta(@PathVariable Integer id) {
        try {
            respuestaService.deleteRespuesta(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
