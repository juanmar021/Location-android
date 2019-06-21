package com.example.lab03dsl;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.lab03dsl.SQLite.SQLiteManager;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback , GoogleMap.OnMapClickListener, View.OnClickListener {

    private GoogleMap mMap;
    private static final float camera_zoom = 15;
    public boolean seleccionar_ubicacion=false;// SI ES TRUE QUIERE DECIR QUE SE ESTA REGISTRANDO UN ESTUDIANTE
     private Button btnSeleccionar;
    private LatLng coordenadas_select;
    private Posicion posicion_select=null;//ESTUDIANTE SELECCIONADO
    SQLiteManager dbManager;

    private boolean listarPosiciones=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        btnSeleccionar=findViewById(R.id.btnSeleccionar);
        mapFragment.getMapAsync(this);
        dbManager = new SQLiteManager(this);

        this.posicion_select = (Posicion) getIntent().getSerializableExtra("posicion");
        this.seleccionar_ubicacion =   getIntent().getBooleanExtra("estudianteNuevo",false);
        this.listarPosiciones=  getIntent().getBooleanExtra("listar",false);


        btnSeleccionar.setOnClickListener(this);

    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        if(!listarPosiciones) {

            if (posicion_select != null)//SI HAY UN ESTUDIANTE SELECCIONADO ENTONCES QUE LO MUESTRE
            {
                AgregarMarcadorMapa(posicion_select);
            } else {
                LatLng monteria = new LatLng(8.7571952, -75.8990509);
                VolverPosicion(monteria);//muevo la camara hacia monteria

            }
        }
        else
        {
            listarPosiciones();
        }
        mMap.setOnMapClickListener(this);


    }

    @Override
    public void onMapClick(LatLng latLng) {

        if(this.seleccionar_ubicacion)
        {


            mMap.clear();
             mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    //.icon(bitmapDescriptorFromVector(MapsActivity.this, R.drawable.ic_person_point))
                    .title("Ubicación del estudiante")

            );
             VolverPosicion(latLng);

            btnSeleccionar.setVisibility(View.VISIBLE);
            coordenadas_select=latLng;
        }


     }


    private void AgregarMarcadorMapa(Posicion posicion)
    {
        LatLng latLng= new LatLng(posicion.getLat(),posicion.getLng());
        mMap.clear();
        Marker marcador_ = mMap.addMarker(new MarkerOptions()
                .position(latLng)
                //.icon(bitmapDescriptorFromVector(MapsActivity.this, R.drawable.ic_person_point))
                .title(posicion.getNombres()+" - "+posicion.getDireccion())

        );
        VolverPosicion(latLng);
        // marcador_.setTag(listaPuntos.get(i));
    }
    private void VolverPosicion(LatLng miLatLng) {// DADA UNA COORDENADA MUEVE LA CAMARA HACIA ELLA

        //     mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        CameraPosition camPos = new CameraPosition.Builder()
                .target(miLatLng)   //Centramos el mapa en Madrid
                .zoom(camera_zoom)         //Establecemos el zoom en 19
                .bearing(0)      //Establecemos la orientación con el noreste arriba
                .tilt(0)         //Bajamos el punto de vista de la cámara 70 grados
                .build();

        CameraUpdate miUbicacion = CameraUpdateFactory.newCameraPosition(camPos);
        mMap.animateCamera(miUbicacion);
    }

    private  void listarPosiciones()//LISTA TODAS LAS POSIONES REGISTRADAS EN LA DB Y LAS MUESTRA EN EL MAPA
    {
        //Toast.makeText(MainActivity.this, "Cargando datos de la DB", Toast.LENGTH_SHORT).show();

        ArrayList<Posicion> arrayList= dbManager.getPosiciones();




         if (arrayList.size() > 0) {
             mMap.clear();

            LatLng ultpos = null;

            for (int i = 0; i < arrayList.size(); i++) {



                Marker marcador_ = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng((arrayList.get(i).getLat())
                                , ( arrayList.get(i).getLng())))
                        .title(arrayList.get(i).getNombres())

                );
                marcador_.setTag(arrayList.get(i));

                ultpos = new LatLng((arrayList.get(i).getLat())//ULTIMA POSICION
                        , ( arrayList.get(i).getLng()));
            }

            VolverPosicion(ultpos);//DIRIGIMOS LA CAMARA HACIA LA ULTIMA POSICION

        }



    }

    @Override
    public void onClick(View v) {

        MainActivity.COORDENADAS_SELECT=this.coordenadas_select;
         finish();
         System.out.println("CLICK SELECCIONAR");
    }
}
