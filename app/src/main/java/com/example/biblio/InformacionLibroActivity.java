package com.example.biblio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.biblio.clases.Autor;
import com.example.biblio.clases.Libro;
import com.example.biblio.clases.Tematica;
import com.example.biblio.clases.Usuario;
import com.example.biblio.clases.UsuarioLibro;
import com.example.biblio.peticiones.PeticionActualizarLibro;

import java.util.ArrayList;

public class InformacionLibroActivity extends AppCompatActivity {

    String id = null;
    Usuario usuario = null;
    CheckBox en_posesion = null;
    CheckBox deseado = null;
    CheckBox leido = null;
    CheckBox favorito = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_libro);
        Intent i = getIntent();
        id = i.getStringExtra("libro");
        usuario = informacionUsuario();
        Libro libro = informacionLibro(id);
        TextView textTitulo = findViewById(R.id.textInfoLibroTitulo);
        TextView textAutor = findViewById(R.id.textInfoLibroAutor);
        TextView textHojas = findViewById(R.id.textInfoLibroHojas);
        TextView textTematicas = findViewById(R.id.textInfoLibroTematicas);
        TextView textSinopsis = findViewById(R.id.textInfoLibroSinopsis);
        ImageView foto = findViewById(R.id.imageInformacionLibrosImagen);
        CheckBox en_posesion = findViewById(R.id.checkBoxen_posesion);
        CheckBox deseado = findViewById(R.id.checkBoxdeseado);
        CheckBox leido = findViewById(R.id.checkBoxleido);
        CheckBox favorito = findViewById(R.id.checkBoxfavorito);
        DownloadImageTask u1 = new DownloadImageTask(foto);
        u1.execute(libro.getUrl());
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
                    cursor.getString(3), cursor.getInt(4), cursor.getString(5));
            libro.setEn_posesion(trueOrFalse(cursor.getInt(6)));
            libro.setDeseado(trueOrFalse(cursor.getInt(7)));
            libro.setLeido(trueOrFalse(cursor.getInt(8)));
            libro.setFavorito(trueOrFalse(cursor.getInt(9)));
            id_autor = cursor.getInt(10);
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

    public void confirmarCambios(View v){
        UsuarioLibro ul = new UsuarioLibro(Integer.parseInt(this.id), this.usuario.getId(),
                en_posesion.isChecked(),deseado.isChecked(),leido.isChecked(),favorito.isChecked());
        PeticionActualizarLibro p1 = new PeticionActualizarLibro(ul);
        p1.start();
    }
}