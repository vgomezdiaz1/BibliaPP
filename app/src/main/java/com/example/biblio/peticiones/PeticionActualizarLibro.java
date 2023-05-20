package com.example.biblio.peticiones;

import android.content.res.Resources;
import android.util.JsonReader;

import com.example.biblio.clases.Autor;
import com.example.biblio.clases.Libro;
import com.example.biblio.clases.Tematica;
import com.example.biblio.clases.Usuario;
import com.example.biblio.clases.UsuarioLibro;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class PeticionActualizarLibro extends Thread{

    UsuarioLibro ul;
    String completado = "";
    public PeticionActualizarLibro(UsuarioLibro ul, String completado){
        this.ul = ul;
        this.completado = completado;
    }

    @Override
    public void run() {
        super.run();
        URL url = null;
        try {
            url = new URL("http://192.168.1.148:8080/BibliotecaAPI/resources/app/actualizarLibro");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "text/plain");
            conn.setRequestMethod("POST");
            try(OutputStream os = conn.getOutputStream()) {
                System.out.println(ul.toString());
                byte[] input = ul.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }catch(Exception e){
                e.printStackTrace();
            }
            if(conn.getResponseCode()==200){
                InputStream in = new BufferedInputStream(conn.getInputStream());
                if (in != null) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
                    String line = "";
                    while ((line = bufferedReader.readLine()) != null)
                        completado += line;
                }
                in.close();
                System.out.println("Completado " + completado);
            }else{

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
