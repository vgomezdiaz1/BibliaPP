package com.example.biblio;

import static java.lang.Thread.sleep;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.biblio.clases.Autor;
import com.example.biblio.clases.Libro;
import com.example.biblio.clases.Tematica;
import com.example.biblio.clases.Usuario;
import com.example.biblio.clases.UsuarioLibro;
import com.example.biblio.peticiones.PeticionActualizarLibro;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class InformacionLibroActivity extends AppCompatActivity implements LifecycleObserver {

    String id = null;
    Usuario usuario = null;
    Libro libro = null;
    Switch en_posesion;
    Switch deseado = null;
    Switch leido = null;
    Switch favorito = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_libro);
        Intent i = getIntent();
        id = i.getStringExtra("libro");
        usuario = informacionUsuario();
        libro = informacionLibro(id);
        TextView textTitulo = findViewById(R.id.textInfoLibroTitulo);
        TextView textAutor = findViewById(R.id.textInfoLibroAutor);
        TextView textHojas = findViewById(R.id.textInfoLibroHojas);
        TextView textTematicas = findViewById(R.id.textInfoLibroTematicas);
        TextView textSinopsis = findViewById(R.id.textInfoLibroSinopsis);
        ImageView foto = findViewById(R.id.imageInformacionLibrosImagen);
        this.en_posesion = findViewById(R.id.switchen_posesion);
        this.deseado = findViewById(R.id.switchdeseado);
        this.leido = findViewById(R.id.switchLeido);
        this.favorito = findViewById(R.id.switchfavorito);
        Picasso.get()
                .load(libro.getUrl())
                .fit()
                .into(foto);
        textTitulo.setText(libro.getTitulo());
        textAutor.setText(libro.getAutor().getNombre());
        textHojas.setText(libro.getHojas() + "");
        textTematicas.setText(libro.toStringTematicas());
        textSinopsis.setText(libro.getSinopsis());
        en_posesion.setChecked(libro.isEn_posesion());
        deseado.setChecked(libro.isDeseado());
        leido.setChecked(libro.isLeido());
        favorito.setChecked(libro.isFavorito());
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void releaseCamera() {
        finish();
    }

    static boolean trueOrFalse(int n){
        if(n==1){
            return true;
        }else{
            return false;
        }
    }

    public Usuario informacionUsuario(){
        Usuario usuario = null;
        SQLiteDatabase myDB = openOrCreateDatabase(getResources().getString(R.string.db), MODE_PRIVATE, null);
        Cursor cursor = myDB.rawQuery("select * from usuario",null);
        while(cursor.moveToNext()){
            usuario = new Usuario(cursor.getInt(0),cursor.getString(1),cursor.getString(2),
                    cursor.getString(3),cursor.getString(4),cursor.getString(5));
        }
        cursor.close();
        myDB.close();
        return usuario;
    }


    public Libro informacionLibro(String id) {
        Libro libro = null;
        Autor autor = null;
        ArrayList<Tematica> temas = new ArrayList<>();
        SQLiteDatabase myDB = openOrCreateDatabase(getResources().getString(R.string.db), MODE_PRIVATE, null);
        Cursor cursor = myDB.rawQuery("select * from libro where id = " + id, null);
        int id_autor = 0;
        while (cursor.moveToNext()) {
            libro = new Libro(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), cursor.getInt(4), cursor.getInt(5), cursor.getString(6));
            libro.setEn_posesion(trueOrFalse(cursor.getInt(7)));
            libro.setDeseado(trueOrFalse(cursor.getInt(8)));
            libro.setLeido(trueOrFalse(cursor.getInt(9)));
            libro.setFavorito(trueOrFalse(cursor.getInt(10)));
            id_autor = cursor.getInt(11);
        }
        cursor = myDB.rawQuery("select * from autor where id = " + id_autor,null);
        while(cursor.moveToNext()){
            autor = new Autor(cursor.getInt(0),cursor.getString(1));
        }
        cursor= myDB.rawQuery("select t.id, t.nombre from tematica t " +
                "inner join libro_tematica lt on t.id = lt.id_tematica " +
                "where lt.id_libro =  " + id,null);
        while(cursor.moveToNext()){
            temas.add(new Tematica(cursor.getInt(0),cursor.getString(1)));
        }
        libro.setAutor(autor);
        libro.setTematica(temas);
        cursor.close();
        myDB.close();
        return libro;
    }

    public void confirmarCambios(View v) {
        UsuarioLibro ul = new UsuarioLibro(this.usuario, this.libro,
                this.en_posesion.isChecked(), this.deseado.isChecked(),
                this.leido.isChecked(), this.favorito.isChecked());
        PeticionActualizarLibro p1 = new PeticionActualizarLibro(ul);
        p1.start();
        try {
            p1.join();
            if(ul.getUsuario().getId() == -5){
                ContentValues cv = new ContentValues();
                cv.put("en_posesion", this.en_posesion.isChecked());
                cv.put("deseado", this.deseado.isChecked());
                cv.put("leido", this.leido.isChecked());
                cv.put("favorito", this.favorito.isChecked());
                SQLiteDatabase myDB = openOrCreateDatabase(getResources().getString(R.string.db), MODE_PRIVATE, null);
                int c = myDB.update("libro", cv, "id = ? ", new String[]{this.libro.getId() + ""});
                Toast.makeText(this, "Información actualizada", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "No se ha podido actualizar la información", Toast.LENGTH_SHORT).show();
        }
    }
}
