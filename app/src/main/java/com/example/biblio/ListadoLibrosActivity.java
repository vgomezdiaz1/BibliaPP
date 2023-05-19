package com.example.biblio;

import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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
        long correcto = 0;
        for (Libro li: libros) {
            ContentValues cv = new ContentValues();
            cv.put("id", li.getId());
            cv.put("isbn",li.getIsbn());
            cv.put("titulo", li.getTitulo());
            cv.put("sinopsis", li.getSinopsis());
            cv.put("hojas", li.getHojas());
            cv.put("url",li.getUrl());
            cv.put("en_posesion", li.isEn_posesion());
            cv.put("deseado", li.isDeseado());
            cv.put("leido",li.isLeido());
            cv.put("favorito", li.isFavorito());
            cv.put("id_autor", li.getAutor().getId());
            SQLiteDatabase myDB = openOrCreateDatabase(getResources().getString(R.string.db), MODE_PRIVATE, null);
            correcto = myDB.insert("libro",null, cv);
        }

        RecyclerView rv = findViewById(R.id.listaLibros);
        rv.setHasFixedSize(true);

        RecyclerView.LayoutManager lm = new LinearLayoutManager(this);
        rv.setLayoutManager(lm);

        MiAdaptador adaptador = new MiAdaptador(libros);
        rv.setAdapter(adaptador);
    }
}