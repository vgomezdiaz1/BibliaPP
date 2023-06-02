package com.example.biblio;

import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

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

import com.example.biblio.clases.Autor;
import com.example.biblio.clases.Libro;
import com.example.biblio.clases.Mensaje;
import com.example.biblio.clases.Tematica;
import com.example.biblio.clases.Usuario;
import com.example.biblio.peticiones.PeticionLibros;

import java.util.ArrayList;

public class ListadoLibrosActivity extends AppCompatActivity {

    ArrayList<Libro> libros = new ArrayList<>();
    ArrayList<Autor> autores = new ArrayList<>();
    ArrayList<Tematica> tematicas = new ArrayList<>();
    Usuario u;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_libros);
        Intent i = getIntent();
        if(i.getBooleanExtra("seleccionarLibro", true)){
            Intent in = new Intent(this,InformacionLibroActivity.class);
            int idRespuesta = i.getIntExtra("idLibro",-1);
            System.out.println("id respuesta " + idRespuesta);
            if(idRespuesta > 0){
                in.putExtra("libro",idRespuesta+"");
                startActivity(in);
            }
        }
        cargarUsuario();
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("BibliApp");
        toolbar.setTitleTextColor(0xffffffff);
        setSupportActionBar(toolbar);

        cargarLibros(i);
        RecyclerView rv = findViewById(R.id.listaBusquedaButton);
        rv.setHasFixedSize(true);

        RecyclerView.LayoutManager lm = new LinearLayoutManager(this);
        rv.setLayoutManager(lm);

        MiAdaptador adaptador = new MiAdaptador(this.libros);
        rv.setAdapter(adaptador);
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

    static boolean trueOrFalse(int n){
        if(n==1){
            return true;
        }else{
            return false;
        }
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

    public void cargarLibros(Intent i){
        String ileido =  i.getIntExtra("Leido",0) + "";
        String iposesion =  i.getIntExtra("Posesion",0) + "";
        String ideseado =  i.getIntExtra("Deseado",0) + "";
        String ifavorito =  i.getIntExtra("Favorito",0) + "";
        SQLiteDatabase myDB = openOrCreateDatabase(getResources().getString(R.string.db), MODE_PRIVATE, null);
        SQLiteDatabase myDB1 = openOrCreateDatabase(getResources().getString(R.string.db), MODE_PRIVATE, null);
        Cursor cursor1 = null;
        Cursor cursor = myDB.rawQuery("select * from autor",null);
        while(cursor.moveToNext()){
            this.autores.add(new Autor(cursor.getInt(0),cursor.getString(1)));
        }
        String sql = "select l.id, l.isbn, l.titulo, l.sinopsis, l.hojas, l.id_portada, l.url," +
                " l.en_posesion, l.deseado, l.leido, l.favorito, l.id_autor from libro l";
        if(ileido.equals("1")){
            toolbar.setTitle("Leido por " + u.getUsername());
            sql += " where l.leido = 1 ";
        }else if(iposesion.equals("1")){
            toolbar.setTitle("Posesion de " + u.getUsername() );
            sql += " where l.en_posesion = 1 ";
        }else if(ideseado.equals("1")){
            toolbar.setTitle("Deseado por " + u.getUsername());
            sql += " where l.deseado = 1 ";
        }else if(ifavorito.equals("1")){
            toolbar.setTitle("Fav de " + u.getUsername());
            sql += " where l.favorito = 1 ";
        }
        cursor = myDB.rawQuery(sql , null);
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
                this.tematicas.add(tema);
            }
            Libro l = new Libro(id,isbn,titulo,sinopsis,hojas,id_portada,url,en_posesion,deseado,leido,favorito,autor,temas);
            this.libros.add(l);
        }
        cursor.close();
        myDB.close();
        myDB1.close();
    }

    public void cargarUsuario(){
        SQLiteDatabase myDB = openOrCreateDatabase(getResources().getString(R.string.db), MODE_PRIVATE, null);
        Cursor cursor = myDB.rawQuery("select * from usuario",null);
        while(cursor.moveToNext()){
            int id = cursor.getInt(0);
            String username = cursor.getString(1);
            String nombre = cursor.getString(2);
            String mail = cursor.getString(3);
            String apellido = cursor.getString(4);
            String contrasenya = cursor.getString(5);
            this.u = new Usuario(id,username,nombre,mail,apellido,contrasenya);
        }
        cursor.close();
        myDB.close();
    }
}