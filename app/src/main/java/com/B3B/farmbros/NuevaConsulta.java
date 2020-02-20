package com.B3B.farmbros;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.B3B.farmbros.domain.Consulta;
import com.B3B.farmbros.domain.EstadoConsulta;
import com.B3B.farmbros.domain.Productor;
import com.B3B.farmbros.retrofit.ConsultaRepository;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Random;

/*
    Esta clase representa la actividad que permite crear una nueva consulta
 */

public class NuevaConsulta extends AppCompatActivity {

    private Button btnTomarFoto;
    private Button btnConsultar;
    private EditText txtConsulta;
    private EditText txtAsunto;
    private String fotoEnBase64;
    static ImageView fotoConsulta;
    static final int REQUEST_IMAGE_SAVE = 1;
    static String pathFoto;
    private final int CODE_ACTIVITY_MAPS = 9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta);
        btnTomarFoto = findViewById(R.id.btnAgregarFoto);
        btnConsultar = findViewById(R.id.btnConsultar);
        txtConsulta = findViewById(R.id.txtFieldConsulta);
        txtAsunto = findViewById(R.id.editAsuntoConsulta);
        fotoConsulta = findViewById(R.id.imageConsulta);
        fotoConsulta.setVisibility(View.INVISIBLE);

        //TODO: agregar un spinner para asuntos predefinidos (que no los ingrege via texto)

        //código para mostrar el logo en la action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
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
                //se va al mapa para marcar la ubicación del lote a consultar
                AlertDialog.Builder builder = new AlertDialog.Builder(NuevaConsulta.this);
                builder.setMessage("Por favor, seleccione la ubicación del terreno sobre el cual quiere consultar")
                        .setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent i1 = new Intent(getApplicationContext(), MapsActivity.class);
                                startActivityForResult(i1, CODE_ACTIVITY_MAPS);
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
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
                //se codifica la foto en base 64 para guardarla en el Api REST
                FileInputStream fis = new FileInputStream(file);
                byte[] bytesFoto = new byte[(int) file.length()];
                fis.read(bytesFoto);
                fotoEnBase64 = Base64.encodeToString(bytesFoto, Base64.DEFAULT);
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
        else if(requestCode == CODE_ACTIVITY_MAPS && resultCode == RESULT_OK) {

            //se guarda la consulta en el Api REST
            Random r = new Random();
            int idConsulta = r.nextInt(10000) + 1;
            Productor productor = new Productor();

            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
            String userName = account.getDisplayName();
            String emailProductor = account.getEmail();
            productor.setNombre(userName);
            productor.setEmail(emailProductor);
            Consulta consulta = new Consulta();
            consulta.setIdConsulta(idConsulta);
            consulta.setTextoConsulta(txtConsulta.getText().toString());
            consulta.setFechaConsulta(LocalDateTime.now());
            consulta.setLatConsulta(data.getExtras().getDouble("latitud"));
            consulta.setLngConsulta(data.getExtras().getDouble("longitud"));
            consulta.setRemitenteConsulta(productor);
            consulta.setEstado(EstadoConsulta.ABIERTA);
            consulta.setFotoConsultaBase64(fotoEnBase64);
            consulta.setAsuntoConsulta(txtAsunto.getText().toString());

            ConsultaRepository.getInstance().crearConsulta(consulta,handlerCrearConsultas);

            String profesion = "productor";
            Intent i1 = new Intent(getApplicationContext(), Home.class);
            i1.putExtra("profesion", profesion);
            startActivity(i1);
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

    Handler handlerCrearConsultas = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(Message msg) {
            Log.d("HANDLER","Vuelve al handler"+msg.arg1);
            switch (msg.arg1){
                case ConsultaRepository._POST:
                 //TODO notificacion
                    /*
                    codigo de notificacion exitosa
                     */
                    Toast.makeText(getApplicationContext(), "La consulta se ha registrado con éxito", Toast.LENGTH_SHORT).show();

                    break;
                case ConsultaRepository._ERROR:
                    Log.d("HANDLER","Llego con error");
                    Toast.makeText(getApplicationContext(),"@string/error_BD",Toast.LENGTH_SHORT).show();
            }
        }
    };
}
