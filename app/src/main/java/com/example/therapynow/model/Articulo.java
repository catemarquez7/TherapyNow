package com.example.therapynow.model;

public class Articulo {
    private String id;
    private String titulo;
    private String descripcion;
    private String contenido;

    public Articulo() {
    }

    public Articulo(String id, String titulo, String descripcion, String contenido) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.contenido = contenido;
    }

    public String getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getDescripcion() { return descripcion; }
    public String getContenido() { return contenido; }
}