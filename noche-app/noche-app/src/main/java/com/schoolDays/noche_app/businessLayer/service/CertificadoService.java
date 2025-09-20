package com.schoolDays.noche_app.businessLayer.service;

import com.schoolDays.noche_app.businessLayer.CertificadoDTO;

import java.util.List;

public interface CertificadoService {

    /**
     * Generar certificado automáticamente
     *
     * REGLAS:
     * - Usuario debe haber completado el curso
     * - Curso debe estar activo
     * - No duplicar certificados
     */
    CertificadoDTO generarCertificado(Integer idUsuario, Integer idCurso);

    /**
     * Buscar certificado por ID
     */
    CertificadoDTO getCertificadoById(Integer id);

    /**
     * Buscar todos los certificados
     */
    List<CertificadoDTO> getAllCertificados();

    /**
     * Buscar certificados por usuario
     */
    List<CertificadoDTO> getCertificadosByUsuario(Integer idUsuario);

    /**
     * Buscar certificados por curso
     */
    List<CertificadoDTO> getCertificadosByCurso(Integer idCurso);

    /**
     * Buscar certificado específico
     */
    CertificadoDTO getCertificadoByUsuarioAndCurso(Integer idUsuario, Integer idCurso);

    /**
     * Verificar certificado por hash
     */
    CertificadoDTO verificarCertificado(String hash);

    /**
     * Obtener certificados recientes
     */
    List<CertificadoDTO> getCertificadosRecientes();

    /**
     * Buscar por departamento
     */
    List<CertificadoDTO> getCertificadosByDepartamento(String departamento);

    /**
     * Verificar si usuario puede obtener certificado
     */
    boolean puedeGenerarCertificado(Integer idUsuario, Integer idCurso);

    /**
     * Regenerar certificado (nuevo hash)
     */
    CertificadoDTO regenerarCertificado(Integer id);

    /**
     * Revocar certificado
     */
    void revocarCertificado(Integer id);

    /**
     * Obtener conteo por usuario
     */
    long getCertificadosCountByUsuario(Integer idUsuario);

    /**
     * Obtener conteo por curso
     */
    long getCertificadosCountByCurso(Integer idCurso);

    /**
     * Generar PDF del certificado
     */
    byte[] generarCertificadoPDF(Integer id);
}