package com.example.biblio;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;

public class DownloadImageTask extends AsyncTask <String, Void, Bitmap> {

    ImageView bmimage;

    public DownloadImageTask(ImageView bmimage) {
        this.bmimage = bmimage;
    }

    @Override
    protected Bitmap doInBackground(String... urlImagen) {
        String url = urlImagen[0];
        Bitmap bm = null;
        try {
            InputStream is = new java.net.URL(url).openStream();
            bm = BitmapFactory.decodeStream(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bm;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        bmimage.setImageBitmap(bitmap);
    }
}
