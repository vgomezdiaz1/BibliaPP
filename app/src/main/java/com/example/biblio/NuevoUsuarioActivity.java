package com.example.biblio;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.biblio.clases.Mensaje;
import com.example.biblio.clases.Usuario;
import com.example.biblio.peticiones.PeticionNuevoUsuario;

public class NuevoUsuarioActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_usuario);
    }

    public void NuevoUsuario(View v){
        TextView n = findViewById(R.id.editTextNuevoNombre);
        TextView a = findViewById(R.id.editTextNuevoApellido);
        TextView e = findViewById(R.id.editTextNuevoEmail);
        TextView c1 = findViewById(R.id.editTextNuevoPassword);
        TextView c2 = findViewById(R.id.editTextNuevoPassword2);
        String nombre = n.getText().toString();
        String apellido = a.getText().toString();
        String email = e.getText().toString();
        String contra1 = c1.getText().toString();
        String contra2 = c2.getText().toString();
        Usuario usuario = null;
        if(nombre.equals("") || apellido.equals("") || email.equals("") || contra1.equals("") || contra2.equals("")){
            Toast.makeText(this, "Todos los campos deben estar completados", Toast.LENGTH_SHORT).show();
        }else if(!contra1.equals(contra2)){
            Toast.makeText(this, "Las contraseñas deben de coincidir", Toast.LENGTH_SHORT).show();
        }else{
            Mensaje men = null;
            try{
                men  = new Mensaje(this);
            }catch(Exception ex){
                ex.printStackTrace();
            }
            usuario = new Usuario(email, nombre, apellido, contra1);
            PeticionNuevoUsuario p = new PeticionNuevoUsuario(men,usuario);
            p.start();
            try {
                p.join();
            }catch(Exception ex){
                ex.printStackTrace();
            }if(usuario.getId()==-5) {
                Toast.makeText(this, "No hay conexión con el servidor", Toast.LENGTH_SHORT).show();
            }else if(usuario.getId()!=0){
                Toast.makeText(this, "Usuario creado", Toast.LENGTH_SHORT).show();
                finish();
            }else{
                Toast.makeText(this, "Error al crear usuario", Toast.LENGTH_SHORT).show();
            }
        }
    }
}