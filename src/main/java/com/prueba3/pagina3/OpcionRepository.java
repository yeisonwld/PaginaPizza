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
        // 10 pizzas precargadas con precios por tamaño
        opciones.add(new Opcion(1L, "Hawaiana", 18000, 26000, 32000, "/img/hawaiana.png"));
        opciones.add(new Opcion(2L, "Mexicana", 20000, 28000, 35000, "/img/mexicana.png"));
        opciones.add(new Opcion(3L, "Napolitana", 17000, 25000, 31000, "/img/napolitana.png"));
        opciones.add(new Opcion(4L, "Vegetariana", 19000, 27000, 34000, "/img/vegetariana.png"));
        opciones.add(new Opcion(5L, "Cuatro quesos", 21000, 30000, 37000, "/img/cuatro_quesos.png"));
        opciones.add(new Opcion(6L, "Pollo y champiñón", 20000, 29000, 36000, "/img/pollo_champinon.png"));
        opciones.add(new Opcion(7L, "Jamón y queso", 18000, 26000, 32000, "/img/jamon_queso.png"));
        opciones.add(new Opcion(8L, "Peperoni", 19000, 27000, 34000, "/img/peperoni.png"));
        opciones.add(new Opcion(9L, "Mar y tierra", 23000, 32000, 40000, "/img/mar_tierra.png"));
        opciones.add(new Opcion(10L, "BBQ especial", 22000, 31000, 39000, "/img/bbq_especial.png"));
        
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