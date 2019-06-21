package com.example.lab03dsl;

import android.content.ContentValues;

import java.io.Serializable;

import static com.example.lab03dsl.SQLite.SQLiteHelper.DIRECCION;
import static com.example.lab03dsl.SQLite.SQLiteHelper.ID;
import static com.example.lab03dsl.SQLite.SQLiteHelper.LAT;
import static com.example.lab03dsl.SQLite.SQLiteHelper.LNG;
import static com.example.lab03dsl.SQLite.SQLiteHelper.NOMBRES;

public class Posicion implements Serializable {

    private int id;
    private String Nombres,Direccion;
    private Double lat,lng;
    public Posicion( ) {

    }
    public Posicion(int id, String nombres, String direccion, Double lat, Double lng) {
        this.id = id;
        Nombres = nombres;
        Direccion = direccion;
        this.lat = lat;
        this.lng = lng;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombres() {
        return Nombres;
    }

    public void setNombres(String nombres) {
        Nombres = nombres;
    }

    public String getDireccion() {
        return Direccion;
    }

    public void setDireccion(String direccion) {
        Direccion = direccion;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }


    public ContentValues toContentValues() {// PARA FACILITAR EL GUARDADO EN LA DB
        ContentValues values = new ContentValues();

        values.put(ID, getId());
        values.put(NOMBRES, getNombres());
        values.put(DIRECCION, getDireccion());
        values.put(LAT, getLat());
        values.put(LNG, getLng());

        return values;
    }
}
