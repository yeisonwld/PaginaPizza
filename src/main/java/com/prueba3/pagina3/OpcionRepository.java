package com.prueba3.pagina3;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class OpcionRepository {

    // Lista de pizzas
    private List<Opcion> opciones = new ArrayList<>();
    
    // NUEVO: Lista VACÍA de usuarios que se irá llenando con el registro
    private List<Map<String, String>> usuariosRegistrados = new ArrayList<>();

    public OpcionRepository() {
        // Solo pizzas al inicio
        opciones.add(new Opcion(1L, "Pizza pequeña", 10000, "/img/basico.png"));
        opciones.add(new Opcion(2L, "Pizza mediana", 20000, "/img/intermedio.png"));
        opciones.add(new Opcion(3L, "Pizza grande", 30000, "/img/premium.png"));
        
        // Lista de usuarios empieza VACÍA (se llena con el formulario)
    }

    // ========== MÉTODOS DE PIZZAS ==========
    
    public List<Opcion> findAll() {
        return opciones;
    }

    public Opcion findById(Long id) {
        return opciones.stream()
                .filter(o -> o.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
    
    // ========== MÉTODOS DE USUARIOS ==========
    
    // Registrar nuevo usuario (agregar a la lista)
    public void registrarUsuario(String nombre, String apellidos, String telefono, 
                                 String direccion, String email, String password) {
        Map<String, String> nuevoUsuario = new HashMap<>();
        nuevoUsuario.put("nombre", nombre);
        nuevoUsuario.put("apellidos", apellidos);
        nuevoUsuario.put("telefono", telefono);
        nuevoUsuario.put("direccion", direccion);
        nuevoUsuario.put("email", email);
        nuevoUsuario.put("password", password);
        
        usuariosRegistrados.add(nuevoUsuario);
    }
    
    // Verificar si un email ya existe en la lista
    public boolean emailExiste(String email) {
        for (Map<String, String> usuario : usuariosRegistrados) {
            if (usuario.get("email").equals(email)) {
                return true;
            }
        }
        return false;
    }
    
    // Buscar usuario por email y password (para login)
    public Map<String, String> buscarUsuarioPorCredenciales(String email, String password) {
        for (Map<String, String> usuario : usuariosRegistrados) {
            if (usuario.get("email").equals(email) && usuario.get("password").equals(password)) {
                return usuario;
            }
        }
        return null;
    }
    
    // Obtener todos los usuarios registrados (para ver la lista)
    public List<Map<String, String>> obtenerTodosLosUsuarios() {
        return usuariosRegistrados;
    }
}