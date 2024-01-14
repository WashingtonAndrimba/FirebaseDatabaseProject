package com.example.firebasedatabaseproject;
public class Producto {
    private String nombre;
    private int stock;

    private String codigo;

    private double precioCosto;
    private double precioVenta;
    public Producto() {


    }

    public Producto(String codigo, String nombre, int stock, double precioCosto, double precioVenta) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.stock = stock;
        this.precioCosto = precioCosto;
        this.precioVenta = precioVenta;

    }
    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public int getStock() {
        return stock;
    }

    public double getPrecioCosto() {
        return precioCosto;
    }

    public double getPrecioVenta() {
        return precioVenta;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }


}
