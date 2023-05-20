package com.example.biblio.peticiones;

import android.util.JsonReader;
import android.util.Log;

import com.example.biblio.R;
import com.example.biblio.clases.Libro;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

public class PeticionNuevoLibro  extends Thread{
    @Override
    public void run() {
        super.run();

        URL url = null;
        try {
            url = new URL("http://192.168.1.148:8080/BibliotecaAPI/resources/app/nuevoLibro");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            Log.v("prueba", conn.getRequestMethod());
            Log.v("Response message",conn.getResponseMessage());
            Log.v("Response code", "" + conn.getResponseCode());
            if(conn.getResponseCode()==200){
                InputStream is = conn.getInputStream();
                InputStreamReader isr = new InputStreamReader(is, "UTF-8");
                JsonReader jr = new JsonReader(isr);
                jr.beginObject();
                Libro l = new Libro();
                while(jr.hasNext()){
                    String clave = jr.nextName();
                    if(clave.equals("hojas")){
                        l.setHojas(Integer.parseInt(jr.nextString()));
                    }else if(clave.equals("id")){
                        l.setId(Integer.parseInt(jr.nextString()));
                    }else if(clave.equals("isbn")){
                        l.setIsbn(jr.nextString());
                    }else if(clave.equals("sinopsis")){
                        l.setSinopsis(jr.nextString());
                    }else if(clave.equals("titulo")) {
                        l.setTitulo(jr.nextString());
                    }else{
                        jr.skipValue();
                    }
                }
                Log.v("Libro",l.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
