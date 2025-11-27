package models;

import modelo.Producto;

public class ItemCarrito {
    private Producto producto;
    private Integer cantidad;

    public ItemCarrito(Producto producto, Integer cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
    
    public String getNombreProducto() {
        return this.producto.getNombre();
    }
    
    public Double getSubtotal() {
        return this.producto.getPrecio() * this.cantidad;
    }

    @Override
    public String toString() {
        return producto.getNombre() + " - Cantidad: " + cantidad;
    }
}
