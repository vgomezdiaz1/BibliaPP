package com.example.biblio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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