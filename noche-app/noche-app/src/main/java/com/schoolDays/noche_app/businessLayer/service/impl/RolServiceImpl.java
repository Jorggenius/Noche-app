package com.schoolDays.noche_app.businessLayer.service.impl;

import com.schoolDays.noche_app.businessLayer.RolDTO;
import com.schoolDays.noche_app.businessLayer.service.RolService;
import com.schoolDays.noche_app.persistenceLayer.dao.RolDAO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class RolServiceImpl implements RolService {

    private final RolDAO rolDAO;

    @Override
    public RolDTO createRol(RolDTO rolDTO) {
        log.info("Creando nuevo rol: {}", rolDTO.getNombreRol());

        validateRolData(rolDTO);

        if (rolDAO.existsByNombreRol(rolDTO.getNombreRol())) {
            throw new IllegalArgumentException("Ya existe un rol con el nombre: " + rolDTO.getNombreRol());
        }

        RolDTO createdRol = rolDAO.save(rolDTO);
        log.info("Rol creado exitosamente con ID: {}", createdRol.getIdRol());
        return createdRol;
    }

    @Override
    @Transactional(readOnly = true)
    public RolDTO getRolById(Integer id) {
        return rolDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<RolDTO> getAllRoles() {
        return rolDAO.findAll();
    }

    @Override
    public RolDTO updateRol(Integer id, RolDTO rolDTO) {
        log.info("Actualizando rol ID: {}", id);

        getRolById(id); // Verificar existencia
        validateRolData(rolDTO);

        if (rolDTO.getNombreRol() != null && rolDAO.existsByNombreRol(rolDTO.getNombreRol())) {
            RolDTO existingRol = rolDAO.findByNombreRol(rolDTO.getNombreRol()).orElse(null);
            if (existingRol != null && !existingRol.getIdRol().equals(id)) {
                throw new IllegalArgumentException("Ya existe otro rol con el nombre: " + rolDTO.getNombreRol());
            }
        }

        return rolDAO.update(id, rolDTO)
                .orElseThrow(() -> new RuntimeException("Error al actualizar rol"));
    }

    @Override
    public void deleteRol(Integer id) {
        log.info("Eliminando rol ID: {}", id);

        getRolById(id); // Verificar existencia

        if (!rolDAO.deleteById(id)) {
            throw new RuntimeException("Error al eliminar rol con ID: " + id);
        }

        log.info("Rol eliminado exitosamente ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public RolDTO getRolByNombre(String nombreRol) {
        return rolDAO.findByNombreRol(nombreRol)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con nombre: " + nombreRol));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isNombreRolTaken(String nombreRol) {
        return rolDAO.existsByNombreRol(nombreRol);
    }

    @Override
    @Transactional(readOnly = true)
    public long getTotalRolesCount() {
        return rolDAO.count();
    }

    private void validateRolData(RolDTO rolDTO) {
        if (rolDTO.getNombreRol() == null || rolDTO.getNombreRol().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del rol es obligatorio");
        }

        if (rolDTO.getNombreRol().length() > 50) {
            throw new IllegalArgumentException("El nombre del rol no puede exceder 50 caracteres");
        }

        // Validar que sea uno de los roles permitidos
        String nombreRol = rolDTO.getNombreRol().toUpperCase();
        if (!nombreRol.equals("ADMIN") && !nombreRol.equals("INSTRUCTOR") && !nombreRol.equals("USER")) {
            throw new IllegalArgumentException("Rol no válido. Solo se permiten: ADMIN, INSTRUCTOR, USER");
        }
    }
}