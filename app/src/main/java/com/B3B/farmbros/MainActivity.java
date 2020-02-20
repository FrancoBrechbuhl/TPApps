package com.B3B.farmbros;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.B3B.farmbros.domain.Ingeniero;
import com.B3B.farmbros.domain.Productor;
import com.B3B.farmbros.retrofit.IngenieroRepository;
import com.B3B.farmbros.retrofit.ProductorRepository;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

/*
    Esta clase es la de bienvenida, muestra los botones para registrarse o iniciar sesión
 */

public class MainActivity extends AppCompatActivity {

    /*
        No se recomienda que el cliente de Google sea de clase, pero de otra forma se complicaba
        cerrar la sesion desde otra actividad.
     */
    private static GoogleSignInClient googleSignInClient;
    private SignInButton btnInicioSesion;
    private GoogleSignInAccount account;
    private String profesion;

    private static final int CODE_SIGNIN_GOOGLE = 999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnInicioSesion = (SignInButton) findViewById(R.id.btnInicioSesion);

        /*
            código para mostrar el logo en la action bar
        */

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_flower);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        /*
        inicio de sesion por defecto de Gmail
        */

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestServerAuthCode("704799621360-ad08qm9s1srvn820bu7vq410fe2faklo.apps.googleusercontent.com")
                .requestEmail()
                .build();
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
        account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if(account != null){
            iniciarSesion();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        /*
        si recibe CODE_SIGNIN_GOOGLE se retorno desde Gmail, si recibe CODE_ACTIVITY_HOME retorna desde la actividad Home
        */

        switch (requestCode){
            case CODE_SIGNIN_GOOGLE:
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                handleSignInResult(task);
                break;
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask){
        try{
            /*final GoogleSignInAccount */account = completedTask.getResult(ApiException.class);
            if(account != null){
                iniciarSesion();
                }
        }
        catch (ApiException e){
            Log.d("FALLO", "Código de error: "+e.getStatusCode());
            e.printStackTrace();
        }
    }

    private void iniciarSesion(){
        /*
        primer llamada, el resto se hacen en el handler
         */
        IngenieroRepository.getInstance().buscarIngeniero(account.getEmail(),handlerInicioSesion);

    }

    Handler handlerInicioSesion = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(Message msg) {
            Log.d("HANDLER","Vuelve al handler"+msg.arg1);
            switch (msg.arg1){
                case IngenieroRepository._GET:
                    Ingeniero ingeniero = IngenieroRepository.getInstance().getIngeniero();
                    if (ingeniero == null)
                        ProductorRepository.getInstance().buscarProductor(account.getEmail(),handlerInicioSesion);
                    else goToHome("ingeniero");
                    break;
                case ProductorRepository._GET:
                    Productor productor = ProductorRepository.getInstance().getProductor();
                    if (productor == null) crearCuenta();
                    else goToHome("productor");
                    break;

                case ProductorRepository._ERROR:
                    case IngenieroRepository._ERROR:
                    Log.d("HANDLER","Llego con error");
            }
        }
    };

    public static void cerrarSesion(){
        googleSignInClient.signOut();
    }

    private void goToHome(String profesion){
        Intent home = new Intent(getApplicationContext(), Home.class);
        home.putExtra("userName", account.getDisplayName());
        home.putExtra("email", account.getEmail());
        home.putExtra("profesion", profesion);
        startActivity(home);
    }

    private void crearCuenta(){
        /*
        Intent crearCuenta = new Intent(getApplicationContext(),CrearCuentaActivity.class);
        crearCuenta.putExtra("nombre",account.getDisplayName());
        crearCuenta.putExtra("email",account.getEmail());
        startActivity(crearCuenta);

         */
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Elija su profesión para continuar")
                .setSingleChoiceItems(R.array.profesiones, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(i == 0){
                            profesion = "productor";
                        }
                        else {
                            profesion = "ingeniero";
                        }
                    }
                })
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String nombre = account.getDisplayName();
                        String email = account.getEmail();
                        if(profesion.equals("productor")){
                            Productor productor = new Productor();
                            productor.setNombre(nombre);
                            productor.setEmail(email);
                            ProductorRepository.getInstance().crearProductor(productor);
                            goToHome(profesion);
                        }
                        else{
                            Ingeniero ingeniero = new Ingeniero();
                            ingeniero.setNombre(nombre);
                            ingeniero.setEmail(email);
                            IngenieroRepository.getInstance().crearIngeniero(ingeniero);
                            goToHome(profesion);
                        }
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onBackPressed(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Esta seguro que desea abandonar FarmBros?")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}