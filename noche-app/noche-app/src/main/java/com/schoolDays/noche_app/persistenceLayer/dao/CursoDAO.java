package com.schoolDays.noche_app.persistenceLayer.dao;

import com.schoolDays.noche_app.businessLayer.CursoDTO;
import com.schoolDays.noche_app.persistenceLayer.entity.CursoEntity;
import com.schoolDays.noche_app.persistenceLayer.mapper.CursoMapper;
import com.schoolDays.noche_app.persistenceLayer.repository.CursoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CursoDAO {

    private final CursoRepository cursoRepository;
    private final CursoMapper cursoMapper;

    public CursoDTO save(CursoDTO cursoDTO) {
        CursoEntity entity = cursoMapper.toEntity(cursoDTO);
        CursoEntity savedEntity = cursoRepository.save(entity);
        return cursoMapper.toDTO(savedEntity);
    }

    public Optional<CursoDTO> findById(Integer id) {
        return cursoRepository.findById(id)
                .map(cursoMapper::toDTO);
    }

    public List<CursoDTO> findAll() {
        List<CursoEntity> entities = cursoRepository.findAll();
        return cursoMapper.toDTOList(entities);
    }

    public Optional<CursoDTO> update(Integer id, CursoDTO cursoDTO) {
        return cursoRepository.findById(id)
                .map(existingEntity -> {
                    cursoMapper.updateEntityFromDTO(cursoDTO, existingEntity);
                    CursoEntity updatedEntity = cursoRepository.save(existingEntity);
                    return cursoMapper.toDTO(updatedEntity);
                });
    }

    public boolean deleteById(Integer id) {
        if (cursoRepository.existsById(id)) {
            cursoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<CursoDTO> findByCreador(Integer idUsuario) {
        List<CursoEntity> entities = cursoRepository.findByCreadoPor_IdUsuario(idUsuario);
        return cursoMapper.toDTOList(entities);
    }

    public List<CursoDTO> findByNivel(CursoEntity.Nivel nivel) {
        List<CursoEntity> entities = cursoRepository.findByNivel(nivel);
        return cursoMapper.toDTOList(entities);
    }

    public List<CursoDTO> findByTitulo(String titulo) {
        List<CursoEntity> entities = cursoRepository.findByTituloContainingIgnoreCase(titulo);
        return cursoMapper.toDTOList(entities);
    }

    public List<CursoDTO> findByFechaCreacion(LocalDate fechaInicio, LocalDate fechaFin) {
        List<CursoEntity> entities = cursoRepository.findByFechaCreacionBetween(fechaInicio, fechaFin);
        return cursoMapper.toDTOList(entities);
    }

    public List<CursoDTO> findMasRecientes() {
        List<CursoEntity> entities = cursoRepository.findAllByOrderByFechaCreacionDesc();
        return cursoMapper.toDTOList(entities);
    }

    public List<CursoDTO> findMasPopulares() {
        List<CursoEntity> entities = cursoRepository.findCursosMasPopulares();
        return cursoMapper.toDTOList(entities);
    }

    public List<CursoDTO> findCursosConModulos() {
        List<CursoEntity> entities = cursoRepository.findCursosConModulos();
        return cursoMapper.toDTOList(entities);
    }

    public long countByNivel(CursoEntity.Nivel nivel) {
        return cursoRepository.countByNivel(nivel);
    }

    public long count() {
        return cursoRepository.count();
    }
}

