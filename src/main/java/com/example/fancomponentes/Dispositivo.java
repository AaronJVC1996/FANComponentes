package com.example.fancomponentes;

import javafx.beans.property.*;

public class Dispositivo {

    private final IntegerProperty nDispositivo;
    private final StringProperty nombre;
    private final DoubleProperty precio;
    private final StringProperty descripcion;
    private final IntegerProperty cantidad;

    public Dispositivo(int nDispositivo, String nombre, double precio, String descripcion, int cantidad) {
        this.nDispositivo = new SimpleIntegerProperty(nDispositivo);
        this.nombre = new SimpleStringProperty(nombre);
        this.precio = new SimpleDoubleProperty(precio);
        this.descripcion = new SimpleStringProperty(descripcion);
        this.cantidad = new SimpleIntegerProperty(cantidad);
    }

    public int getNDispositivo() {
        return nDispositivo.get();
    }

    public IntegerProperty nDispositivoProperty() {
        return nDispositivo;
    }

    public String getNombre() {
        return nombre.get();
    }

    public StringProperty nombreProperty() {
        return nombre;
    }

    public double getPrecio() {
        return precio.get();
    }

    public DoubleProperty precioProperty() {
        return precio;
    }

    public String getDescripcion() {
        return descripcion.get();
    }

    public StringProperty descripcionProperty() {
        return descripcion;
    }

    public int getCantidad() {
        return cantidad.get();
    }

    public IntegerProperty cantidadProperty() {
        return cantidad;
    }

    public void setNombre(String nombre) {
        this.nombre.set(nombre);
    }

    public void setPrecio(double precio) {
        this.precio.set(precio);
    }

    public void setDescripcion(String descripcion) {
        this.descripcion.set(descripcion);
    }

    public void setCantidad(int cantidad) {
        this.cantidad.set(cantidad);
    }
}
