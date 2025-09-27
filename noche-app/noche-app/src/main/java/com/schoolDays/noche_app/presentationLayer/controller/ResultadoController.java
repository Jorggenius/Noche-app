package com.schoolDays.noche_app.presentationLayer.controller;

import com.schoolDays.noche_app.businessLayer.dto.ResultadoDTO;
import com.schoolDays.noche_app.businessLayer.dto.ErrorResponseData;
import com.schoolDays.noche_app.businessLayer.service.ResultadoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/v1/resultados")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Resultados", description = "Gestión de resultados de evaluaciones")
@CrossOrigin(origins = "*")
public class ResultadoController {

    private final ResultadoService resultadoService;

    @PostMapping
    @Operation(summary = "Registrar resultado", description = "Registra el resultado final de una evaluación")
    public ResponseEntity<?> registrarResultado(@RequestBody ResultadoDTO resultadoDTO) {
        try {
            ResultadoDTO created = resultadoService.registrarResultado(resultadoDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            return createErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/calcular-automatico")
    @Operation(summary = "Calcular resultado automático", description = "Calcula automáticamente el resultado basado en las respuestas del usuario")
    public ResponseEntity<?> calcularResultadoAutomatico(
            @RequestParam Integer usuarioId,
            @RequestParam Integer evaluacionId) {
        try {
            ResultadoDTO resultado = resultadoService.calcularResultadoAutomatico(usuarioId, evaluacionId);
            return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
        } catch (IllegalArgumentException e) {
            return createErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Resultados por usuario")
    public ResponseEntity<List<ResultadoDTO>> getResultadosByUsuario(@PathVariable Integer usuarioId) {
        try {
            List<ResultadoDTO> resultados = resultadoService.getResultadosByUsuario(usuarioId);
            return ResponseEntity.ok(resultados);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/evaluacion/{evaluacionId}")
    @Operation(summary = "Resultados por evaluación")
    public ResponseEntity<List<ResultadoDTO>> getResultadosByEvaluacion(@PathVariable Integer evaluacionId) {
        try {
            List<ResultadoDTO> resultados = resultadoService.getResultadosByEvaluacion(evaluacionId);
            return ResponseEntity.ok(resultados);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/mejores")
    @Operation(summary = "Mejores resultados", description = "Obtiene los mejores resultados ordenados por puntaje")
    public ResponseEntity<List<ResultadoDTO>> getMejoresResultados() {
        List<ResultadoDTO> mejores = resultadoService.getMejoresResultados();
        return ResponseEntity.ok(mejores);
    }

    @GetMapping("/evaluacion/{evaluacionId}/promedio")
    @Operation(summary = "Promedio de evaluación")
    public ResponseEntity<BigDecimal> getPromedioPuntajeByEvaluacion(@PathVariable Integer evaluacionId) {
        try {
            BigDecimal promedio = resultadoService.getPromedioPuntajeByEvaluacion(evaluacionId);
            return ResponseEntity.ok(promedio);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/evaluacion/{evaluacionId}/estadisticas")
    @Operation(summary = "Estadísticas de evaluación")
    public ResponseEntity<Object> getEstadisticasEvaluacion(@PathVariable Integer evaluacionId) {
        try {
            Object estadisticas = resultadoService.getEstadisticasEvaluacion(evaluacionId);
            return ResponseEntity.ok(estadisticas);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    private ResponseEntity<ErrorResponseData> createErrorResponse(HttpStatus status, String message) {
        ErrorResponseData error = new ErrorResponseData(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message
        );
        return ResponseEntity.status(status).body(error);
    }
}
