package com.example.biblio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.biblio.clases.Autor;
import com.example.biblio.clases.Libro;
import com.example.biblio.clases.Mensaje;
import com.example.biblio.clases.Tematica;
import com.example.biblio.clases.Usuario;
import com.example.biblio.peticiones.PeticionLibros;

import java.util.ArrayList;

public class ListadoBotonesActivity extends AppCompatActivity {

    ArrayList<Libro> libros = new ArrayList<>();
    ArrayList<Libro> librosEnPosesion = new ArrayList<>();
    ArrayList<Autor> autores = new ArrayList<>();
    ArrayList<Tematica> tematicas = new ArrayList<>();
    Usuario u;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_botones);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("BibliApp");
        toolbar.setTitleTextColor(0xffffffff);
        setSupportActionBar(toolbar);
        Intent i = getIntent();
        this.u = new Usuario(i.getIntExtra("id",0),i.getStringExtra("username"), i.getStringExtra("mail")
                , i.getStringExtra("nombre"),i.getStringExtra("apellido"),i.getStringExtra("contrasenya"));
        if(i.getBooleanExtra("iniciado",false)){
            cargarLibros(i);
            librosEnPosesion();
        }else{
            Mensaje men = null;
            try {
                men = new Mensaje(this);
            } catch (Exception e) {
            }
            PeticionLibros pl = new PeticionLibros(men, u,libros);
            pl.start();
            try {
                pl.join();
            }catch(Exception e){
                e.printStackTrace();
            }
            if(libros.size() == 0){
                Toast.makeText(this, "No hay conexión con el servidor", Toast.LENGTH_SHORT).show();
            }else{
                guardarDatosLibros(libros);
                guardarDatosAutores(libros, autores);
                guardarDatosTematicas(libros);
            }
        }
        ArrayList<String> nombres = new ArrayList<>();
        nombres.add("Lo he Leido");
        nombres.add("Lo tengo");
        nombres.add("Lo quiero");
        nombres.add("Mis Favoritos");
        RecyclerView rv = findViewById(R.id.listaBusquedaButton);
        rv.setHasFixedSize(true);

        RecyclerView.LayoutManager lm = new LinearLayoutManager(this);
        rv.setLayoutManager(lm);

        MiAdaptadorBotones adaptador = new MiAdaptadorBotones(nombres);
        rv.setAdapter(adaptador);
    }

    static boolean trueOrFalse(int n){
        if(n==1){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.leerISBNToolbar) {
            addLibro();
            return true;
        } else if (id == R.id.buscarToolbar) {
            buscarLibros();
            return true;
        } else if (id == R.id.cerrarSesionToolbar) {
            cerrarSesion();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
            cv.put("id_portada", li.getId_portada());
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
        ArrayList<Integer> lista = new ArrayList<>();
        for (Libro li: libros) {
            if(!lista.contains(li.getAutor().getId())){
                lista.add(li.getAutor().getId());
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

    public void cerrarSesion(){
        SQLiteDatabase myDB = openOrCreateDatabase(getResources().getString(R.string.db), MODE_PRIVATE, null);
        myDB.delete("libro_tematica","",null);
        myDB.delete("libro","",null);
        myDB.delete("tematica","",null);
        myDB.delete("usuario","",null);
        myDB.delete("autor","",null);
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    public void addLibro(){
        Intent i = new Intent(this, AddLibroActivity.class);
        i.putExtra("id", u.getId());
        i.putExtra("username", u.getUsername());
        i.putExtra("nombre", u.getNombre());
        i.putExtra("mail", u.getMail());
        i.putExtra("apellido", u.getApellido());
        i.putExtra("contrasenya", u.getContrasenya());
        i.putExtra("iniciado",false);
        startActivity(i);
    }

    public void buscarLibros(){
        Intent i = new Intent(this, BusquedaActivity.class);
        startActivity(i);
    }

    public void librosEnPosesion(){
        for (Libro l: this.libros) {
            if(l.isEn_posesion()){
                this.librosEnPosesion.add(l);
            }
        }
    }

    public void cargarLibros(Intent i){
        SQLiteDatabase myDB = openOrCreateDatabase(getResources().getString(R.string.db), MODE_PRIVATE, null);
        SQLiteDatabase myDB1 = openOrCreateDatabase(getResources().getString(R.string.db), MODE_PRIVATE, null);
        Cursor cursor1 = null;
        Cursor cursor = myDB.rawQuery("select * from autor",null);
        while(cursor.moveToNext()){
            autores.add(new Autor(cursor.getInt(0),cursor.getString(1)));
        }
        cursor = myDB.rawQuery("select l.id, l.isbn, l.titulo, l.sinopsis, l.hojas, l.id_portada, l.url," +
                " l.en_posesion, l.deseado, l.leido, l.favorito, l.id_autor from libro l" ,null);
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
            Tematica tema = null;
            cursor1= myDB1.rawQuery("select t.id, t.nombre from tematica t " +
                    "inner join libro_tematica lt on t.id = lt.id_tematica " +
                    "where lt.id_libro =  " + id,null);
            while(cursor1.moveToNext()){
                tema = new Tematica(cursor1.getInt(0),cursor1.getString(1));
                temas.add(tema);
            }
            if(!tematicas.contains(tema)){
                tematicas.add(tema);
            }
            Libro l = new Libro(id,isbn,titulo,sinopsis,hojas,id_portada,url,en_posesion,deseado,leido,favorito,autor,temas);
            libros.add(l);
        }
        if(i.getBooleanExtra("seleccionarLibro",true) && i.getBooleanExtra("iniciado",true)){
            System.out.println("info");
            Intent in = new Intent(this,InformacionLibroActivity.class);
            in.putExtra("libro",i.getStringExtra("idNuevo"));
            startActivity(in);
        }
        cursor.close();
        myDB.close();
        myDB1.close();
    }
}