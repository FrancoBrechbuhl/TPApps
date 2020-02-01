package com.B3B.farmbros;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.B3B.farmbros.domain.Ingeniero;
import com.B3B.farmbros.domain.Productor;
import com.B3B.farmbros.retrofit.IngenieroRepository;
import com.B3B.farmbros.retrofit.ProductorRepository;
import com.google.android.material.navigation.NavigationView;

public class CrearCuentaActivity extends AppCompatActivity {

    private Button btnProductor;
    private Button btnIngeniero;
    private String profesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_cuenta);

        btnIngeniero = (Button) findViewById(R.id.btnCrearIngeniero);
        btnProductor = (Button) findViewById(R.id.btnCrearProductor);

        final String nombre = this.getIntent().getExtras().getString("nombre");
        final String email = this.getIntent().getExtras().getString("email");

        btnProductor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Productor productor = new Productor();
                productor.setNombre(nombre);
                productor.setEmail(email);
                profesion = "Productor";
                ProductorRepository.getInstance().crearProductor(productor);
                goToHome(nombre,email);
            }
        });
        btnIngeniero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Ingeniero ingeniero = new Ingeniero();
                ingeniero.setNombre(nombre);
                ingeniero.setEmail(email);
                profesion = "Ingeniero agrónomo";
                IngenieroRepository.getInstance().crearIngeniero(ingeniero);
                goToHome(nombre,email);
            }
        });
    }

    private void goToHome(String userName,String email){
        Intent home = new Intent(getApplicationContext(), Home.class);
        home.putExtra("userName", userName);
        home.putExtra("email", email);
        home.putExtra("profesion", profesion);
        startActivity(home);
        this.finish();
    }
}
