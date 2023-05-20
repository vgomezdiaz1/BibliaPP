package com.example.biblio;

import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import com.example.biblio.clases.Autor;
import com.example.biblio.clases.Libro;
import com.example.biblio.clases.Tematica;
import com.example.biblio.clases.Usuario;
import com.example.biblio.peticiones.PeticionLibros;

import java.util.ArrayList;

public class ListadoLibrosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_libros);
        ArrayList<Libro> libros = new ArrayList<>();
        ArrayList<Autor> autores = new ArrayList<>();
        ArrayList<Tematica> tematicas = new ArrayList<>();
        Intent i = getIntent();
        Usuario u = new Usuario(i.getIntExtra("id",0),i.getStringExtra("username"), i.getStringExtra("mail")
                , i.getStringExtra("nombre"),i.getStringExtra("apellido"),i.getStringExtra("contrasenya"));
        if(i.getBooleanExtra("iniciado",false)){
            SQLiteDatabase myDB = openOrCreateDatabase(getResources().getString(R.string.db), MODE_PRIVATE, null);
            SQLiteDatabase myDB1 = openOrCreateDatabase(getResources().getString(R.string.db), MODE_PRIVATE, null);
            Cursor cursor1 = null;
            Cursor cursor = myDB.rawQuery("select * from autor",null);
            while(cursor.moveToNext()){
                autores.add(new Autor(cursor.getInt(0),cursor.getString(1)));
            }
            cursor = myDB.rawQuery("select l.id, l.isbn, l.titulo, l.sinopsis, l.hojas, l.url," +
                    " l.en_posesion, l.deseado, l.leido, l.favorito, l.id_autor from libro l" ,null);
            while(cursor.moveToNext()){
                int id = cursor.getInt(0);
                String isbn = cursor.getString(1);
                String titulo = cursor.getString(2);
                String sinopsis = cursor.getString(3);
                int hojas = cursor.getInt(4);
                String url = cursor.getString(5);
                boolean en_posesion = trueOrFalse(cursor.getInt(6));
                boolean deseado = trueOrFalse(cursor.getInt(7));
                boolean leido = trueOrFalse(cursor.getInt(8));
                boolean favorito = trueOrFalse(cursor.getInt(9));
                int id_autor = cursor.getInt(10);
                Autor autor = new Autor();
                for (Autor a: autores) {
                    if(id_autor == a.getId()){
                        autor.setId(a.getId());
                        autor.setNombre(a.getNombre());
                        break;
                    }
                }
                ArrayList<Tematica> temas = new ArrayList<>();
                cursor1= myDB1.rawQuery("select t.id, t.nombre from tematica t " +
                        "inner join libro_tematica lt on t.id = lt.id_tematica " +
                        "where lt.id_libro =  " + id,null);
                while(cursor1.moveToNext()){
                    temas.add(new Tematica(cursor1.getInt(0),cursor1.getString(1)));
                }
                Libro l = new Libro(id,isbn,titulo,sinopsis,hojas,url,en_posesion,deseado,leido,favorito,autor,temas);
                libros.add(l);
            }
            cursor.close();
            myDB.close();
            myDB1.close();
        }else{
            PeticionLibros pl = new PeticionLibros(u,libros);
            pl.start();
            try {
                pl.join();
            }catch(Exception e){
                e.printStackTrace();
            }
            guardarDatosLibros(libros);
            guardarDatosAutores(libros, autores);
            guardarDatosTematicas(libros);
        }
        RecyclerView rv = findViewById(R.id.listaLibros);
        rv.setHasFixedSize(true);

        RecyclerView.LayoutManager lm = new LinearLayoutManager(this);
        rv.setLayoutManager(lm);

        MiAdaptador adaptador = new MiAdaptador(libros);
        rv.setAdapter(adaptador);
    }

    static boolean trueOrFalse(int n){
        if(n==1){
            return true;
        }else{
            return false;
        }
    }

    public void guardarDatosLibros(ArrayList<Libro> libros){
        SQLiteDatabase myDB = openOrCreateDatabase(getResources().getString(R.string.db), MODE_PRIVATE, null);
        for (Libro li: libros) {
            ContentValues cv = new ContentValues();
            cv.put("id", li.getId());
            cv.put("isbn",li.getIsbn());
            cv.put("titulo", li.getTitulo());
            cv.put("sinopsis", li.getSinopsis());
            cv.put("hojas", li.getHojas());
            cv.put("url",li.getUrl());
            cv.put("en_posesion", li.isEn_posesion());
            cv.put("deseado", li.isDeseado());
            cv.put("leido",li.isLeido());
            cv.put("favorito", li.isFavorito());
            cv.put("id_autor", li.getAutor().getId());
            myDB.insert("libro",null, cv);
        }
        myDB.close();
    }

    public void guardarDatosAutores(ArrayList<Libro> libros, ArrayList<Autor> autores){
        for (Libro li: libros) {
            if(!autores.contains(li.getAutor())){
                autores.add(li.getAutor());
            }
        }
        SQLiteDatabase myDB = openOrCreateDatabase(getResources().getString(R.string.db), MODE_PRIVATE, null);
        for (Autor a: autores) {
            ContentValues cv = new ContentValues();
            cv.put("id", a.getId());
            cv.put("nombre",a.getNombre());
            myDB.insert("autor",null, cv);
        }
        myDB.close();
    }

    public void guardarDatosTematicas(ArrayList<Libro> libros){
        ArrayList<Tematica> tematicas = new ArrayList<>();
        for (Libro li: libros) {
            for (Tematica te: li.getTematica()) {
                if(!tematicas.contains(te)){
                    tematicas.add(te);
                }
            }
        }
        SQLiteDatabase myDB = openOrCreateDatabase(getResources().getString(R.string.db), MODE_PRIVATE, null);
        for (Tematica a: tematicas) {
            ContentValues cv = new ContentValues();
            cv.put("id", a.getId());
            cv.put("nombre",a.getNombre());
            myDB.insert("tematica",null, cv);
        }
        for (Libro li: libros) {
            for (Tematica te: li.getTematica()) {
                ContentValues cv = new ContentValues();
                cv.put("id_libro", li.getId());
                cv.put("id_tematica",te.getId());
                myDB.insert("libro_tematica",null, cv);
            }
        }
        myDB.close();
    }

    public void cerrarSesion(View v){
        SQLiteDatabase myDB = openOrCreateDatabase(getResources().getString(R.string.db), MODE_PRIVATE, null);
        myDB.delete("libro_tematica","",null);
        myDB.delete("libro","",null);
        myDB.delete("tematica","",null);
        myDB.delete("usuario","",null);
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}