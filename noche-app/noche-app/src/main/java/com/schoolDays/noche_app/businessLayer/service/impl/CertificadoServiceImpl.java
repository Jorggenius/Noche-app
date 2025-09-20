package com.schoolDays.noche_app.businessLayer.service.impl;

import com.schoolDays.noche_app.businessLayer.CertificadoDTO;
import com.schoolDays.noche_app.businessLayer.InscripcionDTO;
import com.schoolDays.noche_app.businessLayer.service.CertificadoService;
import com.schoolDays.noche_app.businessLayer.service.InscripcionService;
import com.schoolDays.noche_app.persistenceLayer.dao.CertificadoDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CertificadoServiceImpl implements CertificadoService {

    private final CertificadoDAO certificadoDAO;
    private final InscripcionService inscripcionService;

    @Override
    public CertificadoDTO generarCertificado(Integer idUsuario, Integer idCurso) {
        log.info("Generando certificado para usuario {} en curso {}", idUsuario, idCurso);

        // Verificar si ya tiene certificado
        if (certificadoDAO.existeCertificado(idUsuario, idCurso)) {
            throw new IllegalArgumentException("El usuario ya tiene certificado para este curso");
        }

        // Verificar que completó el curso
        InscripcionDTO inscripcion = inscripcionService.getInscripcionByUsuarioAndCurso(idUsuario, idCurso);
        if (!"COMPLETADO".equals(inscripcion.getEstado())) {
            throw new IllegalArgumentException("El usuario debe completar el curso para obtener certificado");
        }

        // Generar hash único
        String hash;
        do {
            hash = generateUniqueHash();
        } while (certificadoDAO.existeHash(hash));

        CertificadoDTO certificadoDTO = new CertificadoDTO();
        certificadoDTO.setIdUsuario(idUsuario);
        certificadoDTO.setIdCurso(idCurso);
        certificadoDTO.setFechaEmision(LocalDate.now());
        certificadoDTO.setHash(hash);

        CertificadoDTO createdCertificado = certificadoDAO.save(certificadoDTO);
        log.info("Certificado generado exitosamente con ID: {}", createdCertificado.getIdCertificado());

        return createdCertificado;
    }

    @Override
    @Transactional(readOnly = true)
    public CertificadoDTO getCertificadoById(Integer id) {
        return certificadoDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Certificado no encontrado con ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CertificadoDTO> getAllCertificados() {
        return certificadoDAO.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CertificadoDTO> getCertificadosByUsuario(Integer idUsuario) {
        return certificadoDAO.findByUsuario(idUsuario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CertificadoDTO> getCertificadosByCurso(Integer idCurso) {
        return certificadoDAO.findByCurso(idCurso);
    }

    @Override
    @Transactional(readOnly = true)
    public CertificadoDTO getCertificadoByUsuarioAndCurso(Integer idUsuario, Integer idCurso) {
        return certificadoDAO.findByUsuarioAndCurso(idUsuario, idCurso)
                .orElseThrow(() -> new RuntimeException("No se encontró certificado para el usuario y curso especificados"));
    }

    @Override
    @Transactional(readOnly = true)
    public CertificadoDTO verificarCertificado(String hash) {
        return certificadoDAO.findByHash(hash)
                .orElseThrow(() -> new RuntimeException("Certificado no válido o no encontrado"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CertificadoDTO> getCertificadosRecientes() {
        return certificadoDAO.findRecientes();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CertificadoDTO> getCertificadosByDepartamento(String departamento) {
        return certificadoDAO.findByDepartamento(departamento);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean puedeGenerarCertificado(Integer idUsuario, Integer idCurso) {
        try {
            InscripcionDTO inscripcion = inscripcionService.getInscripcionByUsuarioAndCurso(idUsuario, idCurso);
            return "COMPLETADO".equals(inscripcion.getEstado()) &&
                    !certificadoDAO.existeCertificado(idUsuario, idCurso);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public CertificadoDTO regenerarCertificado(Integer id) {
        log.info("Regenerando certificado ID: {}", id);

        CertificadoDTO certificado = getCertificadoById(id);

        // Generar nuevo hash
        String nuevoHash;
        do {
            nuevoHash = generateUniqueHash();
        } while (certificadoDAO.existeHash(nuevoHash));

        CertificadoDTO updateDTO = new CertificadoDTO();
        updateDTO.setHash(nuevoHash);
        updateDTO.setFechaEmision(LocalDate.now());

        return certificadoDAO.update(id, updateDTO)
                .orElseThrow(() -> new RuntimeException("Error al regenerar certificado"));
    }

    @Override
    public void revocarCertificado(Integer id) {
        log.info("Revocando certificado ID: {}", id);

        getCertificadoById(id); // Verificar existencia

        if (!certificadoDAO.deleteById(id)) {
            throw new RuntimeException("Error al revocar certificado con ID: " + id);
        }

        log.info("Certificado revocado exitosamente ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public long getCertificadosCountByUsuario(Integer idUsuario) {
        return certificadoDAO.countByUsuario(idUsuario);
    }

    @Override
    @Transactional(readOnly = true)
    public long getCertificadosCountByCurso(Integer idCurso) {
        return certificadoDAO.countByCurso(idCurso);
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] generarCertificadoPDF(Integer id) {
        CertificadoDTO certificado = getCertificadoById(id);

        // Aquí implementarías la generación del PDF
        // Podrías usar librerías como iText, PDFBox, etc.

        log.info("Generando PDF para certificado ID: {}", id);
        return new byte[0]; // Placeholder
    }

    private String generateUniqueHash() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 32);
    }
}
