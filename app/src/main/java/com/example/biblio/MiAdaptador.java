package com.example.biblio;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.biblio.clases.Libro;
import com.squareup.picasso.Picasso;

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
        TextView txtTematicas;
        TextView txtId;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imagePortada);
            txtNombre = itemView.findViewById(R.id.textNombreLibro);
            txtAutor = itemView.findViewById(R.id.textAutor);
            txtTematicas = itemView.findViewById(R.id.textSinopsis);
            txtId = itemView.findViewById(R.id.textID);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(itemView.getContext(),InformacionLibroActivity.class);
                    i.putExtra("libro",txtId.getText().toString());
                    itemView.getContext().startActivity(i);
                }
            });
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
        Picasso.get()
                .load(libros.get(position).getUrl())
                .fit()
                .into(holder.img);
        holder.txtNombre.setText(libros.get(position).getTitulo());
        holder.txtAutor.setText(libros.get(position).getAutor().getNombre());
        holder.txtTematicas.setText(libros.get(position).toStringTematicas());
        holder.txtId.setText(libros.get(position).getId() + "");
    }

    @Override
    public int getItemCount() {
        return libros.size();
    }
}
