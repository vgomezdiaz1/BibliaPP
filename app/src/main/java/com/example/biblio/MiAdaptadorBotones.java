package com.example.biblio;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.biblio.clases.Libro;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MiAdaptadorBotones extends RecyclerView.Adapter<MiAdaptadorBotones.MyViewHolder> {

    ArrayList<String> nombres;
    Context context;

    public MiAdaptadorBotones(Context context, ArrayList<String> nombres) {
        this.nombres = nombres;
        this.context = context;
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
                    }else if(nombre.getText().equals("Biblioteca")){
                        i.putExtra("Posesion",1);
                    }else if(nombre.getText().equals("Deseado")){
                        i.putExtra("Deseado",1);
                    }else if(nombre.getText().equals("Mis Favoritos")){
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
        Drawable icon = null;
        if(position==0){
            icon =  context.getResources().getDrawable(R.drawable.libreria);
        }else if(position==1){
            icon =  context.getResources().getDrawable(R.drawable.leido);
        }else if(position==2){
            icon =  context.getResources().getDrawable(R.drawable.deseado);
        }else{
            icon =  context.getResources().getDrawable(R.drawable.estrellafavoritos);
        }
        int width = 200;
        int height = 200;

        icon = DrawableCompat.wrap(icon);
        icon.setBounds(0, 0, width, height);
        holder.nombre.setText(nombres.get(position));
        holder.nombre.setCompoundDrawables(icon, null, null, null);
        holder.nombre.invalidate();
    }

    @Override
    public int getItemCount() {
        return nombres.size();
    }
}
