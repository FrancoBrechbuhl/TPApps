package com.B3B.farmbros;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.util.Base64;

import androidx.appcompat.app.AppCompatActivity;

public class DetalleConsultaActivity extends AppCompatActivity {

    private TextView nombreProductor;
    private EditText consulta;
    private ImageView imagenConsulta;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_consulta);

        //código para mostrar el logo en la action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_flower);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        nombreProductor = (TextView) findViewById(R.id.textNombreDetCons);
        consulta = (EditText) findViewById(R.id.editConsultaDetCons);
        imagenConsulta = (ImageView) findViewById(R.id.imgDetCons);

        nombreProductor.setText(this.getIntent().getExtras().getString("nombre productor"));
        consulta.setText(this.getIntent().getExtras().getString("consulta"));
        byte[] decoded = Base64.decode(this.getIntent().getExtras().getString("foto consulta"),Base64.DEFAULT);
        Bitmap imagen = BitmapFactory.decodeByteArray(decoded,0,decoded.length);
        imagenConsulta.setImageBitmap(imagen);
        imagenConsulta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent verImagen = new Intent(Intent.ACTION_VIEW).
            }
        });
        //TODO imagen y botones
    }
}
