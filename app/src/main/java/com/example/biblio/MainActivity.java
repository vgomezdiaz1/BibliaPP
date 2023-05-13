package com.example.biblio;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.biblio.clases.Usuario;
import com.example.biblio.peticiones.PeticionInicioSesion;
import com.example.biblio.peticiones.PeticionNuevoLibro;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

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
    }
}