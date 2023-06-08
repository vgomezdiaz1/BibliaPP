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

    Mensaje mensaje;
    UsuarioLibro usuarioLibro;
    public PeticionActualizarLibro(Mensaje men, UsuarioLibro usuarioLibro){
        this.mensaje = men;
        this.usuarioLibro = usuarioLibro;
    }

    @Override
    public void run() {
        super.run();
        URL url = null;
        String o = "";
        boolean conexion = false;
        try {
            url = new URL("http://192.168.168.148:8080/BibliotecaAPI/resources/app/ping");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(2000);
            if (conn.getResponseCode() == 200) {
                conexion = true;
            }
            System.out.println(conn.getResponseCode() == 200);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (conexion) {
            try {
                url = new URL("http://192.168.168.148:8080/BibliotecaAPI/resources/app/actualizarLibro");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "text/plain");
                conn.setRequestMethod("POST");
                conn.setConnectTimeout(5000);
                try (OutputStream os = conn.getOutputStream()) {
                    byte[] input = mensaje.codificarMensaje(usuarioLibro.toString()).getBytes("utf-8");
                    os.write(input, 0, input.length);
                } catch (Exception e) {
                }
                if (conn.getResponseCode() == 200) {
                    InputStream in = new BufferedInputStream(conn.getInputStream());
                    if (in != null) {
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
                        String line = "";
                        while ((line = bufferedReader.readLine()) != null)
                            o += line;
                    }
                    usuarioLibro.getUsuario().setApellido("ok");
                    in.close();
                } else {
                    usuarioLibro.getUsuario().setApellido("ko");
                }
            } catch (java.net.SocketTimeoutException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
