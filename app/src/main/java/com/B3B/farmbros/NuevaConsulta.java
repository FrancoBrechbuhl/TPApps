package com.B3B.farmbros;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class NuevaConsulta extends AppCompatActivity {

    private Button btnTomarFoto;
    private Button btnConsultar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta);
        btnTomarFoto = findViewById(R.id.btnAgregarFoto);
        btnConsultar = findViewById(R.id.btnConsultar);

        btnTomarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Se pulso añadir foto", Toast.LENGTH_SHORT).show();
                //TODO: Copiar lo de sacar foto del proyecto sendMeal
            }
        });

        btnConsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Se pulso consultar", Toast.LENGTH_SHORT).show();
                //TODO: implementar consulta en el API REST
            }
        });
    }
}
