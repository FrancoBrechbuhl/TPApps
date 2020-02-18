package com.B3B.farmbros;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.util.Base64;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.B3B.farmbros.domain.Consulta;
import com.B3B.farmbros.domain.EstadoConsulta;
import com.B3B.farmbros.retrofit.ConsultaRepository;
import com.B3B.farmbros.retrofit.MensajeRepository;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class DetalleConsultaActivity extends AppCompatActivity {

    private TextView nombreProductor;
    private EditText consulta;
    private ImageView imagenConsulta;
    private Button envioMensaje;
    private Button cierreConsulta;
    private Consulta consultaDetallada;

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
        envioMensaje = (Button) findViewById(R.id.btnEmviarMsgDetCons);
        cierreConsulta = (Button) findViewById(R.id.btnFinalizarConsultaDetCons);

        final String profesion = getIntent().getExtras().getString("profesion");

        //solo se permiten chats de ingeniero a productor o viceversa, pero nunca entre dos productores
        if(profesion.equals("productor")){
            envioMensaje.setVisibility(View.INVISIBLE);
        }
        else{
            String emailProductor = getIntent().getExtras().getString("email productor");
            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
            String emailEmisor = account.getEmail();
            MensajeRepository.getInstance().listarMensajesPorEmisoryReceptor(emailEmisor, emailProductor);
            MensajeRepository.getInstance().listarMensajesPorReceptoryEmisor(emailProductor, emailEmisor);
        }

        nombreProductor.setText(this.getIntent().getExtras().getString("nombre productor"));
        consulta.setText(this.getIntent().getExtras().getString("consulta"));
        //TODO: revisar que aca da null pointer cuando se vuelve de los chats
        if (!this.getIntent().getExtras().getString("foto consulta").equals("")){
            byte[] decoded = Base64.decode(this.getIntent().getExtras().getString("foto consulta"),Base64.DEFAULT);
            Bitmap imagen = BitmapFactory.decodeByteArray(decoded,0,decoded.length);
            imagenConsulta.setImageBitmap(imagen);
        }

        envioMensaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailProductor = getIntent().getExtras().getString("email productor");
                String nombreProductor = getIntent().getExtras().getString("nombre productor");
                GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
                String emailEmisor = account.getEmail();
                MensajeRepository.getInstance().listarMensajesPorEmisoryReceptor(emailEmisor, emailProductor);
                MensajeRepository.getInstance().listarMensajesPorReceptoryEmisor(emailProductor, emailEmisor);
                Intent i1 = new Intent(getApplicationContext(), ChatsActivity.class);
                i1.putExtra("nombre productor", nombreProductor);
                i1.putExtra("email productor", emailProductor);
                i1.putExtra("profesion", profesion);
                startActivity(i1);
            }
        });

        cierreConsulta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //se marca la consulta en estado FINALIZADA
                consultaDetallada = ConsultaRepository.getInstance().getConsulta();
                consultaDetallada.setEstado(EstadoConsulta.FINALIZADA);
                ConsultaRepository.getInstance().actualizarConsulta(consultaDetallada);
                //TODO: Corregir, no se hace el update porque la consulta tiene id y idConsulta
                // yo trate de hacer que actualice con @Query y el idConsulta, pero en el server
                // se ejecuta el PUT solo que no cambia la consulta, si o si hay que hacerlo con
                // el id que es la PK, pero no se como obtenerlo porque eso lo hace el handler
                Toast.makeText(getApplicationContext(), "La consulta se ha finalizado con éxito", Toast.LENGTH_SHORT).show();
                Intent i1 = new Intent(getApplicationContext(), ListaConsultasActivity.class);
                i1.putExtra("profesion", profesion);
                startActivity(i1);
            }
        });
    }
}
