package com.example.fancomponentes;

public class Manual {
    private String componenteId;
    private String nombreComponente;
    private int cantidad;

    public Manual(String componenteId, String nombreComponente, int cantidad) {
        this.componenteId = componenteId;
        this.nombreComponente = nombreComponente;
        this.cantidad = cantidad;
    }

    public String getComponenteId() {
        return componenteId;
    }

    public void setComponenteId(String componenteId) {
        this.componenteId = componenteId;
    }

    public String getNombreComponente() {
        return nombreComponente;
    }

    public void setNombreComponente(String nombreComponente) {
        this.nombreComponente = nombreComponente;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}