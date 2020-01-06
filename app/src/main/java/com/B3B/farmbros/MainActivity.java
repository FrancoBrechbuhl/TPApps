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

    private Button btnInicioSesion;
    private Button btnRegistro;
    private String profesion;
    private GoogleSignInClient googleSignInClient;
    private static final int CODE_ACTIVITY_HOME = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnInicioSesion = findViewById(R.id.btnInicioSesion);
        btnRegistro = findViewById(R.id.btnRegistro);

        //código para mostrar el logo en la action bar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_flower);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        //inicio de sesion por defecto de Gmail
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleSignInClient = GoogleSignIn.getClient(getApplicationContext(), gso);

        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String urlGmail = "https://accounts.google.com/signup/v2/webcreateaccount?service=mail&hl=es&continue=http%3A%2F%2Fmail.google.com%2Fmail%2F%3Fpc%3Dtopnav-about-es&flowName=GlifWebSignIn&flowEntry=SignUp#__utma=29003808.1113598014.1578326162.1578326162.1578326162.1&__utmb=29003808.0.10.1578326162&__utmc=29003808&__utmx=-&__utmz=29003808.1578326162.1.1.utmcsr=google%7Cutmccn=(organic)%7Cutmcmd=organic%7Cutmctr=(not%20provided)&__utmv=-&__utmk=114705604";
                Uri uri = Uri.parse(urlGmail);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        btnInicioSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Elija una opción").
                        setItems(R.array.profesiones, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(i == 0){
                                    profesion = "Productor";
                                }
                                else{
                                    profesion = "Ingeniero agrónomo";
                                }
                                Intent signIn = googleSignInClient.getSignInIntent();
                                startActivityForResult(signIn, 999);
                            }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
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
        //si recibe 999 se retorno desde Gmail, si recibe CODE_ACTIVITY_HOME retorna desde la actividad nuevaConsulta
        if(requestCode == 999){
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
                String userName = account.getDisplayName();
                Intent i1 = new Intent(getApplicationContext(), Home.class);
                i1.putExtra("userName", userName);
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