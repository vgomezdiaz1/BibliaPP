package com.example.biblio;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.biblio.clases.Libro;

import java.util.ArrayList;

public class MiAdaptador extends RecyclerView.Adapter<MiAdaptador.MyViewHolder> {

    ArrayList<Libro> libros;

    public MiAdaptador(ArrayList<Libro> lista) {
        this.libros = lista;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView txtNombre;
        TextView txtAutor;
        TextView txtSinopsis;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imagePortada);
            txtNombre = itemView.findViewById(R.id.textNombreLibro);
            txtAutor = itemView.findViewById(R.id.textAutor);
            txtSinopsis = itemView.findViewById(R.id.textSinopsis);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflador = LayoutInflater.from(parent.getContext());

        View v = inflador.inflate(R.layout.elemento,parent, false);

        MyViewHolder mvh = new MyViewHolder(v);

        return mvh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DownloadImageTask u1 = new DownloadImageTask(holder.img);
        u1.execute(libros.get(position).getUrl());
        holder.txtNombre.setText(libros.get(position).getTitulo());
        holder.txtAutor.setText(libros.get(position).getAutor().getNombre());
        holder.txtSinopsis.setText(libros.get(position).getSinopsis());
    }

    @Override
    public int getItemCount() {
        return libros.size();
    }
}
