package com.example.biblio.peticiones;

import android.util.JsonReader;

import com.example.biblio.clases.Autor;
import com.example.biblio.clases.Libro;
import com.example.biblio.clases.Tematica;
import com.example.biblio.clases.Usuario;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

public class PeticionLibros extends Thread{

    Usuario usuario;
    ArrayList<Libro> libros = new ArrayList<>();
    public PeticionLibros(Usuario usuario, ArrayList<Libro> libros){
        this.usuario = usuario;
        this.libros = libros;
    }

    @Override
    public void run() {
        super.run();
        URL url = null;
        try {
            url = new URL("http://192.168.1.143:8080/BibliotecaAPI/resources/app/librosByUsuario");
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
                jr.beginArray();
                int n = 0;
                while(jr.hasNext()){
                    Libro l = new Libro();
                    jr.beginArray();
                    l.setId(Integer.parseInt(jr.nextString()));
                    l.setHojas(Integer.parseInt(jr.nextString()));
                    l.setIsbn(jr.nextString());
                    l.setSinopsis(jr.nextString());
                    l.setTitulo(jr.nextString());
                    l.setUrl(jr.nextString());
                    int idAutor = jr.nextInt();
                    String nombreAutor = jr.nextString();
                    l.setEn_posesion(jr.nextBoolean());
                    l.setDeseado(jr.nextBoolean());
                    l.setLeido(jr.nextBoolean());
                    l.setFavorito(jr.nextBoolean());
                    int idTematica = jr.nextInt();
                    String nombreTematica = jr.nextString();
                    l.setAutor(new Autor(idAutor,nombreAutor));
                    l.setTematica(new Tematica(idTematica,nombreTematica));
                    this.libros.add(l);
                    jr.endArray();
                }
                jr.endArray();
            }else{

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
