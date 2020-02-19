package com.B3B.farmbros;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.B3B.farmbros.retrofit.MensajeRepository;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.navigation.NavigationView;

/*
    Esta clase es la principal, aqui se encuentra la barra deslizable con el menu que tiene las
    opciones disponibles de la aplicación
 */

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Button btnCierreSesion;
    private TextView txtIdentificadorUsuario;
    private String profesion;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_drawer);

        txtIdentificadorUsuario = findViewById(R.id.txtIdentificadorUsuario);
        btnCierreSesion = findViewById(R.id.btnCierreSesion);
        mNavigationView = findViewById(R.id.navigation_view);
        mDrawerLayout = findViewById(R.id.drawer_layout);

        //el drawer toggle se usa para que la barra deslizable se pueda abrir con el boton de la action bar
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_opened, R.string.drawer_closed) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(R.string.app_name);
            }

            public void onDrawerOpened(View view) {
                super.onDrawerOpened(view);
                getSupportActionBar().setTitle(R.string.app_name);
            }
        };

        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        //aca se cambian las opciones del drawer, por defecto se ponen las de productor y se cambian si el usuario es ingeniero
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        profesion = getIntent().getExtras().getString("profesion");
        Log.d("Profesion: ", profesion);
        if(profesion.equals("ingeniero")){
            Menu menu = mNavigationView.getMenu();
            MenuItem menuItem = menu.findItem(R.id.menuItemNuevaConsulta);
            menuItem.setVisible(false);
            menuItem = menu.findItem(R.id.menuItemVerConsultasRealizadas);
            menuItem.setVisible(false);
            menuItem = menu.findItem(R.id.menuItemChats);
            menuItem.setVisible(false);
        }
        else{
            MensajeRepository.getInstance().listarMensajesPorReceptor(account.getEmail());
        }

        mNavigationView.setNavigationItemSelectedListener(this);


        //código para mostrar el logo en la action bar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_flower);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_options);

        String userName = account.getDisplayName();
        txtIdentificadorUsuario.setText("Usted se ha identificado como "+ userName);

        //TODO: no se porque cuando se crea una consulta, si se quiere cerrar sesion se vuelve
        // a la pantalla de nueva consulta y despues a la de home de nuevo para poder salir
        // es porque esta activity se inicia con un startActivityForResult, y cuando se invoca
        // al finish() vuelve a la ultima activity que llamó, en el caso anterior vuelve a
        // nuevaConsulta
        btnCierreSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
                builder.setMessage("Está seguro que desea cerrar la sesión?")
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //si se cierra sesión, se retorna a la actividad que llamó (mainActivity)
                                setResult(Activity.RESULT_OK);
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
        });
    }

    @Override
    public void onBackPressed(){
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START)){
            //si se presiona el boton de volver atras (de Android) y el drawer está abierto, primero hay que cerrarlo
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            //no hacer nada porque para volver atras debe pulsar cerrar sesión
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.drawer_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(mDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem){
        Intent i1;
        switch (menuItem.getItemId()){
            case R.id.menuItemNuevaConsulta:
                i1 = new Intent (this, NuevaConsulta.class);
                startActivity(i1);
                return true;
            case R.id.menuItemForoDeConsultas:
                i1 = new Intent (this, ListaConsultasActivity.class);
                i1.putExtra("profesion", profesion);
                startActivity(i1);
                return true;
            case R.id.menuItemVerConsultasRealizadas:
                i1 = new Intent (this, ConsultasRealizadasActivity.class);
                startActivity(i1);
                return true;
            case R.id.menuItemVerConsultasAtendidas:
                i1 = new Intent(getApplicationContext(), ConsultasAtendidasActivity.class);
                i1.putExtra("profesion", profesion);
                startActivity(i1);
            case R.id.menuItemChats:
                i1 = new Intent (this, ListaContactosActivity.class);
                startActivity(i1);
                return true;
            case R.id.menuItemMiPerfil:
                //controlar porque esta opcion puede estar en los dos menúes
                if(profesion.equals("productor")) {
                    //i1 = new Intent (this,Perfil.class);
                    //startActivity(i1);
                }
                else{

                }
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return true;
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
        //se cierra siempre el drawer para evitar que si se retorna de una actividad se muestre abierto
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }
}