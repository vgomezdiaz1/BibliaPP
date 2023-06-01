package com.example.biblio;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.biblio.clases.Libro;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MiAdaptadorBotones extends RecyclerView.Adapter<MiAdaptadorBotones.MyViewHolder> {

    ArrayList<String> nombres;

    public MiAdaptadorBotones(ArrayList<String> nombres) {
        this.nombres = nombres;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        Button nombre;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.buttonSeleccionable);
            nombre.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(itemView.getContext(),ListadoLibrosActivity.class);
                    if(nombre.getText().equals("Leido")){
                        i.putExtra("Leido",1);
                    }else if(nombre.getText().equals("Posesion")){
                        i.putExtra("Posesion",1);
                    }else if(nombre.getText().equals("Deseado")){
                        i.putExtra("Deseado",1);
                    }else if(nombre.getText().equals("Favorito")){
                        i.putExtra("Favorito",1);
                    }
                    itemView.getContext().startActivity(i);
                }
            });
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflador = LayoutInflater.from(parent.getContext());

        View v = inflador.inflate(R.layout.elementobotones,parent, false);

        MyViewHolder mvh = new MyViewHolder(v);

        return mvh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.nombre.setText(nombres.get(position));
    }

    @Override
    public int getItemCount() {
        return nombres.size();
    }
}
