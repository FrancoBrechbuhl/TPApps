package com.B3B.farmbros;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

public class Home extends AppCompatActivity {
    private Button btnCierreSesion;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_drawer);
        btnCierreSesion = findViewById(R.id.btnCierreSesion);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_opened, R.string.drawer_closed){
            public void onDrawerClosed(View view){
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(R.string.app_name);
            }
            public void onDrawerOpened(View view){
                super.onDrawerOpened(view);

                getSupportActionBar().setTitle(R.string.app_name);
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_flower);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_options);

        btnCierreSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Se pulso cerrar sesión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.drawer_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Intent i1;
        switch(item.getItemId()) {
            case R.id.menuItemNuevaConsulta:
                i1 = new Intent (getApplicationContext(),NuevaConsulta.class);
                startActivity(i1);
                return true;
            case R.id.menuItemVerConsultasRealizadas:
                return true;
            case R.id.menuItemChats:
                return true;
            case R.id.menuItemMiPerfil:
                return true;
            case android.R.id.home:
                return true;
        }
        return true;
    }

}
