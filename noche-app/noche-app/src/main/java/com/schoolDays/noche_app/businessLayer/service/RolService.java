package com.schoolDays.noche_app.businessLayer.service;

import java.util.List;
import com.schoolDays.noche_app.businessLayer.dto.RolDTO;

public interface RolService {


    RolDTO createRol(RolDTO rolDTO);

    RolDTO getRolById(Integer id);

    List<RolDTO> getAllRoles();

    RolDTO updateRol(Integer id, RolDTO rolDTO);

    void deleteRol(Integer id);

    RolDTO getRolByNombre(String nombreRol);

    boolean isNombreRolTaken(String nombreRol);

    long getTotalRolesCount();
}

