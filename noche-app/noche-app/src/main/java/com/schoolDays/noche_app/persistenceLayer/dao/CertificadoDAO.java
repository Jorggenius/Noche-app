package com.schoolDays.noche_app.persistenceLayer.dao;

import com.schoolDays.noche_app.businessLayer.CertificadoDTO;
import com.schoolDays.noche_app.persistenceLayer.entity.CertificadoEntity;
import com.schoolDays.noche_app.persistenceLayer.mapper.CertificadoMapper;
import com.schoolDays.noche_app.persistenceLayer.repository.CertificadoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CertificadoDAO {

    private final CertificadoRepository certificadoRepository;
    private final CertificadoMapper certificadoMapper;

    public CertificadoDTO save(CertificadoDTO certificadoDTO) {
        CertificadoEntity entity = certificadoMapper.toEntity(certificadoDTO);
        CertificadoEntity savedEntity = certificadoRepository.save(entity);
        return certificadoMapper.toDTO(savedEntity);
    }

    public Optional<CertificadoDTO> findById(Integer id) {
        return certificadoRepository.findById(id)
                .map(certificadoMapper::toDTO);
    }

    public List<CertificadoDTO> findAll() {
        List<CertificadoEntity> entities = certificadoRepository.findAll();
        return certificadoMapper.toDTOList(entities);
    }

    public Optional<CertificadoDTO> update(Integer id, CertificadoDTO certificadoDTO) {
        return certificadoRepository.findById(id)
                .map(existingEntity -> {
                    certificadoMapper.updateEntityFromDTO(certificadoDTO, existingEntity);
                    CertificadoEntity updatedEntity = certificadoRepository.save(existingEntity);
                    return certificadoMapper.toDTO(updatedEntity);
                });
    }

    public boolean deleteById(Integer id) {
        if (certificadoRepository.existsById(id)) {
            certificadoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<CertificadoDTO> findByUsuario(Integer idUsuario) {
        List<CertificadoEntity> entities = certificadoRepository.findByUsuario_IdUsuario(idUsuario);
        return certificadoMapper.toDTOList(entities);
    }

    public List<CertificadoDTO> findByCurso(Integer idCurso) {
        List<CertificadoEntity> entities = certificadoRepository.findByCurso_IdCurso(idCurso);
        return certificadoMapper.toDTOList(entities);
    }

    public Optional<CertificadoDTO> findByUsuarioAndCurso(Integer idUsuario, Integer idCurso) {
        return certificadoRepository.findByUsuario_IdUsuarioAndCurso_IdCurso(idUsuario, idCurso)
                .map(certificadoMapper::toDTO);
    }

    public Optional<CertificadoDTO> findByHash(String hash) {
        return certificadoRepository.findByHash(hash)
                .map(certificadoMapper::toDTO);
    }

    public List<CertificadoDTO> findRecientes() {
        List<CertificadoEntity> entities = certificadoRepository.findAllByOrderByFechaEmisionDesc();
        return certificadoMapper.toDTOList(entities);
    }

    public List<CertificadoDTO> findByDepartamento(String departamento) {
        List<CertificadoEntity> entities = certificadoRepository.findByUsuarioDepartamento(departamento);
        return certificadoMapper.toDTOList(entities);
    }

    public boolean existeCertificado(Integer idUsuario, Integer idCurso) {
        return certificadoRepository.existsByUsuario_IdUsuarioAndCurso_IdCurso(idUsuario, idCurso);
    }

    public boolean existeHash(String hash) {
        return certificadoRepository.existsByHash(hash);
    }

    public long countByUsuario(Integer idUsuario) {
        return certificadoRepository.countByUsuario_IdUsuario(idUsuario);
    }

    public long countByCurso(Integer idCurso) {
        return certificadoRepository.countByCurso_IdCurso(idCurso);
    }

    public long count() {
        return certificadoRepository.count();
    }
}
