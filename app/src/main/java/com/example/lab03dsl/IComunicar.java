package com.example.lab03dsl;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public interface IComunicar extends Serializable {

    public void VerEstudianteMapa(Posicion posricion);
    public  void EliminarEstudiante(Posicion posicion);
 }
