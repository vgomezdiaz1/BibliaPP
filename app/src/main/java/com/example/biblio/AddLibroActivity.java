package com.example.biblio;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.biblio.clases.Autor;
import com.example.biblio.clases.Libro;
import com.example.biblio.clases.Mensaje;
import com.example.biblio.clases.Tematica;
import com.example.biblio.clases.Usuario;
import com.example.biblio.databinding.ActivityAddLibroBinding;
import com.example.biblio.databinding.ActivityMainBinding;
import com.example.biblio.peticiones.PeticionNuevoLibro;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.ArrayList;

public class AddLibroActivity extends AppCompatActivity{

    Usuario u;
    ArrayList<Libro> libro = new ArrayList<>();

    ActivityAddLibroBinding bindign;

    ProgressBar buscando;
    private final ActivityResultLauncher<ScanOptions> codigoBarras = registerForActivityResult(new ScanContract(), result ->{
        if(result.getContents() == null){
            Toast.makeText(this, "Cancelado", Toast.LENGTH_SHORT).show();
        }else{
            bindign.editTextAddLibroISBN.setText(result.getContents());
        }
    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_libro);
        Intent i = getIntent();
        this.u = new Usuario(i.getIntExtra("id",0),i.getStringExtra("username"), i.getStringExtra("mail")
                , i.getStringExtra("nombre"),i.getStringExtra("apellido"),i.getStringExtra("contrasenya"));
        bindign = ActivityAddLibroBinding.inflate(getLayoutInflater());
        setContentView(bindign.getRoot());
        buscando = findViewById(R.id.progressBar);
        buscando.setVisibility(View.INVISIBLE);
        bindign.buttonActiuvarCamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                escanear();
            }
        });
    }

    public void botonBuscarISBN(View v){
        TextView nombre = findViewById(R.id.editTextAddLibroISBN);
        String isbn = nombre.getText().toString();
        if (nombre.length() >= 9 && nombre.length() <= 13 && comprobarISBN(isbn)) {
            buscando.setVisibility(View.VISIBLE);
            Mensaje men = null;
            int idLibroBuscado = buscarLibroPorISBN(isbn);
            if(idLibroBuscado != 0){
                Intent i = new Intent(this, ListadoLibrosActivity.class);
                i.putExtra("idLibro", idLibroBuscado);
                i.putExtra("id", u.getId());
                i.putExtra("username", u.getUsername());
                i.putExtra("nombre", u.getNombre());
                i.putExtra("mail", u.getMail());
                i.putExtra("apellido", u.getApellido());
                i.putExtra("contrasenya", u.getContrasenya());
                i.putExtra("iniciado",true);
                i.putExtra("seleccionarLibro", true);
                i.putExtra("idNuevo", idLibroBuscado + "");
                startActivity(i);
                finish();
            }else{
                try{
                    men  = new Mensaje(this);
                    PeticionNuevoLibro p1 = new PeticionNuevoLibro(men, u, isbn, libro);
                    p1.start();
                    p1.join();
                }catch(Exception e){
                    e.printStackTrace();
                }
                if(libro.size()>0){
                    guardarDatosLibros(libro);
                    guardarDatosAutores(libro);
                    guardarDatosTematicas(libro);
                    Toast.makeText(this.getBaseContext(), "Libro guardado", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(this, ListadoLibrosActivity.class);
                    i.putExtra("idLibro", libro.get(0).getId());
                    i.putExtra("id", u.getId());
                    i.putExtra("username", u.getUsername());
                    i.putExtra("nombre", u.getNombre());
                    i.putExtra("mail", u.getMail());
                    i.putExtra("apellido", u.getApellido());
                    i.putExtra("contrasenya", u.getContrasenya());
                    i.putExtra("iniciado",true);
                    i.putExtra("seleccionarLibro", true);
                    i.putExtra("idNuevo", libro.get(0).getId() + "");
                    startActivity(i);
                    finish();
                }else if(u.getApellido().equals("404")){
                    Toast.makeText(this, "El ISBN no existe en la base de datos", Toast.LENGTH_LONG).show();
                    u.setApellido(u.getApellido().substring(0,u.getApellido().length()-3));
                }else{
                    Toast.makeText(this, "No hay conexion con el servidor", Toast.LENGTH_LONG).show();
                    u.setApellido(u.getApellido().substring(0,u.getApellido().length()-1));
                }
            }
        }else{
            Toast.makeText(this, "El ISBN no es valido", Toast.LENGTH_LONG).show();
        }
        buscando.setVisibility(View.INVISIBLE);
    }

    public boolean comprobarISBN(String isbn){
        try{
            float prueba = Float.parseFloat(isbn);
            return true;
        } catch(Exception e){
            e.printStackTrace();
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

    public void guardarDatosAutores(ArrayList<Libro> libros){
        ArrayList<Autor> autores = new ArrayList<>();
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

    public void escanear(){
        ScanOptions options = new ScanOptions();
        options.setDesiredBarcodeFormats(ScanOptions.ALL_CODE_TYPES);
        options.setPrompt("Escanear codigo");
        options.setCameraId(0);
        options.setOrientationLocked(false);
        options.setBeepEnabled(true);
        options.setCaptureActivity(CaptureActivityPortraint.class);
        options.setBarcodeImageEnabled(false);
        codigoBarras.launch(options);
    }

    public int buscarLibroPorISBN(String isbn){
        int id_libro = 0;
        SQLiteDatabase myDB = openOrCreateDatabase(getResources().getString(R.string.db), MODE_PRIVATE, null);
        Cursor cursor = myDB.rawQuery("select id from libro where isbn = " + isbn, null);
        int id_autor = 0;
        while (cursor.moveToNext()) {
            id_libro = cursor.getInt(0);
        }
        return id_libro;
    }
}