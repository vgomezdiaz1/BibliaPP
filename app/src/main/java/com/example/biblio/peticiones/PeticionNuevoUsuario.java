package com.example.biblio.peticiones;

import android.util.JsonReader;
import android.util.Log;

import com.example.biblio.R;
import com.example.biblio.clases.Mensaje;
import com.example.biblio.clases.Usuario;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class PeticionNuevoUsuario extends Thread{

    Mensaje men;
    Usuario usuario;

    public PeticionNuevoUsuario( Mensaje men, Usuario usuario){
        this.men = men;
        this.usuario = usuario;
    }

    @Override
    public void run() {
        super.run();
        URL url = null;
        try {
            url = new URL("http://192.168.1.148:8080/BibliotecaAPI/resources/app/nuevoUsuario");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "text/plain");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestMethod("POST");
            try(OutputStream os = conn.getOutputStream()) {
                byte[] input = men.codificarMensaje(usuario.toString()).getBytes("utf-8");
                os.write(input, 0, input.length);
            }catch(Exception e){
                e.printStackTrace();
            }
            if(conn.getResponseCode()==200){
                String devolucion = "";
                InputStream is = conn.getInputStream();
                InputStreamReader isr = new InputStreamReader(is, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(isr);
                try {
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        devolucion += line;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        bufferedReader.close();
                        isr.close();
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    devolucion = men.decodificarMensaje(devolucion);
                }catch (Exception e){
                    e.printStackTrace();
                }
                devolucion = devolucion.substring(7);
                System.out.println(devolucion);
                byte[] bytes = devolucion.getBytes(StandardCharsets.UTF_8);
                ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
                InputStreamReader isrd = new InputStreamReader(bais, StandardCharsets.UTF_8);
                JsonReader jr = new JsonReader(isrd);
                jr.setLenient(true);
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
                    }else{
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
