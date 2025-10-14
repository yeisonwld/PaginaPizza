package com.prueba3.pagina3;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
}