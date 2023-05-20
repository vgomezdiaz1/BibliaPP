package com.example.biblio.peticiones;

import android.content.res.Resources;
import android.util.JsonReader;

import com.example.biblio.clases.Autor;
import com.example.biblio.clases.Libro;
import com.example.biblio.clases.Tematica;
import com.example.biblio.clases.Usuario;
import com.example.biblio.clases.UsuarioLibro;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class PeticionActualizarLibro extends Thread{

    UsuarioLibro ul;
    public PeticionActualizarLibro(UsuarioLibro ul){
        this.ul = ul;
    }

    @Override
    public void run() {
        super.run();
        URL url = null;
        try {
            url = new URL("http://192.168.1.148:8080/BibliotecaAPI/resources/app/actualizarLibro");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestMethod("POST");
            try(OutputStream os = conn.getOutputStream()) {
                byte[] input = ul.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }catch(Exception e){
                e.printStackTrace();
            }
            boolean completado = false;
            if(conn.getResponseCode()==200){
                InputStream is = conn.getInputStream();
                InputStreamReader isr = new InputStreamReader(is, "UTF-8");
                JsonReader jr = new JsonReader(isr);
                jr.beginArray();
                while(jr.hasNext()){
                    Libro l = new Libro();
                    jr.beginArray();
                    completado = jr.nextBoolean();
                    jr.endArray();
                }
                jr.endArray();
                System.out.println("Completado " + completado);
            }else{

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
