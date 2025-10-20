package com.prueba3.pagina3;

public class ItemCarrito {
    private Long id;
    private Opcion opcion;
    private String tamano;
    private int cantidad;
    private double precioUnitario;
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

    // Método auxiliar para actualizar el subtotal
    private void actualizarSubtotal() {
        this.subtotal = this.precioUnitario * this.cantidad;
    }

    // Método para obtener el nombre completo del item (pizza + tamaño)
    public String getNombreCompleto() {
        return opcion.getNombre() + " (" + tamano + ")";
    }
}