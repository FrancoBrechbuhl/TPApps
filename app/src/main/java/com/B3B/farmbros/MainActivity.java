package com.B3B.farmbros;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

/*
    Esta clase es la de bienvenida, muestra los botones para registrarse o iniciar sesión
 */

public class MainActivity extends AppCompatActivity {

    private static final int CODE_SIGNIN_GOOGLE = 999;
    private static final int CODE_ACTIVITY_HOME = 7;

    private Button btnInicioSesion;
    private String profesion;
    private GoogleSignInClient googleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnInicioSesion = findViewById(R.id.btnInicioSesion);

        /*
            código para mostrar el logo en la action bar
        */

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_flower);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        /*
        inicio de sesion por defecto de Gmail
        */

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleSignInClient = GoogleSignIn.getClient(getApplicationContext(), gso);

        btnInicioSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signIn = googleSignInClient.getSignInIntent();
                startActivityForResult(signIn, CODE_SIGNIN_GOOGLE);
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if(account != null){
            String userName = account.getDisplayName();
            Intent i1 = new Intent(getApplicationContext(), Home.class);
            i1.putExtra("userName", userName);
            i1.putExtra("profesion", profesion);
            startActivityForResult(i1, CODE_ACTIVITY_HOME);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        /*
        si recibe CODE_SIGNIN_GOOGLE se retorno desde Gmail, si recibe CODE_ACTIVITY_HOME retorna desde la actividad nuevaConsulta
        */
        if(requestCode == CODE_SIGNIN_GOOGLE){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
        else if(requestCode == CODE_ACTIVITY_HOME){
            googleSignInClient.signOut();
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask){
        try{
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            if(account != null){
                //TODO: realizar consulta a retrofit pidiendo si el email esta registrado o no
                /*
                if(!registrado){
                     AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Elija una opción").
                        setItems(R.array.profesiones, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(i == 0){
                                    profesion = "Productor";
                                    Productor productor = new Productor();
                                }
                                else{
                                    Ingeniero ingeniero = new Ingeniero();
                                    profesion = "Ingeniero agrónomo";
                                }
                            }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                }
                 */
                String userName = account.getDisplayName();
                String email = account.getEmail();
                Intent i1 = new Intent(getApplicationContext(), Home.class);
                i1.putExtra("userName", userName);
                i1.putExtra("email", email);
                i1.putExtra("profesion", profesion);
                startActivityForResult(i1, CODE_ACTIVITY_HOME);
            }
        }
        catch (ApiException e){
            Log.d("FALLO", "Código de error: "+e.getStatusCode());
            e.printStackTrace();
        }
    }

}