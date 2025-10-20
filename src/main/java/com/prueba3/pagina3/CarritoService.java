package com.prueba3.pagina3;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CarritoService {
    
    private List<ItemCarrito> itemsCarrito = new ArrayList<>();
    private Long siguienteId = 1L;

    // Agregar item al carrito
    public void agregarItem(Opcion opcion, String tamano, int cantidad) {
        // Verificar si ya existe un item igual (misma pizza y tamaño)
        ItemCarrito itemExistente = buscarItemExistente(opcion.getId(), tamano);
        
        if (itemExistente != null) {
            // Si existe, aumentar la cantidad
            itemExistente.setCantidad(itemExistente.getCantidad() + cantidad);
        } else {
            // Si no existe, crear nuevo item
            ItemCarrito nuevoItem = new ItemCarrito(siguienteId++, opcion, tamano, cantidad);
            itemsCarrito.add(nuevoItem);
        }
    }

    // Buscar item existente por pizza y tamaño
    private ItemCarrito buscarItemExistente(Long opcionId, String tamano) {
        return itemsCarrito.stream()
                .filter(item -> item.getOpcion().getId().equals(opcionId) && 
                               item.getTamano().equalsIgnoreCase(tamano))
                .findFirst()
                .orElse(null);
    }

    // Obtener todos los items del carrito
    public List<ItemCarrito> obtenerItems() {
        return new ArrayList<>(itemsCarrito);
    }

    // Eliminar item por ID
    public boolean eliminarItem(Long itemId) {
        return itemsCarrito.removeIf(item -> item.getId().equals(itemId));
    }

    // Vaciar carrito completo
    public void vaciarCarrito() {
        itemsCarrito.clear();
        siguienteId = 1L;
    }

    // Calcular total del carrito
    public double calcularTotal() {
        return itemsCarrito.stream()
                .mapToDouble(ItemCarrito::getSubtotal)
                .sum();
    }

    // Obtener cantidad total de items
    public int obtenerCantidadItems() {
        return itemsCarrito.stream()
                .mapToInt(ItemCarrito::getCantidad)
                .sum();
    }

    // Verificar si el carrito está vacío
    public boolean estaVacio() {
        return itemsCarrito.isEmpty();
    }

    // Obtener item por ID
    public ItemCarrito obtenerItemPorId(Long itemId) {
        return itemsCarrito.stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst()
                .orElse(null);
    }

    // Actualizar cantidad de un item específico
    public boolean actualizarCantidad(Long itemId, int nuevaCantidad) {
        ItemCarrito item = obtenerItemPorId(itemId);
        if (item != null && nuevaCantidad > 0) {
            item.setCantidad(nuevaCantidad);
            return true;
        } else if (item != null && nuevaCantidad <= 0) {
            // Si la cantidad es 0 o negativa, eliminar el item
            return eliminarItem(itemId);
        }
        return false;
    }
}