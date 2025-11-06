package com.prueba3.pagina3;

import jakarta.persistence.*;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Column;

@Entity
public class ItemCarrito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "opcion_id", nullable = false)
    private Opcion opcion;
    @Column(nullable = false)
    private String tamano;
    @Column(nullable = false)
    private int cantidad;
    @Column(nullable = false)
    private double precioUnitario;
    @Column(nullable = false)
    private double subtotal;

    public ItemCarrito() {}

    public ItemCarrito(Long id, Opcion opcion, String tamano, int cantidad) {
        this.id = id;
        this.opcion = opcion;
        this.tamano = tamano;
        this.cantidad = cantidad;
        this.precioUnitario = opcion.getPrecioPorTamano(tamano);
        this.subtotal = this.precioUnitario * cantidad;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Opcion getOpcion() { return opcion; }
    public void setOpcion(Opcion opcion) { 
        this.opcion = opcion;
        actualizarSubtotal();
    }

    public String getTamano() { return tamano; }
    public void setTamano(String tamano) { 
        this.tamano = tamano;
        if (opcion != null) {
            this.precioUnitario = opcion.getPrecioPorTamano(tamano);
        }
        actualizarSubtotal();
    }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { 
        this.cantidad = cantidad;
        actualizarSubtotal();
    }

    public double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(double precioUnitario) { 
        this.precioUnitario = precioUnitario;
        actualizarSubtotal();
    }

    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }

    // Metodo auxiliar para actualizar el subtotal
    private void actualizarSubtotal() {
        this.subtotal = this.precioUnitario * this.cantidad;
    }

    // Metodo para obtener el nombre completo del item (pizza + tamaño)
    public String getNombreCompleto() {
        return opcion.getNombre() + " (" + tamano + ")";
    }
}