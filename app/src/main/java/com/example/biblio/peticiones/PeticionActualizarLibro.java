package com.example.biblio.peticiones;

import static android.content.Context.MODE_PRIVATE;
import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.example.biblio.R;
import com.example.biblio.clases.UsuarioLibro;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

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
            conn.setConnectTimeout(5000);
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
                if(completado.equals("true")){
                    ContentValues cv = new ContentValues();
                    cv.put("en_posesion", ul.isEn_posesion());
                    cv.put("deseado", ul.isDeseado());
                    cv.put("leido", ul.isLeido());
                    cv.put("favorito", ul.isFavorito());
                    SQLiteDatabase myDB = openOrCreateDatabase(R.string.db+"", null);
                    int c = myDB.update("libro", cv, "id = ? ", new String[]{this.ul.getLibro().getId() + ""});
                }else{
                    System.out.println("No se ha podido actualizar");
                }
            }else{

            }
        } catch (java.net.SocketTimeoutException e) {
            System.out.println("Tiempo demasiado largo");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
