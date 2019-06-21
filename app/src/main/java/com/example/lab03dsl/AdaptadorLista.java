package com.example.lab03dsl;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class AdaptadorLista extends RecyclerView.Adapter<AdaptadorLista.ViewHolder> {

    private  static  ArrayList<Posicion> datos;
    private   IComunicar icomunica;
    public AdaptadorLista(Context contexto, ArrayList<Posicion> datos,IComunicar icomunica) {
        this.datos = datos;
        this.icomunica= icomunica;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        return new ViewHolder(inflater.inflate(R.layout.my_item_lista, parent, false));
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(datos.get(position));
    }



     class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textv_nombre, textv_direccion,textv_lat,textv_lng;
         ImageView imgEliminar;


        public ViewHolder(View itemView) {
            super(itemView);

            textv_nombre = itemView.findViewById(R.id.estudiante);
            textv_direccion = itemView.findViewById(R.id.direccion);
            textv_lat=itemView.findViewById(R.id.txtlat);
            textv_lng=itemView.findViewById(R.id.txtLng);
            imgEliminar=itemView.findViewById(R.id.btnEliminar);


        }



        private void bind(final Posicion informacion) {
          textv_nombre.setText(informacion.getId()+" - "+ informacion.getNombres());
        textv_direccion.setText( informacion.getDireccion());
        textv_lat.setText("Lat: "+ informacion.getLat());
        textv_lng.setText("Lng: "+informacion.getLng());



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                     icomunica.VerEstudianteMapa(informacion);
                }
            });

            imgEliminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    icomunica.EliminarEstudiante(informacion);
                }
            });



        }



    }

//
//    public class InfoViewHolder extends RecyclerView.ViewHolder  {
//
//        private final TextView textv_nombre, textv_direccion,textv_lat,textv_lng;
//
//        public InfoViewHolder(@NonNull final View itemView) {
//            super(itemView);
//
//            textv_nombre = itemView.findViewById(R.id.estudiante);
//            textv_direccion = itemView.findViewById(R.id.direccion);
//            textv_lat=itemView.findViewById(R.id.txtlat);
//            textv_lng=itemView.findViewById(R.id.txtLng);
//
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    icomunica.VerEstudianteMapa(v);
//                }
//            });
//
//        }
//
//
//    }

//    @NonNull
//    @Override
//    public InfoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//        View item = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_item_lista, viewGroup, false);
//
//        return new InfoViewHolder(item);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull AdaptadorLista.InfoViewHolder holder, int i) {
//        Posicion informacion = datos.get(i);
//
//        holder.textv_nombre.setText(informacion.getId()+" - "+ informacion.getNombres());
//        holder.textv_direccion.setText( informacion.getDireccion());
//        holder.textv_lat.setText("Lat: "+ informacion.getLat());
//        holder.textv_lng.setText("Lng: "+informacion.getLng());





    //}

    @Override
    public int getItemCount() {
        return datos.size();
    }

    public void setEstudiantes(ArrayList<Posicion> datos){

        this.datos = datos;
        notifyDataSetChanged();

    }
}
