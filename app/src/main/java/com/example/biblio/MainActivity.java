package com.example.biblio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.biblio.clases.Usuario;
import com.example.biblio.peticiones.PeticionInicioSesion;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SQLiteDatabase myDB = openOrCreateDatabase(getResources().getString(R.string.db), MODE_PRIVATE, null);
        myDB.execSQL(
                "CREATE TABLE IF NOT EXISTS usuario " +
                        "(id Integer PRIMARY KEY, " +
                        "username VARCHAR(50), " +
                        "nombre VARCHAR(50), " +
                        "apellido varchar(50), " +
                        "email varchar(100), " +
                        "contrasenya varchar(30));"
        );
        myDB.execSQL(
                "CREATE TABLE IF NOT EXISTS autor " +
                        "(id Integer PRIMARY KEY, " +
                        "nombre VARCHAR(50) " +
                        ");"
        );
        myDB.execSQL(
                "CREATE TABLE IF NOT EXISTS libro " +
                        "(id Integer PRIMARY KEY," +
                        "isbn VARCHAR(20), " +
                        "titulo VARCHAR(50), " +
                        "sinopsis TEXT, " +
                        "hojas int, " +
                        "id_portada int, " +
                        "url varchar(100), " +
                        "en_posesion INTEGER DEFAULT 0, " +
                        "deseado INTEGER DEFAULT 0, " +
                        "leido INTEGER DEFAULT 0, " +
                        "favorito INTEGER DEFAULT 0," +
                        "id_autor Integer, " +
                        "constraint fk_libro_autor foreign key (id_autor) references autor(id));"
        );
        myDB.execSQL(
                "CREATE TABLE IF NOT EXISTS tematica " +
                        "(id Integer PRIMARY KEY," +
                        "nombre VARCHAR(20) " +
                        ");"
        );
        myDB.execSQL(
                "CREATE TABLE IF NOT EXISTS libro_tematica " +
                        "(id_libro Integer ," +
                        "id_tematica Integer," +
                        "constraint fk_libro_tematica_libro foreign key (id_libro) references libro(id)," +
                        "constraint fk_libro_tematica_tematica foreign key (id_tematica) references tematica(id) " +
                        ");"
        );
        Usuario usuario = new Usuario();
        SQLiteDatabase myDB1 = openOrCreateDatabase(getResources().getString(R.string.db), MODE_PRIVATE, null);
        Cursor cursor = myDB1.rawQuery("select * from usuario",null);
        while(cursor.moveToNext()){
            usuario.setId(cursor.getInt(0));
            usuario.setUsername(cursor.getString(1));
            usuario.setNombre(cursor.getString(2));
            usuario.setMail(cursor.getString(3));
            usuario.setApellido(cursor.getString(4));
            usuario.setContrasenya(cursor.getString(5));
        }
        if(usuario.getUsername()!= null){
            Intent i = new Intent(this, ListadoLibrosActivity.class);
            i.putExtra("id", usuario.getId());
            i.putExtra("username", usuario.getUsername());
            i.putExtra("nombre", usuario.getNombre());
            i.putExtra("mail", usuario.getMail());
            i.putExtra("apellido", usuario.getApellido());
            i.putExtra("contrasenya", usuario.getContrasenya());
            i.putExtra("iniciado",true);
            startActivity(i);
            finish();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void inciarSesion(View v){
        TextView nombre = findViewById(R.id.editTextUsuario);
        TextView contra = findViewById(R.id.editTextContrasenya);

        Usuario u = new Usuario(nombre.getText().toString(), contra.getText().toString());
        PeticionInicioSesion p = new PeticionInicioSesion(u);
        p.start();
        try {
            p.join();
        }catch(Exception e){
            e.printStackTrace();
        }
        if(u.getId()== 0 ){
            Toast.makeText(this, "Usuario o contraseña incorrecta", Toast.LENGTH_SHORT).show();
        }else{
            guardarUsuario(u);
            Intent i = new Intent(this, ListadoLibrosActivity.class);
            i.putExtra("id", u.getId());
            i.putExtra("username", u.getUsername());
            i.putExtra("nombre", u.getNombre());
            i.putExtra("mail", u.getMail());
            i.putExtra("apellido", u.getApellido());
            i.putExtra("contrasenya", u.getContrasenya());
            i.putExtra("iniciado",false);
            startActivity(i);
            finish();
        }
    }

    public void nuevoUsuario(View v){
        Intent i = new Intent(this, NuevoUsuarioActivity.class);
        startActivity(i);
    }

    public void guardarUsuario(Usuario u){
        ContentValues cv = new ContentValues();
        cv.put("id", u.getId());
        cv.put("username",u.getUsername());
        cv.put("email", u.getMail());
        cv.put("nombre", u.getNombre());
        cv.put("apellido", u.getApellido());
        cv.put("contrasenya",u.getContrasenya());
        SQLiteDatabase myDB = openOrCreateDatabase(getResources().getString(R.string.db), MODE_PRIVATE, null);
        myDB.insert("usuario",null, cv);
    }
}