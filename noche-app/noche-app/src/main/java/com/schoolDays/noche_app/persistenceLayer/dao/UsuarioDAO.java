package com.schoolDays.noche_app.persistenceLayer.dao;

import com.schoolDays.noche_app.businessLayer.dto.UsuarioDTO;
import com.schoolDays.noche_app.persistenceLayer.entity.UsuarioEntity;
import com.schoolDays.noche_app.persistenceLayer.mapper.UsuarioMapper;
import com.schoolDays.noche_app.persistenceLayer.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UsuarioDAO {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;

    public UsuarioDTO save(UsuarioDTO usuarioDTO) {
        UsuarioEntity entity = usuarioMapper.toEntity(usuarioDTO);
        UsuarioEntity savedEntity = usuarioRepository.save(entity);
        return usuarioMapper.toDTO(savedEntity);
    }

    public Optional<UsuarioDTO> findById(Integer id) {
        return usuarioRepository.findById(id)
                .map(usuarioMapper::toDTO);
    }

    public List<UsuarioDTO> findAll() {
        List<UsuarioEntity> entities = usuarioRepository.findAll();
        return usuarioMapper.toDTOList(entities);
    }

    public Optional<UsuarioDTO> update(Integer id, UsuarioDTO usuarioDTO) {
        return usuarioRepository.findById(id)
                .map(existingEntity -> {
                    usuarioMapper.updateEntityFromDTO(usuarioDTO, existingEntity);
                    UsuarioEntity updatedEntity = usuarioRepository.save(existingEntity);
                    return usuarioMapper.toDTO(updatedEntity);
                });
    }

    public boolean deleteById(Integer id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // CORREGIDO: email → correo
    public Optional<UsuarioDTO> findByCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo)
                .map(usuarioMapper::toDTO);
    }

    public boolean existsByCorreo(String correo) {
        return usuarioRepository.existsByCorreo(correo);
    }

    public List<UsuarioDTO> findByRol(Integer idRol) {
        List<UsuarioEntity> entities = usuarioRepository.findByRol_IdRol(idRol);
        return usuarioMapper.toDTOList(entities);
    }

    public List<UsuarioDTO> findByDepartamento(String departamento) {
        List<UsuarioEntity> entities = usuarioRepository.findByDepartamento(departamento);
        return usuarioMapper.toDTOList(entities);
    }

    public List<UsuarioDTO> findByNombreOrApellido(String texto) {
        List<UsuarioEntity> entities = usuarioRepository.findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase(texto, texto);
        return usuarioMapper.toDTOList(entities);
    }

    public List<UsuarioDTO> findInstructores() {
        List<UsuarioEntity> entities = usuarioRepository.findInstructores();
        return usuarioMapper.toDTOList(entities);
    }

    public List<UsuarioDTO> findEstudiantes() {
        List<UsuarioEntity> entities = usuarioRepository.findEstudiantes();
        return usuarioMapper.toDTOList(entities);
    }

    public long countByRol(Integer rolId) {
        return usuarioRepository.countByRol(rolId);
    }

    public long count() {
        return usuarioRepository.count();
    }
}
