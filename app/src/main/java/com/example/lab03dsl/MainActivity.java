package com.example.lab03dsl;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.DropBoxManager;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lab03dsl.SQLite.SQLiteHelper;
import com.example.lab03dsl.SQLite.SQLiteManager;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener,IComunicar  {

    private EditText edtNmobre, edtDireccion,edtLatLng;
    private Button btnGuardarInfo, btnCargarJSON,btnBorrarDatos;
    private RecyclerView recyclerView;
     private AdaptadorLista adaptadorLista;
     FloatingActionButton btnListarTodos;//MUESTRA TODOS LAS POSCIONES EN EL MAPA
    android.support.design.widget.TextInputLayout txtLatLng;
    SQLiteManager dbManager;
    ProgressBar pCargando;
    TextView txtNoHayRegistros;
    private Posicion informacion;
    private ArrayList<Posicion> arrayList;
    String[] archivos;
    public  static LatLng  COORDENADAS_SELECT;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbManager = new SQLiteManager(this);

        edtNmobre = findViewById(R.id.edtt_nombres);
        edtDireccion = findViewById(R.id.edtt_direccion);
        btnGuardarInfo = findViewById(R.id.boton_guardar_info);
        btnCargarJSON = findViewById(R.id.boton_generar_json);
        btnBorrarDatos=findViewById(R.id.boton_borrar_datos);
        recyclerView = findViewById(R.id.lista_informacion);
        edtLatLng=findViewById(R.id.edt_lat_lng);
        txtNoHayRegistros=findViewById(R.id.txtNoHayRegistros);
        pCargando=findViewById(R.id.pCargando);
        txtLatLng=findViewById(R.id.txtLatLng);
        btnListarTodos=findViewById(R.id.fabListarTodos);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);

        adaptadorLista = new AdaptadorLista(MainActivity.this, new ArrayList<Posicion>(), MainActivity.this);
        recyclerView.setAdapter(adaptadorLista);

        archivos = fileList();

       // extraerMostrarInformacion(); (LAB 03)

        btnGuardarInfo.setOnClickListener(this);
        btnCargarJSON.setOnClickListener(this);
        btnBorrarDatos.setOnClickListener(this);
        txtLatLng.setOnClickListener(this);
        btnListarTodos.setOnClickListener(this);
        edtLatLng.setOnFocusChangeListener(new View.OnFocusChangeListener() {//CUANDO SE QUIERE AGREGAR UNA COORDENADA SE LLAMA EL MAPA
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {

                    Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                    intent.putExtra("estudianteNuevo",true);
                     startActivity(intent);



                } else {
                 }
            }
        });

        cargarDatosDB();//CARGA LOS DATOS GUARDADOS EN LA BASE DE DATOS


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.boton_guardar_info:
                //guardarInformacion();
                this.GuardarEstudiante();
                break;
            case R.id.boton_generar_json:
                leerArchivoJson();
                break;
            case R.id.boton_borrar_datos:
                borrarDatosDB();
                break;

            case R.id.fabListarTodos:
                if(arrayList.size()>0)
                {
                    Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                    intent.putExtra("listar",true);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(MainActivity.this,"La base de datos esta vacia",Toast.LENGTH_LONG).show();
                }


                break;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

         if(MainActivity.COORDENADAS_SELECT!=null){
            String lat="Lat: "+MainActivity.COORDENADAS_SELECT.latitude;
            String lng="Lng: "+MainActivity.COORDENADAS_SELECT.longitude;

            edtLatLng.setText(lat+"  -  "+lng);

        }
    }

    private void borrarDatosDB()
    {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){

                    case DialogInterface.BUTTON_POSITIVE:
                         long resultado=dbManager.borrarTabla(SQLiteHelper.TABLA_POSICIONES);

                         if(resultado>0)
                        {
                            Toast.makeText(MainActivity.this, "Base de datos borrada ", Toast.LENGTH_SHORT).show();
                            arrayList.clear();
                            adaptadorLista.setEstudiantes(arrayList);
                            txtNoHayRegistros.setVisibility(View.VISIBLE);

                        }
                         break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("¿Quiere eliminar toda la base de datos?").setPositiveButton("Sí", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }
    private  void cargarDatosDB()
    {
        //Toast.makeText(MainActivity.this, "Cargando datos de la DB", Toast.LENGTH_SHORT).show();

       arrayList= dbManager.getPosiciones();

       if(arrayList.size()>0)// SI LA BASE DE DATOS TIENE REGISTROS LOS MOSTRAMOS
       {
           adaptadorLista.setEstudiantes(arrayList);
           txtNoHayRegistros.setVisibility(View.GONE);
       }else
       {
           txtNoHayRegistros.setVisibility(View.VISIBLE);
       }



    }
    private  void leerArchivoJson()// LEE EL ARCHIVO JSON DE EJEMPLO Y LO CONVIERTE EN UNA CADENA STRING
       {



        try{
            pCargando.setVisibility(View.VISIBLE);
            AssetManager am = getAssets();
            InputStream is = am.open("datosejemplo.txt");


            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line="";
            String json="";
            while(reader.ready()) {
                 line = reader.readLine();
                 json+=line;
            }
            pCargando.setVisibility(View.GONE);
            generarLista(json);




        }catch (Exception e)
        {
            e.printStackTrace();
        }


    }

    private void generarLista(String json)// CONVIERTE LA CADENA STRING EN UNA LISTA DE POSICIONES Y LO GUARDA EN LA DB
    {
        try {
             JSONObject object = new JSONObject(json); //Creamos un objeto JSON a partir de la cadena obtenida en el fichero

            JSONArray json_array = object.optJSONArray("results"); //cogemos cada uno de los elementos dentro de la etiqueta "result"

            String nombres = "", direccion = "";
            Double lat, lng;
            JSONObject geometry;//COORDENADAS GEOMETRICAS

            arrayList=new ArrayList<>();//BORRAMOS LA LISTA PARA NO TENER DATOS REPETIDOS

            Posicion posicion  ;
            for (int i = 0; i < json_array.length(); i++) {

                nombres = json_array.getJSONObject(i).getString("name");//OPTENEMOS EL NOMBRE DEL ESTUDIANTE
                direccion = json_array.getJSONObject(i).getString("formatted_address");//OPTENEMOS LA DIRECCION
                geometry = json_array.getJSONObject(i).getJSONObject("geometry");// OBJETO GEOMETRY QUE CONTIENE LAT Y LNG
                lat = geometry.getJSONObject("location").getDouble("lat");
                lng = geometry.getJSONObject("location").getDouble("lng");

                posicion=new Posicion(getIDRandom(), nombres, direccion, lat, lng);
                 dbManager.registrarPosicion(posicion);
                cargarDatosDB();//LUEGO CARGAMOS LOS DATOS DESDE LA BASE DE DATOS


            }
            Toast.makeText(MainActivity.this, "Datos cargados del archivo JSON", Toast.LENGTH_SHORT).show();
            adaptadorLista.setEstudiantes(arrayList);
        }catch (Exception e)
        {
            e.printStackTrace();
        }


    }
    private int  getIDRandom()// PARA GENERAR EL ID ALEATORIAMENTE
    {

        return  (int) (Math.random() * 99999) + 1;
    }





    private void GuardarEstudiante()
    {
        if(this.COORDENADAS_SELECT!=null)
        {
            if(!edtNmobre.getText().toString().isEmpty())
            {
                if(!edtDireccion.getText().toString().isEmpty())
                {
                    String nombre=edtNmobre.getText().toString();
                    String direccion=edtDireccion.getText().toString();
                    Double lat=this.COORDENADAS_SELECT.latitude;
                    Double lng=this.COORDENADAS_SELECT.longitude;


                    Posicion posicion= new Posicion(getIDRandom(), nombre,direccion,lat,lng);
                    long result=dbManager.registrarPosicion(posicion);
                    if(result>0)
                    {
                        Toast.makeText(MainActivity.this,"Estudiante registrado",Toast.LENGTH_LONG).show();

                        this.cargarDatosDB();

                    }



                }else
                {
                    Toast.makeText(MainActivity.this,"Ingrese la dirección",Toast.LENGTH_LONG).show();
                }

            }else
            {
                Toast.makeText(MainActivity.this,"Ingrese el nombre del estudiante",Toast.LENGTH_LONG).show();
            }

        }else
        {
            Toast.makeText(MainActivity.this,"Selecione las coordenadas",Toast.LENGTH_LONG).show();
        }
    }

    /// LABORATORIO 03 DSL
    private void extraerMostrarInformacion(){// EXTRAE LA INFORMACION DEL FICHERO  (LAB 03)

        arrayList = new ArrayList<>();

        boolean creado = false;
        for (int i = 0; i < archivos.length; i++){
            if ("info_estudiantes.txt".equals(archivos[i])){
                creado = true;
            }
        }

        if (creado){
            try {

                InputStreamReader file = new InputStreamReader(openFileInput("info_estudiantes.txt"));
                BufferedReader br = new BufferedReader(file);

                String lineas = br.readLine();

                int a = 1;

                while (lineas != null){

                    if (a < 2){
                        //informacion = new Posicion();
                        informacion.setNombres(lineas);
                    }else{
                        informacion.setDireccion(lineas);
                        arrayList.add(informacion);
                        a=0;
                    }
                    a++;

                    lineas = br.readLine();

                }

                br.close();
                file.close();

                adaptadorLista.setEstudiantes(arrayList);

            }catch (IOException ioex){

            }
        }
    }


    // LABORATORIO 03 DSL

    private void guardarInformacion(){
        try {

            OutputStreamWriter file = new OutputStreamWriter(openFileOutput("info_estudiantes.txt", Context.MODE_PRIVATE));

            //informacion = new Posicion();

            informacion.setNombres(edtNmobre.getText().toString());
            informacion.setDireccion(edtDireccion.getText().toString());

            arrayList.add(informacion);

            for (int i = 0; i < arrayList.size(); i++){

                file.write(arrayList.get(i).getNombres()+"\n");
                file.write(arrayList.get(i).getDireccion()+"\n");
            }

            file.flush();
            file.close();

            edtNmobre.setText("");
            edtDireccion.setText("");

            Toast.makeText(this, "Información guardada exitosamente.", Toast.LENGTH_SHORT).show();

        }catch (IOException ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void VerEstudianteMapa(Posicion posicion) {

        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
        intent.putExtra("posicion", posicion);

        startActivity(intent);

    }

    @Override
    public void EliminarEstudiante(final Posicion posicion) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:

                        long result=dbManager.EliminarEstudiante(posicion.getId());

                        if(result>0)
                        {
                            Toast.makeText(MainActivity.this, "Estudiante eliminado", Toast.LENGTH_LONG).show();
                           cargarDatosDB();

                        }
                         break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("¿Quiere eliminar al estudiante "+posicion.getNombres()+"?").setPositiveButton("Sí", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();


    }

}
