package com.example.biblio.clases;

import android.content.Context;
import android.content.res.AssetManager;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

public class Mensaje {


    private static final String ALGORITMO = "AES";
    private static final String ARCHIVO_CLAVE = "clave_secreta.txt";

    private SecretKey claveSecreta;

    public Mensaje(Context context) throws Exception {
        cargarClaveDesdeAssets(context);
    }

    private void cargarClaveDesdeAssets(Context context) throws Exception {
        AssetManager assetManager = context.getAssets();
        InputStream inputStream = assetManager.open(ARCHIVO_CLAVE);

        byte[] claveBytes = new byte[inputStream.available()];
        inputStream.read(claveBytes);

        claveSecreta = new SecretKeySpec(claveBytes, ALGORITMO);
    }

    public String codificarMensaje(String mensaje) throws Exception {
        Cipher cifrador = Cipher.getInstance(ALGORITMO);
        cifrador.init(Cipher.ENCRYPT_MODE, claveSecreta);
        byte[] mensajeCodificado = cifrador.doFinal(mensaje.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(mensajeCodificado);
    }

    public String decodificarMensaje(String mensajeCodificado) throws Exception {
        Cipher cifrador = Cipher.getInstance(ALGORITMO);
        cifrador.init(Cipher.DECRYPT_MODE, claveSecreta);
        byte[] mensajeDecodificado = cifrador.doFinal(Base64.getDecoder().decode(mensajeCodificado));
        return new String(mensajeDecodificado, StandardCharsets.UTF_8);
    }
}

