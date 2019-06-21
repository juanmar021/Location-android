package com.example.lab03dsl.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {
    private final static String DB = "lab04.db";//NOMBRE DE LA BASE DE DATOS
    private final static int VERSION_DB = 1;

    public SQLiteHelper(Context context) {
        super(context, DB, null, VERSION_DB);
    }

    public static final String TABLA_POSICIONES = "posiciones";

    public static final String ID = "id";
    public static final String NOMBRES = "nombres";//NOMBRES DEL ESTUDIANTE
    public static final String DIRECCION = "direccion";
    public static final String LAT = "lat";
    public static final String LNG = "log";



    @Override
    public void onCreate(SQLiteDatabase db) {


        //CREO UN STRING PARA GENERAR LA TABLA

        String CREAR_TABLA = "CREATE TABLE  IF NOT EXISTS "+ TABLA_POSICIONES +" ( " +
                ID + " INTEGER, "+
                NOMBRES + " TEXT, " +
                DIRECCION + " TEXT, " +
                LAT + " REAL, " +
                LNG + " REAL " +
                "); ";

        db.execSQL(CREAR_TABLA);

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        onCreate(db);
    }
}
