package com.prueba3.pagina3;


import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OpcionService {

    private final OpcionRepository opcionRepository;

    public OpcionService(OpcionRepository opcionRepository) {
        this.opcionRepository = opcionRepository;
    }

    public List<Opcion> listarOpciones() {
        return opcionRepository.findAll();
    }

    public Opcion obtenerPorId(Long id) {
        return opcionRepository.findById(id);
    }
}