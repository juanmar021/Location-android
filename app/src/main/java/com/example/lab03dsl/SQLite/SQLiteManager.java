package com.example.lab03dsl.SQLite;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import static com.example.lab03dsl.SQLite.SQLiteHelper.DIRECCION;
import static com.example.lab03dsl.SQLite.SQLiteHelper.ID;
import static com.example.lab03dsl.SQLite.SQLiteHelper.LAT;
import static com.example.lab03dsl.SQLite.SQLiteHelper.LNG;
import static com.example.lab03dsl.SQLite.SQLiteHelper.NOMBRES;
import static com.example.lab03dsl.SQLite.SQLiteHelper.TABLA_POSICIONES;

import com.example.lab03dsl.Posicion;

import java.util.ArrayList;

public class SQLiteManager {

    private SQLiteHelper helper;
    private SQLiteDatabase db;

    public SQLiteManager(Context context) {
        helper = new SQLiteHelper(context);
    }

    public ArrayList<Posicion> getPosiciones() {//OPTENGO LAS POSICIONES REGISTRADAS EN LA DB
        db = helper.getReadableDatabase();

        ArrayList<Posicion> lista=new ArrayList<>();
        Cursor c = db.rawQuery( //CONSULTA SELECT
                "SELECT * FROM "+TABLA_POSICIONES,
                null
        );

        Posicion posicion = null;




        if (c.moveToFirst()) {

            do {
                posicion=new Posicion();
                posicion.setId( c.getInt( c.getColumnIndex(ID) ) );
                posicion.setNombres( c.getString( c.getColumnIndex(NOMBRES) ) );
                posicion.setDireccion( c.getString( c.getColumnIndex(DIRECCION) ) );
                posicion.setLat( c.getDouble( c.getColumnIndex(LAT) ) );
                posicion.setLng( c.getDouble( c.getColumnIndex(LNG) ) );
                lista.add(posicion);

            } while(c.moveToNext());
        }

        c.close();

        return lista;
    }




    public long registrarPosicion(Posicion posicion) {
        db = helper.getWritableDatabase();

        long resInsert = db.insert(TABLA_POSICIONES, null, posicion.toContentValues());

        return resInsert;
    }

    public long borrarTabla(String tabla) {// BORRA TODOS LOS DATOS DE LA TABLA
        db = helper.getWritableDatabase();

        return db.delete(tabla, null, null);
    }

    public  long EliminarEstudiante(int id)
    {
        db = helper.getWritableDatabase();

        return db.delete(TABLA_POSICIONES,""+ID+ "= "+id, null);
    }

}
