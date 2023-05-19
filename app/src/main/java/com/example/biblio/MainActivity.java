package com.example.biblio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.biblio.clases.Libro;
import com.example.biblio.clases.Usuario;
import com.example.biblio.peticiones.PeticionInicioSesion;
import com.example.biblio.peticiones.PeticionLibros;
import com.example.biblio.peticiones.PeticionNuevoLibro;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SQLiteDatabase myDB = openOrCreateDatabase(getResources().getString(R.string.db), MODE_PRIVATE, null);
        myDB.execSQL(
                "CREATE TABLE IF NOT EXISTS usuario " +
                        "(id Integer PRIMARY KEY, " +
                        "username VARCHAR(50), " +
                        "nombre VARCHAR(50), " +
                        "apellido varchar(50), " +
                        "email varchar(100), " +
                        "contrasenya varchar(30));"
        );
        myDB.execSQL(
                "CREATE TABLE IF NOT EXISTS autor " +
                        "(id Integer PRIMARY KEY, " +
                        "nombre VARCHAR(50) " +
                        ");"
        );
        myDB.execSQL(
                "CREATE TABLE IF NOT EXISTS libro " +
                        "(id Integer PRIMARY KEY," +
                        "isbn VARCHAR(20), " +
                        "titulo VARCHAR(50), " +
                        "sinopsis TEXT, " +
                        "hojas int, " +
                        "url varchar(100), " +
                        "en_posesion BOOLEAN, " +
                        "deseado BOOLEAN, " +
                        "leido BOOLEAN, " +
                        "favorito BOOLEAN," +
                        "id_autor Integer, " +
                        "constraint fk_libro_autor foreign key (id_autor) references autor(id));"
        );
        myDB.execSQL(
                "CREATE TABLE IF NOT EXISTS tematica " +
                        "(id Integer PRIMARY KEY," +
                        "nombre VARCHAR(20) " +
                        ");"
        );
        myDB.execSQL(
                "CREATE TABLE IF NOT EXISTS libro_tematica " +
                        "(id_libro Integer ," +
                        "id_tematica Integer," +
                        "constraint fk_libro_tematica_libro foreign key (id_libro) references libro(id)," +
                        "constraint fk_libro_tematica_tematica foreign key (id_tematica) references tematica(id) " +
                        ");"
        );
    }

    public void inciarSesion(View v){
        TextView nombre = findViewById(R.id.editTextUsuario);
        TextView contra = findViewById(R.id.editTextContrasenya);

        Usuario u = new Usuario(nombre.getText().toString(), contra.getText().toString());
        PeticionInicioSesion p = new PeticionInicioSesion(u);
        p.start();
        try {
            p.join();
        }catch(Exception e){
            e.printStackTrace();
        }
        if(u.getId()== 0 ){
            Toast.makeText(this, "Usuario o contraseña incorrecta", Toast.LENGTH_SHORT).show();
        }else{
            Intent i = new Intent(this, ListadoLibrosActivity.class);
            i.putExtra("id", u.getId());
            i.putExtra("username", u.getUsername());
            i.putExtra("nombre", u.getNombre());
            i.putExtra("mail", u.getMail());
            i.putExtra("apellido", u.getApellido());
            i.putExtra("contrasenya", u.getContrasenya());
            startActivity(i);
        }
    }

    public void nuevoUsuario(View v){
        Intent i = new Intent(this, NuevoUsuarioActivity.class);
        startActivity(i);
    }
}