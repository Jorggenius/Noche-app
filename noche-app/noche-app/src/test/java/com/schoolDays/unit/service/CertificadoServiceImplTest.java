package com.schoolDays.unit.service;

import com.schoolDays.noche_app.businessLayer.dto.CertificadoDTO;
import com.schoolDays.noche_app.businessLayer.dto.InscripcionDTO;
import com.schoolDays.noche_app.businessLayer.service.InscripcionService;
import com.schoolDays.noche_app.businessLayer.service.impl.CertificadoServiceImpl;
import com.schoolDays.noche_app.persistenceLayer.dao.CertificadoDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CertificadoServiceImplTest {

    @Mock
    private CertificadoDAO certificadoDAO;

    @Mock
    private InscripcionService inscripcionService;

    @InjectMocks
    private CertificadoServiceImpl certificadoService;

    private CertificadoDTO certificadoDTO;
    private InscripcionDTO inscripcionDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        certificadoDTO = new CertificadoDTO(1, LocalDate.now(), "hash123", 10, "Juan", 20, "Curso Java");
        inscripcionDTO = new InscripcionDTO();
        inscripcionDTO.setEstado("COMPLETADO");
    }

    // ---------------------- CREAR / GENERAR CERTIFICADO ----------------------

    @Test
    void generarCertificado_Exitoso() {
        when(certificadoDAO.existeCertificado(10, 20)).thenReturn(false);
        when(inscripcionService.getInscripcionByUsuarioAndCurso(10, 20)).thenReturn(inscripcionDTO);
        when(certificadoDAO.existeHash(anyString())).thenReturn(false);
        when(certificadoDAO.save(any())).thenReturn(certificadoDTO);

        CertificadoDTO result = certificadoService.generarCertificado(10, 20);

        assertNotNull(result);
        assertEquals("hash123", result.getHash());
        verify(certificadoDAO, times(1)).save(any());
    }

    @Test
    void generarCertificado_YaExisteLanzaExcepcion() {
        when(certificadoDAO.existeCertificado(10, 20)).thenReturn(true);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> certificadoService.generarCertificado(10, 20));

        assertEquals("El usuario ya tiene certificado para este curso", ex.getMessage());
    }

    @Test
    void generarCertificado_NoCompletadoLanzaExcepcion() {
        inscripcionDTO.setEstado("EN_CURSO");

        when(certificadoDAO.existeCertificado(10, 20)).thenReturn(false);
        when(inscripcionService.getInscripcionByUsuarioAndCurso(10, 20)).thenReturn(inscripcionDTO);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> certificadoService.generarCertificado(10, 20));

        assertEquals("El usuario debe completar el curso para obtener certificado", ex.getMessage());
    }

    // ---------------------- OBTENER CERTIFICADO ----------------------

    @Test
    void getCertificadoById_Exitoso() {
        when(certificadoDAO.findById(1)).thenReturn(Optional.of(certificadoDTO));

        CertificadoDTO result = certificadoService.getCertificadoById(1);

        assertEquals(1, result.getIdCertificado());
    }

    @Test
    void getCertificadoById_NoEncontradoLanzaExcepcion() {
        when(certificadoDAO.findById(99)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> certificadoService.getCertificadoById(99));

        assertEquals("Certificado no encontrado con ID: 99", ex.getMessage());
    }

    @Test
    void getAllCertificados_Exitoso() {
        when(certificadoDAO.findAll()).thenReturn(List.of(certificadoDTO));

        List<CertificadoDTO> result = certificadoService.getAllCertificados();

        assertEquals(1, result.size());
        assertEquals("hash123", result.get(0).getHash());
    }

    // ---------------------- BÚSQUEDAS ----------------------

    @Test
    void getCertificadosByUsuario_Exitoso() {
        when(certificadoDAO.findByUsuario(10)).thenReturn(List.of(certificadoDTO));

        List<CertificadoDTO> result = certificadoService.getCertificadosByUsuario(10);

        assertEquals(1, result.size());
        verify(certificadoDAO).findByUsuario(10);
    }

    @Test
    void getCertificadosByCurso_Exitoso() {
        when(certificadoDAO.findByCurso(20)).thenReturn(List.of(certificadoDTO));

        List<CertificadoDTO> result = certificadoService.getCertificadosByCurso(20);

        assertEquals(1, result.size());
    }

    @Test
    void getCertificadoByUsuarioAndCurso_Exitoso() {
        when(certificadoDAO.findByUsuarioAndCurso(10, 20)).thenReturn(Optional.of(certificadoDTO));

        CertificadoDTO result = certificadoService.getCertificadoByUsuarioAndCurso(10, 20);

        assertEquals("hash123", result.getHash());
    }

    @Test
    void getCertificadoByUsuarioAndCurso_NoEncontradoLanzaExcepcion() {
        when(certificadoDAO.findByUsuarioAndCurso(10, 20)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> certificadoService.getCertificadoByUsuarioAndCurso(10, 20));

        assertEquals("No se encontró certificado para el usuario y curso especificados", ex.getMessage());
    }

    @Test
    void verificarCertificado_Exitoso() {
        when(certificadoDAO.findByHash("hash123")).thenReturn(Optional.of(certificadoDTO));

        CertificadoDTO result = certificadoService.verificarCertificado("hash123");

        assertEquals(1, result.getIdCertificado());
    }

    @Test
    void verificarCertificado_NoEncontradoLanzaExcepcion() {
        when(certificadoDAO.findByHash("invalid")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> certificadoService.verificarCertificado("invalid"));

        assertEquals("Certificado no válido o no encontrado", ex.getMessage());
    }

    // ---------------------- FILTROS Y CONTADORES ----------------------

    @Test
    void getCertificadosRecientes_Exitoso() {
        when(certificadoDAO.findRecientes()).thenReturn(List.of(certificadoDTO));

        List<CertificadoDTO> result = certificadoService.getCertificadosRecientes();

        assertEquals(1, result.size());
    }

    @Test
    void getCertificadosByDepartamento_Exitoso() {
        when(certificadoDAO.findByDepartamento("Antioquia")).thenReturn(List.of(certificadoDTO));

        List<CertificadoDTO> result = certificadoService.getCertificadosByDepartamento("Antioquia");

        assertEquals(1, result.size());
    }

    @Test
    void puedeGenerarCertificado_Exitoso() {
        when(inscripcionService.getInscripcionByUsuarioAndCurso(10, 20)).thenReturn(inscripcionDTO);
        when(certificadoDAO.existeCertificado(10, 20)).thenReturn(false);

        assertTrue(certificadoService.puedeGenerarCertificado(10, 20));
    }

    @Test
    void puedeGenerarCertificado_FallaPorExcepcion() {
        when(inscripcionService.getInscripcionByUsuarioAndCurso(anyInt(), anyInt()))
                .thenThrow(new RuntimeException("Error"));

        assertFalse(certificadoService.puedeGenerarCertificado(1, 1));
    }

    // ---------------------- REGENERAR / REVOCAR ----------------------

    @Test
    void regenerarCertificado_Exitoso() {
        when(certificadoDAO.findById(1)).thenReturn(Optional.of(certificadoDTO));
        when(certificadoDAO.existeHash(anyString())).thenReturn(false);
        when(certificadoDAO.update(eq(1), any())).thenReturn(Optional.of(certificadoDTO));

        CertificadoDTO result = certificadoService.regenerarCertificado(1);

        assertNotNull(result);
        verify(certificadoDAO).update(eq(1), any());
    }

    @Test
    void regenerarCertificado_ErrorLanzaExcepcion() {
        when(certificadoDAO.findById(1)).thenReturn(Optional.of(certificadoDTO));
        when(certificadoDAO.existeHash(anyString())).thenReturn(false);
        when(certificadoDAO.update(eq(1), any())).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> certificadoService.regenerarCertificado(1));

        assertEquals("Error al regenerar certificado", ex.getMessage());
    }

    @Test
    void revocarCertificado_Exitoso() {
        when(certificadoDAO.findById(1)).thenReturn(Optional.of(certificadoDTO));
        when(certificadoDAO.deleteById(1)).thenReturn(true);

        assertDoesNotThrow(() -> certificadoService.revocarCertificado(1));
        verify(certificadoDAO).deleteById(1);
    }

    @Test
    void revocarCertificado_ErrorLanzaExcepcion() {
        when(certificadoDAO.findById(1)).thenReturn(Optional.of(certificadoDTO));
        when(certificadoDAO.deleteById(1)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> certificadoService.revocarCertificado(1));

        assertEquals("Error al revocar certificado con ID: 1", ex.getMessage());
    }

    // ---------------------- CONTADORES ----------------------

    @Test
    void getCertificadosCountByUsuario_Exitoso() {
        when(certificadoDAO.countByUsuario(10)).thenReturn(5L);

        long result = certificadoService.getCertificadosCountByUsuario(10);

        assertEquals(5, result);
    }

    @Test
    void getCertificadosCountByCurso_Exitoso() {
        when(certificadoDAO.countByCurso(20)).thenReturn(3L);

        long result = certificadoService.getCertificadosCountByCurso(20);

        assertEquals(3, result);
    }

    // ---------------------- PDF GENERATION ----------------------

    @Test
    void generarCertificadoPDF_Exitoso() {
        when(certificadoDAO.findById(1)).thenReturn(Optional.of(certificadoDTO));

        byte[] pdf = certificadoService.generarCertificadoPDF(1);

        assertNotNull(pdf);
        assertEquals(0, pdf.length);
    }
}
