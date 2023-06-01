package com.example.biblio.peticiones;

import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

import com.example.biblio.clases.Mensaje;
import com.example.biblio.clases.UsuarioLibro;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PeticionActualizarLibro extends Thread {

    Mensaje men;
    UsuarioLibro ul;
    public PeticionActualizarLibro(Mensaje men, UsuarioLibro mensaje){
        this.men = men;
        this.ul = mensaje;
    }

    @Override
    public void run(){
        super.run();
        URL url = null;
        String o = "";
        try {
            url = new URL("http://192.168.1.148:8080/BibliotecaAPI/resources/app/actualizarLibro");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "text/plain");
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(5000);
            try(OutputStream os = conn.getOutputStream()) {
                byte[] input = men.codificarMensaje(ul.toString()).getBytes("utf-8");
                os.write(input, 0, input.length);
            }catch(Exception e){
            }
            if(conn.getResponseCode()==200){
                InputStream in = new BufferedInputStream(conn.getInputStream());
                if (in != null) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
                    String line = "";
                    while ((line = bufferedReader.readLine()) != null)
                        o += line;
                }
                ul.getUsuario().setId(-5);
                in.close();
            }else{

            }
        } catch (java.net.SocketTimeoutException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
