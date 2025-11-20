package com.Curso.CapacitacionInterna.service;

import com.Curso.CapacitacionInterna.dto.LoginDTO;
import com.Curso.CapacitacionInterna.dto.UsuarioCreateDTO;
import com.Curso.CapacitacionInterna.dto.UsuarioDTO;
import com.Curso.CapacitacionInterna.model.Usuario;
import com.Curso.CapacitacionInterna.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Crear usuario
    public UsuarioDTO crearUsuario(UsuarioCreateDTO usuarioCreateDTO) {
        if (usuarioRepository.existsByEmail(usuarioCreateDTO.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(usuarioCreateDTO.getNombre());
        usuario.setEmail(usuarioCreateDTO.getEmail());
        usuario.setPassword(usuarioCreateDTO.getPassword()); // En producción se debe encriptar
        usuario.setRol(usuarioCreateDTO.getRol());
        usuario.setDepartamento(usuarioCreateDTO.getDepartamento());

        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        return convertirADTO(usuarioGuardado);
    }

    // Obtener todos los usuarios
    public List<UsuarioDTO> obtenerTodosLosUsuarios() {
        return usuarioRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Obtener usuario por ID
    public Optional<UsuarioDTO> obtenerUsuarioPorId(Long id) {
        return usuarioRepository.findById(id)
                .map(this::convertirADTO);
    }

    // Actualizar usuario
    public UsuarioDTO actualizarUsuario(Long id, UsuarioCreateDTO usuarioCreateDTO) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Verificar si el email ya existe y no es el mismo usuario
        if (!usuario.getEmail().equals(usuarioCreateDTO.getEmail()) && 
            usuarioRepository.existsByEmail(usuarioCreateDTO.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }

        usuario.setNombre(usuarioCreateDTO.getNombre());
        usuario.setEmail(usuarioCreateDTO.getEmail());
        
        // Solo actualizar password si se proporciona uno nuevo
        if (usuarioCreateDTO.getPassword() != null && !usuarioCreateDTO.getPassword().trim().isEmpty()) {
            usuario.setPassword(usuarioCreateDTO.getPassword());
        }
        
        usuario.setRol(usuarioCreateDTO.getRol());
        usuario.setDepartamento(usuarioCreateDTO.getDepartamento());

        Usuario usuarioActualizado = usuarioRepository.save(usuario);
        return convertirADTO(usuarioActualizado);
    }

    // Eliminar usuario
    public void eliminarUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado");
        }
        usuarioRepository.deleteById(id);
    }

    // Login
    public UsuarioDTO login(LoginDTO loginDTO) {
        Usuario usuario = usuarioRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("Credenciales inválidas"));

        // En producción se debe verificar password encriptado
        if (!usuario.getPassword().equals(loginDTO.getPassword())) {
            throw new RuntimeException("Credenciales inválidas");
        }

        return convertirADTO(usuario);
    }

    // Obtener usuarios por rol
    public List<UsuarioDTO> obtenerUsuariosPorRol(Usuario.Rol rol) {
        return usuarioRepository.findByRol(rol)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Método auxiliar para convertir entidad a DTO
    private UsuarioDTO convertirADTO(Usuario usuario) {
        return new UsuarioDTO(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getRol(),
                usuario.getDepartamento()
        );
    }
}