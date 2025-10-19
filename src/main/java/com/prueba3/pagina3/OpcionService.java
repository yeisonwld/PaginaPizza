package com.prueba3.pagina3;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class OpcionService {

    private final OpcionRepository opcionRepository;

    public OpcionService(OpcionRepository opcionRepository) {
        this.opcionRepository = opcionRepository;
    }

    // Métodos de pizzas
    public List<Opcion> listarOpciones() {
        return opcionRepository.findAll();
    }

    public Opcion obtenerPorId(Long id) {
        return opcionRepository.findById(id);
    }
    
    // ========== MÉTODOS DE USUARIOS ==========
    
    public void registrarUsuario(String nombre, String apellidos, String telefono, 
                                String direccion, String email, String password) {
        opcionRepository.registrarUsuario(nombre, apellidos, telefono, direccion, email, password);
    }
    
    public boolean emailExiste(String email) {
        return opcionRepository.emailExiste(email);
    }
    
    public Map<String, String> validarLogin(String email, String password) {
        return opcionRepository.buscarUsuarioPorCredenciales(email, password);
    }
    
    public List<Map<String, String>> obtenerTodosLosUsuarios() {
        return opcionRepository.obtenerTodosLosUsuarios();
    }
}