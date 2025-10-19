package com.prueba3.pagina3;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpSession;

@Controller
public class OpcionController {

    private final OpcionService opcionService;

    public OpcionController(OpcionService opcionService) {
        this.opcionService = opcionService;
    }

    @GetMapping("/")
    public String mostrarOpciones(Model model) {
        model.addAttribute("opciones", opcionService.listarOpciones());
        return "seleccion";
    }

    @PostMapping("/resultado")
    public String mostrarResultado(@RequestParam("opcionSeleccionada") Long id, Model model) {
        Opcion seleccionada = opcionService.obtenerPorId(id);
        model.addAttribute("seleccionada", seleccionada);
        return "resultado";
    }

    @PostMapping("/hacer-pedido")
    public String hacerPedido(@RequestParam("opcionSeleccionada") Long id, 
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        
        String emailUsuario = (String) session.getAttribute("usuarioEmail");
        
        if (emailUsuario == null) {
            session.setAttribute("pizzaSeleccionada", id);
            redirectAttributes.addFlashAttribute("mensaje", "Por favor inicia sesión para continuar con tu pedido");
            return "redirect:/login";
        }
        
        session.setAttribute("pizzaSeleccionada", id);
        return "redirect:/carrito";
    }
    
    @GetMapping("/carrito")
    public String mostrarCarrito(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        
        String emailUsuario = (String) session.getAttribute("usuarioEmail");
        if (emailUsuario == null) {
            redirectAttributes.addFlashAttribute("error", "Debes iniciar sesión primero");
            return "redirect:/login";
        }
        
        Long pizzaId = (Long) session.getAttribute("pizzaSeleccionada");
        if (pizzaId == null) {
            redirectAttributes.addFlashAttribute("error", "No hay ninguna pizza seleccionada");
            return "redirect:/";
        }
        
        Opcion pizzaSeleccionada = opcionService.obtenerPorId(pizzaId);
        
        model.addAttribute("pizza", pizzaSeleccionada);
        model.addAttribute("usuarioEmail", emailUsuario);
        
        return "carrito";
    }

    
    @GetMapping("/registro")
    public String mostrarFormularioRegistro() {
        return "registro";
    }

    @PostMapping("/registro")
    public String procesarRegistro(
            @RequestParam("nombre") String nombre,
            @RequestParam("apellidos") String apellidos,
            @RequestParam("telefono") String telefono,
            @RequestParam("direccion") String direccion,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            HttpSession session,
            Model model) {
        
        // Verificar si el email ya existe
        if (opcionService.emailExiste(email)) {
            model.addAttribute("error", "Este email ya está registrado");
            return "registro";
        }
        
        // AGREGAR a la lista vacía de OpcionRepository
        opcionService.registrarUsuario(nombre, apellidos, telefono, direccion, email, password);
        
        // Guardar en sesión
        session.setAttribute("usuarioEmail", email);
        session.setAttribute("usuarioNombre", nombre);
        
        // Si había pizza seleccionada, ir al carrito
        Long pizzaId = (Long) session.getAttribute("pizzaSeleccionada");
        if (pizzaId != null) {
            return "redirect:/carrito";
        }
        
        model.addAttribute("mensaje", "¡Registro exitoso! Usuario agregado a la lista.");
        return "registro";
    }

    
    @GetMapping("/login")
    public String mostrarLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String procesarLogin(
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            HttpSession session,
            Model model) {
        
        if (email.isEmpty() || password.isEmpty()) {
            model.addAttribute("error", "Por favor completa todos los campos");
            return "login";
        }
        
        // BUSCAR credenciales en la lista de OpcionRepository
        java.util.Map<String, String> usuario = opcionService.validarLogin(email, password);
        
        if (usuario == null) {
            model.addAttribute("error", "Email o contraseña incorrectos");
            return "login";
        }
        
        // Login exitoso
        session.setAttribute("usuarioEmail", email);
        session.setAttribute("usuarioNombre", usuario.get("nombre"));
        
        // Si había pizza seleccionada, ir al carrito
        Long pizzaId = (Long) session.getAttribute("pizzaSeleccionada");
        if (pizzaId != null) {
            return "redirect:/carrito";
        }
        
        model.addAttribute("mensaje", "¡Bienvenido " + usuario.get("nombre") + "!");
        return "login";
    }
    
    
    @GetMapping("/usuarios")
    public String verUsuarios(Model model) {
        java.util.List<java.util.Map<String, String>> usuarios = opcionService.obtenerTodosLosUsuarios();
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("total", usuarios.size());
        return "usuarios";
    }
    
    @GetMapping("/logout")
    public String cerrarSesion(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("mensaje", "Sesión cerrada exitosamente");
        return "redirect:/";
    }
}