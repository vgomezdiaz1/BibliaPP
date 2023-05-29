package com.example.biblio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.biblio.clases.Autor;
import com.example.biblio.clases.Libro;
import com.example.biblio.clases.Tematica;

import java.util.ArrayList;

public class BusquedaActivity extends AppCompatActivity {

    ArrayList<Libro> libros = new ArrayList<>();
    ArrayList<Autor> autores = new ArrayList<>();
    ArrayList<Tematica> tematicas = new ArrayList<>();
    EditText tNom;
    Spinner selectorautores;
    Spinner selectortematicas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busqueda);
        autores.add(new Autor(-1,"No seleccionado"));
        tematicas.add(new Tematica(-1,"No seleccionado"));
        rellenarTematicas();
        rellenarAutores();

        this.tNom = findViewById(R.id.editTextBusquedaTitulo);
        this.selectorautores = (Spinner) findViewById(R.id.spinnerBusquedaAutor);
        this.selectortematicas = (Spinner) findViewById(R.id.spinnerBusquedaTematica);

        String[] nombresAutores = new String[autores.size()];
        String[] nombresTematicas = new String[tematicas.size()];
        for (int i = 0; i < autores.size(); i++){
            nombresAutores[i] = autores.get(i).getNombre();
        }
        for (int i = 0; i < tematicas.size(); i++){
            nombresTematicas[i] = tematicas.get(i).getNombre();
        }
        ArrayAdapter<String> adapterAutor = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, nombresAutores);
        ArrayAdapter<String> adapterTematicas = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, nombresTematicas);

        this.selectorautores.setAdapter(adapterAutor);
        this.selectortematicas.setAdapter(adapterTematicas);
    }

    public void rellenarTematicas(){
        SQLiteDatabase myDB1 = openOrCreateDatabase(getResources().getString(R.string.db), MODE_PRIVATE, null);
        Cursor cursor = myDB1.rawQuery("select * from tematica",null);
        while(cursor.moveToNext()){
            Tematica tema = new Tematica();
            tema.setId(cursor.getInt(0));
            tema.setNombre(cursor.getString(1));
            this.tematicas.add(tema);
        }
        cursor.close();
    }

    public void rellenarAutores(){
        SQLiteDatabase myDB1 = openOrCreateDatabase(getResources().getString(R.string.db), MODE_PRIVATE, null);
        Cursor cursor = myDB1.rawQuery("select * from autor",null);
        while(cursor.moveToNext()){
            Autor autor = new Autor();
            autor.setId(cursor.getInt(0));
            autor.setNombre(cursor.getString(1));
            this.autores.add(autor);
        }
        cursor.close();
    }

    public void buscarLibro(View v){
        this.libros.clear();
        int idAutor = -1;
        int idTematica = -1;
        boolean aut = false;
        String consulta = "select l.id, l.isbn, l.titulo, l.sinopsis, l.hojas, l.id_portada, l.url," +
                " l.en_posesion, l.deseado, l.leido, l.favorito, l.id_autor from libro l ";
        if(selectortematicas.getSelectedItemPosition()>0) {
            idTematica = tematicas.get(selectortematicas.getSelectedItemPosition()).getId();
            consulta += "inner join libro_tematica lt on l.id = lt.id_libro where lt.id_tematica = " + idTematica + " ";
            aut = true;
        }

        if(selectorautores.getSelectedItemPosition()>0){
            idAutor = autores.get(selectorautores.getSelectedItemPosition()).getId();
            if(aut){
                consulta += " and l.id_autor = " + idAutor;
            }else{
                consulta += " where l.id_autor = " + idAutor;
            }


        }
        SQLiteDatabase myDB = openOrCreateDatabase(getResources().getString(R.string.db), MODE_PRIVATE, null);
        Cursor cursor = myDB.rawQuery(consulta,null);
        Cursor cursor1 = null;
        while(cursor.moveToNext()){
            int id = cursor.getInt(0);
            String isbn = cursor.getString(1);
            String titulo = cursor.getString(2);
            String sinopsis = cursor.getString(3);
            int hojas = cursor.getInt(4);
            int id_portada = cursor.getInt(5);
            String url = cursor.getString(6);
            boolean en_posesion = trueOrFalse(cursor.getInt(7));
            boolean deseado = trueOrFalse(cursor.getInt(8));
            boolean leido = trueOrFalse(cursor.getInt(9));
            boolean favorito = trueOrFalse(cursor.getInt(10));
            int id_autor = cursor.getInt(11);
            Autor autor = new Autor();
            for (Autor a: autores) {
                if(id_autor == a.getId()){
                    autor.setId(a.getId());
                    autor.setNombre(a.getNombre());
                    break;
                }
            }
            ArrayList<Tematica> temas = new ArrayList<>();
            cursor1= myDB.rawQuery("select t.id, t.nombre from tematica t " +
                    "inner join libro_tematica lt on t.id = lt.id_tematica " +
                    "where lt.id_libro =  " + id,null);
            while(cursor1.moveToNext()){
                temas.add(new Tematica(cursor1.getInt(0),cursor1.getString(1)));
            }
            Libro l = new Libro(id,isbn,titulo,sinopsis,hojas,id_portada,url,en_posesion,deseado,leido,favorito,autor,temas);
            this.libros.add(l);
        }
        cursor.close();
        RecyclerView rv = findViewById(R.id.listaBusquedaLibros);
        rv.setHasFixedSize(true);

        RecyclerView.LayoutManager lm = new LinearLayoutManager(this);
        rv.setLayoutManager(lm);

        MiAdaptador adaptador = new MiAdaptador(this.libros);
        rv.setAdapter(adaptador);
    }

    static boolean trueOrFalse(int n){
        if(n==1){
            return true;
        }else{
            return false;
        }
    }
}