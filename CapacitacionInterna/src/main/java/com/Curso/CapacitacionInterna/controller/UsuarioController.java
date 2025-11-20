package com.Curso.CapacitacionInterna.controller;

import com.Curso.CapacitacionInterna.dto.LoginDTO;
import com.Curso.CapacitacionInterna.dto.UsuarioCreateDTO;
import com.Curso.CapacitacionInterna.dto.UsuarioDTO;
import com.Curso.CapacitacionInterna.model.Usuario;
import com.Curso.CapacitacionInterna.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
@Tag(name = "Usuario", description = "Controlador para gestión de usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/hola")
    @Operation(summary = "Método de prueba", description = "Retorna un saludo de prueba")
    public String holaMundo() {
        return "¡Hola Mundo desde el UsuarioController!";
    }

    @PostMapping
    @Operation(summary = "Crear usuario", description = "Crea un nuevo usuario en el sistema")
    public ResponseEntity<UsuarioDTO> crearUsuario(@RequestBody UsuarioCreateDTO usuarioCreateDTO) {
        try {
            UsuarioDTO usuarioCreado = usuarioService.crearUsuario(usuarioCreateDTO);
            return ResponseEntity.ok(usuarioCreado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    @Operation(summary = "Obtener todos los usuarios", description = "Retorna lista de todos los usuarios")
    public ResponseEntity<List<UsuarioDTO>> obtenerTodosLosUsuarios() {
        List<UsuarioDTO> usuarios = usuarioService.obtenerTodosLosUsuarios();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener usuario por ID", description = "Retorna un usuario específico por su ID")
    public ResponseEntity<UsuarioDTO> obtenerUsuarioPorId(@PathVariable Long id) {
        Optional<UsuarioDTO> usuario = usuarioService.obtenerUsuarioPorId(id);
        return usuario.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar usuario", description = "Actualiza los datos de un usuario existente")
    public ResponseEntity<UsuarioDTO> actualizarUsuario(@PathVariable Long id, @RequestBody UsuarioCreateDTO usuarioCreateDTO) {
        try {
            UsuarioDTO usuarioActualizado = usuarioService.actualizarUsuario(id, usuarioCreateDTO);
            return ResponseEntity.ok(usuarioActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar usuario", description = "Elimina un usuario del sistema")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        try {
            usuarioService.eliminarUsuario(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/login")
    @Operation(summary = "Login de usuario", description = "Autentica un usuario y retorna sus datos incluyendo el rol")
    public ResponseEntity<UsuarioDTO> login(@RequestBody LoginDTO loginDTO) {
        try {
            UsuarioDTO usuario = usuarioService.login(loginDTO);
            return ResponseEntity.ok(usuario);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/rol/{rol}")
    @Operation(summary = "Obtener usuarios por rol", description = "Retorna todos los usuarios con un rol específico")
    public ResponseEntity<List<UsuarioDTO>> obtenerUsuariosPorRol(@PathVariable Usuario.Rol rol) {
        List<UsuarioDTO> usuarios = usuarioService.obtenerUsuariosPorRol(rol);
        return ResponseEntity.ok(usuarios);
    }
}