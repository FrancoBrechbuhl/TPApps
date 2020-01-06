package com.B3B.farmbros;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
    Esta clase representa la actividad que permite crear una nueva consulta
 */

public class NuevaConsulta extends AppCompatActivity {

    private Button btnTomarFoto;
    private Button btnConsultar;
    static ImageView fotoConsulta;
    static final int REQUEST_IMAGE_SAVE = 1;
    static String pathFoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta);
        btnTomarFoto = findViewById(R.id.btnAgregarFoto);
        btnConsultar = findViewById(R.id.btnConsultar);
        fotoConsulta = findViewById(R.id.imageConsulta);
        fotoConsulta.setVisibility(View.INVISIBLE);

        //código para mostrar el logo en la action bar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_flower);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        btnTomarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Copiado y pegado de SendMeal para tomar una foto
                Intent takeFoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(takeFoto.resolveActivity(getPackageManager()) != null){
                    File photoFile = null;
                    try{
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        Log.d("FALLO","TOMAR FOTO");
                    }
                    if(photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(getApplicationContext(), "com.B3B.farmbros.android.fileprovider", photoFile);
                        takeFoto.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takeFoto, REQUEST_IMAGE_SAVE);
                    }
                }
            }
        });

        btnConsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Se pulso consultar", Toast.LENGTH_SHORT).show();
                //TODO: implementar consulta en el API REST
            }
        });

        fotoConsulta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //aca se permite tocar la foto y ampliarla, como se realiza usualmente en otras apps como Twitter
                File file = new File(pathFoto);
                final Intent intent = new Intent(Intent.ACTION_VIEW).setDataAndType(FileProvider.getUriForFile(getApplicationContext(), "com.B3B.farmbros.android.fileprovider", file), "image/*").addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(REQUEST_IMAGE_SAVE == requestCode && resultCode == RESULT_OK){
            File file = new File(pathFoto);
            try{
                Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),Uri.fromFile(file));
                fotoConsulta.setImageBitmap(imageBitmap);
                fotoConsulta.setVisibility(View.VISIBLE);
            }
            catch (FileNotFoundException ex){
                ex.printStackTrace();
            }
            catch (IOException ex){
                ex.printStackTrace();
            }
        }
    }

    private File createImageFile () throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", dir);
        pathFoto = image.getAbsolutePath();
        return image;
    }
}
