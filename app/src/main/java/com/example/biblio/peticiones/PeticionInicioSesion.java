package com.example.biblio.peticiones;

import android.util.JsonReader;
import android.util.Log;

import com.example.biblio.R;
import com.example.biblio.clases.Libro;
import com.example.biblio.clases.Usuario;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

public class PeticionInicioSesion extends Thread{

    Usuario usuario;

    public PeticionInicioSesion(Usuario usuario){
        this.usuario = usuario;
    }

    @Override
    public void run() {
        super.run();
        URL url = null;
        try {
            url = new URL("http://192.168.1.148:8080/BibliotecaAPI/resources/app/inicioSesion");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestMethod("POST");
            try(OutputStream os = conn.getOutputStream()) {
                byte[] input = usuario.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }catch(Exception e){
                e.printStackTrace();
            }
            if(conn.getResponseCode()==200){
                InputStream is = conn.getInputStream();
                InputStreamReader isr = new InputStreamReader(is, "UTF-8");
                JsonReader jr = new JsonReader(isr);
                jr.beginObject();
                while(jr.hasNext()){
                    String clave = jr.nextName();
                    if(clave.equals("nombre")){
                        usuario.setNombre(jr.nextString());
                    }else if(clave.equals("username")){
                        usuario.setUsername(jr.nextString());
                    }else if(clave.equals("mail")){
                        usuario.setMail(jr.nextString());
                    }else if(clave.equals("id")){
                        usuario.setId(Integer.parseInt(jr.nextString()));
                    }else if(clave.equals("apellido")){
                        usuario.setApellido(jr.nextString());
                    }else{
                        System.out.println(jr.toString());
                        jr.skipValue();
                    }
                }
            }else{

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
