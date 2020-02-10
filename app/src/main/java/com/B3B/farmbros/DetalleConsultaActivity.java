package com.B3B.farmbros;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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
        //TODO imagen y botones
    }
}
