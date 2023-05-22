package com.example.biblio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.biblio.clases.Libro;
import com.example.biblio.peticiones.PeticionNuevoLibro;

public class AddLibroActivity extends AppCompatActivity {

    int id_usuario;
    Libro libro = new Libro();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_libro);
        Intent i = getIntent();
        id_usuario = i.getIntExtra("id_usuario",0);
    }

    public void botonBuscarISBN(View v){
        TextView nombre = findViewById(R.id.editTextAddLibroISBN);
        String isbn = nombre.getText().toString();
        PeticionNuevoLibro p1 = new PeticionNuevoLibro(id_usuario, isbn, libro);
        p1.start();
        try{
            p1.join();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}