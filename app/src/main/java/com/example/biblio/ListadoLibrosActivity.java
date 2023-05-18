package com.example.biblio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.biblio.clases.Libro;
import com.example.biblio.clases.Usuario;
import com.example.biblio.peticiones.PeticionLibros;

import java.util.ArrayList;

public class ListadoLibrosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_libros);

        Intent i = getIntent();
        Usuario u = new Usuario(i.getIntExtra("id",0),i.getStringExtra("username"), i.getStringExtra("mail")
                , i.getStringExtra("nombre"),i.getStringExtra("apellido"),i.getStringExtra("contrasenya"));


        ArrayList<Libro> libros = new ArrayList<>();
        PeticionLibros pl = new PeticionLibros(u,libros);
        pl.start();
        try {
            pl.join();
        }catch(Exception e){
            e.printStackTrace();
        }

        RecyclerView rv = findViewById(R.id.listaLibros);
        rv.setHasFixedSize(true);

        RecyclerView.LayoutManager lm = new LinearLayoutManager(this);
        rv.setLayoutManager(lm);

        MiAdaptador adaptador = new MiAdaptador(libros);
        rv.setAdapter(adaptador);
    }
}