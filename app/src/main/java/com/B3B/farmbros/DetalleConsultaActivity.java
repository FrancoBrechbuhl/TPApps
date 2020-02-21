package com.B3B.farmbros;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.util.Base64;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.B3B.farmbros.domain.Consulta;
import com.B3B.farmbros.domain.EstadoConsulta;
import com.B3B.farmbros.domain.Ingeniero;
import com.B3B.farmbros.retrofit.ConsultaRepository;
import com.B3B.farmbros.retrofit.IngenieroRepository;

public class DetalleConsultaActivity extends AppCompatActivity {

    private TextView nameProductor;
    private EditText consulta;
    private ImageView imagenConsulta;
    private Button envioMensaje;
    private Button cierreConsulta;
    private RatingBar ratingBarcalificacion;
    private Consulta consultaDetallada;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_consulta);

        //código para mostrar el logo en la action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_flower);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        nameProductor = findViewById(R.id.textNombreDetCons);
        consulta = findViewById(R.id.editConsultaDetCons);
        imagenConsulta = findViewById(R.id.imgDetCons);
        envioMensaje = findViewById(R.id.btnEmviarMsgDetCons);
        cierreConsulta = findViewById(R.id.btnFinalizarConsultaDetCons);
        ratingBarcalificacion = findViewById(R.id.ratingBarCalificacionIngeniero);

        final String profesion = getIntent().getExtras().getString("profesion");
        final String emailProductor = getIntent().getExtras().getString("email productor");
        final String nombreProductor = getIntent().getExtras().getString("nombre productor");

        //solo se permiten chats de ingeniero a productor o viceversa, pero nunca entre dos productores
        if(profesion.equals("productor")){
            envioMensaje.setVisibility(View.INVISIBLE);
        }

        int idConsulta = getIntent().getExtras().getInt("idConsulta");
        ConsultaRepository.getInstance().buscarConsultaPorIdConsulta(idConsulta, handlerDetalleConsulta);

        nameProductor.setText(this.getIntent().getExtras().getString("nombre productor"));
        consulta.setText(this.getIntent().getExtras().getString("consulta"));
        if (!this.getIntent().getExtras().getString("foto consulta").equals("")){
            byte[] decoded = Base64.decode(this.getIntent().getExtras().getString("foto consulta"),Base64.DEFAULT);
            Bitmap imagen = BitmapFactory.decodeByteArray(decoded,0,decoded.length);
            imagenConsulta.setImageBitmap(imagen);
        }

        envioMensaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i1 = new Intent(getApplicationContext(), ChatsActivity.class);
                i1.putExtra("nombre productor", nombreProductor);
                i1.putExtra("email productor", emailProductor);
                i1.putExtra("profesion", "ingeniero");
                startActivity(i1);
            }
        });

        cierreConsulta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                se controla que la consulta sea cerrada por un ingeniero o por el
                remitente de la misma
                 */

                if(consultaDetallada != null) {
                    if (profesion.equals("ingeniero") || consultaDetallada.getRemitenteConsulta().getEmail().equals(emailProductor)) {
                        consultaDetallada.setEstado(EstadoConsulta.FINALIZADA);
                        ConsultaRepository.getInstance().actualizarConsulta(consultaDetallada);
                        Toast.makeText(getApplicationContext(), "La consulta se ha finalizado con éxito", Toast.LENGTH_SHORT).show();
                        if(profesion.equals("productor") && (consultaDetallada.getEncargadoConsulta() != null)){
                            String nombreIngeniero = consultaDetallada.getEncargadoConsulta().getNombre();
                            AlertDialog.Builder builder = new AlertDialog.Builder(DetalleConsultaActivity.this);
                            LayoutInflater inflater = getLayoutInflater();
                            View v = inflater.inflate(R.layout.dialog_calificacion_ingeniero, null);
                            builder.setTitle("Que le ha parecido la experiencia con "+nombreIngeniero)
                                    .setView(v)
                                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            //TODO: actualizar la calificacion del ingeniero
                                            Ingeniero ingeniero = consultaDetallada.getEncargadoConsulta();
                                            //TODO: la rating bar da null pointer y no se porque
                                            // ya arriba le quise declarar un listener y tambien me
                                            // dio null pointer

                                            Integer calificacionIngeniero = (int)(ratingBarcalificacion.getRating()*10);
                                            Log.d("Calificacion ", String.valueOf(calificacionIngeniero));
                                            ingeniero.setCalificacion(calificacionIngeniero);
                                            IngenieroRepository.getInstance().actualizarIngeniero(ingeniero);
                                            Intent i1 = new Intent(getApplicationContext(), ListaConsultasActivity.class);
                                            i1.putExtra("profesion", profesion);
                                            startActivity(i1);
                                        }
                                    });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Lo sentimos, no tiene permiso para finalizar esta consulta", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Log.d("Consulta", "Null");
                    Toast.makeText(getApplicationContext(),"Error al cargar la base de datos",Toast.LENGTH_SHORT).show();
                    Intent i1 = new Intent(getApplicationContext(), ListaConsultasActivity.class);
                    i1.putExtra("profesion", profesion);
                    startActivity(i1);
                }
            }
        });
    }

    Handler handlerDetalleConsulta = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(Message msg) {
            Log.d("HANDLER","Vuelve al handler"+msg.arg1);
            switch (msg.arg1){
                case ConsultaRepository._GET:
                    consultaDetallada = ConsultaRepository.getInstance().getConsulta();
                    if(consultaDetallada.getEstado().equals(EstadoConsulta.FINALIZADA)){
                        //si la consulta ya fue cerrada no se permite cerrarla de nuevo
                        cierreConsulta.setVisibility(View.INVISIBLE);
                    }
                    Log.d("HANDLER","Retorno con exito");
                    break;
                case ConsultaRepository._ERROR:
                    Log.d("HANDLER","Llego con error");
            }
        }
    };
}
