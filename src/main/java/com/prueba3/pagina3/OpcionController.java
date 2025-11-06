package com.prueba3.pagina3;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpSession;

@Controller
public class OpcionController {

    private final OpcionService opcionService;
    private final CarritoService carritoService;

    public OpcionController(OpcionService opcionService, CarritoService carritoService) {
        this.opcionService = opcionService;
        this.carritoService = carritoService;
    }

    @GetMapping("/")
    public String mostrarOpciones(Model model) {
        model.addAttribute("opciones", opcionService.listarOpciones());
        model.addAttribute("cantidadCarrito", carritoService.obtenerCantidadItems());
        return "seleccion";
    }

    @PostMapping("/agregar")
    public String agregarAlCarrito(@RequestParam Long opcionId, @RequestParam String tamano, @RequestParam int cantidad, RedirectAttributes redirectAttributes) {
        Opcion opcion = opcionService.obtenerPorId(opcionId);
        if (opcion != null && cantidad > 0) {
            carritoService.agregarItem(opcion, tamano, cantidad);
            redirectAttributes.addFlashAttribute("mensaje", "Pizza agregada al carrito exitosamente");
        } else {
            redirectAttributes.addFlashAttribute("error", "Error al agregar la pizza al carrito");
        }
        
        return "redirect:/";
    }
    
    @GetMapping("/carrito")
    public String mostrarCarrito(Model model) {
        model.addAttribute("itemsCarrito", carritoService.obtenerItems());
        model.addAttribute("totalCarrito", carritoService.calcularTotal());
        model.addAttribute("cantidadItems", carritoService.obtenerCantidadItems());
        model.addAttribute("carritoVacio", carritoService.estaVacio());
        return "carrito";
    }

    @PostMapping("/carrito/eliminar")
    public String eliminarDelCarrito(@RequestParam Long itemId, RedirectAttributes redirectAttributes) {
        if (carritoService.eliminarItem(itemId)) {
            redirectAttributes.addFlashAttribute("mensaje", "Item eliminado del carrito");
        } else {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el item");
        }
        
        return "redirect:/carrito";
    }

    @PostMapping("/carrito/vaciar")
    public String vaciarCarrito(RedirectAttributes redirectAttributes) {
        carritoService.vaciarCarrito();
        redirectAttributes.addFlashAttribute("mensaje", "Carrito vaciado exitosamente");
        return "redirect:/carrito";
    }

    @PostMapping("/carrito/actualizar")
    public String actualizarCantidadCarrito(@RequestParam Long itemId, @RequestParam int cantidad, RedirectAttributes redirectAttributes) {
        if (carritoService.actualizarCantidad(itemId, cantidad)) {
            redirectAttributes.addFlashAttribute("mensaje", "Cantidad actualizada exitosamente");
        } else {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar la cantidad");
        }
        return "redirect:/carrito";
    }

    @PostMapping("/confirmar")
    public String confirmarPedido(Model model, RedirectAttributes redirectAttributes, HttpSession session) {
        // Verificar si el usuario está autenticado
        String usuarioEmail = (String) session.getAttribute("usuarioEmail");
        if (usuarioEmail == null) {
            redirectAttributes.addFlashAttribute("error", "Debes iniciar sesión para confirmar tu pedido");
            redirectAttributes.addFlashAttribute("redirectAfterLogin", "/carrito");
            return "redirect:/login";
        }
        
        if (carritoService.estaVacio()) {
            redirectAttributes.addFlashAttribute("error", "No hay items en el carrito para confirmar");
            return "redirect:/carrito";
        }
        
        // Preparar datos para la vista de confirmación
        model.addAttribute("itemsPedido", carritoService.obtenerItems());
        model.addAttribute("totalPedido", carritoService.calcularTotal());
        model.addAttribute("cantidadItems", carritoService.obtenerCantidadItems());
        model.addAttribute("usuarioEmail", usuarioEmail);
        
        // Vaciar carrito después de confirmar
        carritoService.vaciarCarrito();
        
        return "resultado";
    }

    
    @GetMapping("/registro")
    public String mostrarFormularioRegistro() {
        return "registro";
    }

    @PostMapping("/registro")
    public String procesarRegistro(
            @RequestParam String nombre,
            @RequestParam String apellidos,
            @RequestParam String telefono,
            @RequestParam String direccion,
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session,
            Model model) {
        
        // Verificar si el email ya existe
        if (opcionService.emailExiste(email)) {
            model.addAttribute("error", "Este email ya está registrado");
            return "registro";
        }
        
        // Persistir usuario en la base de datos
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
            Model model,
            RedirectAttributes redirectAttributes) {
        
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
        
        // Verificar si hay una redirección pendiente
        String redirectAfterLogin = (String) redirectAttributes.getFlashAttributes().get("redirectAfterLogin");
        if (redirectAfterLogin != null) {
            return "redirect:" + redirectAfterLogin;
        }
        
        // Si había pizza seleccionada, ir al carrito
        Long pizzaId = (Long) session.getAttribute("pizzaSeleccionada");
        if (pizzaId != null) {
            return "redirect:/carrito";
        }
        
        // Por defecto, redirigir a la página principal
        redirectAttributes.addFlashAttribute("mensaje", "¡Bienvenido " + usuario.get("nombre") + "!");
        return "redirect:/";
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