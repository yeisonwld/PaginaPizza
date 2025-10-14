package com.prueba3.pagina3;


import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class OpcionRepository {

    // Lista que actúa como base de datos en memoria
    private List<Opcion> opciones = new ArrayList<>();

    public OpcionRepository() {
        opciones.add(new Opcion(1L, "Pizza pequeña", 10000, "/img/basico.png"));
        opciones.add(new Opcion(2L, "Pizza mediana", 20000, "/img/intermedio.png"));
        opciones.add(new Opcion(3L, "Pizza grande", 30000, "/img/premium.png"));
    }

    public List<Opcion> findAll() {
        return opciones;
    }

    public Opcion findById(Long id) {
        return opciones.stream()
                .filter(o -> o.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}