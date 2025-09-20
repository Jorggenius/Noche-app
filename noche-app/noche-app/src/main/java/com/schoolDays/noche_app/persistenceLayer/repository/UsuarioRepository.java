package com.schoolDays.noche_app.persistenceLayer.repository;

import com.schoolDays.noche_app.persistenceLayer.entity.UsuarioEntity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Integer> {

    Optional<UsuarioEntity> findByEmail(String email);

    boolean existsByEmail(String email);

    List<UsuarioEntity> findByRol_IdRol(Integer idRol);

    List<UsuarioEntity> findByDepartamento(String departamento);

    List<UsuarioEntity> findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase(String nombre, String apellido);

    List<UsuarioEntity> findByRol_IdRolAndDepartamento(Integer idRol, String departamento);

    @Query("SELECT COUNT(u) FROM UsuarioEntity u WHERE u.rol.idRol = :rolId")
    long countByRol(@Param("rolId") Integer rolId);

    @Query("SELECT DISTINCT u FROM UsuarioEntity u WHERE SIZE(u.cursosCreados) > 0")
    List<UsuarioEntity> findUsuariosConCursos();

    @Query("SELECT u FROM UsuarioEntity u WHERE u.rol.nombreRol = 'INSTRUCTOR'")
    List<UsuarioEntity> findInstructores();

    @Query("SELECT u FROM UsuarioEntity u WHERE u.rol.nombreRol = 'USER'")
    List<UsuarioEntity> findEstudiantes();
}
