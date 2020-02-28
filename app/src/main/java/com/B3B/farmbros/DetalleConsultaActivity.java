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
    private String profesion;
    private RatingBar ratingBarCalificacion;
    private Consulta consultaDetallada;
    private float calificacionIngeniero;

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

        profesion = getIntent().getExtras().getString("profesion");
        final String emailProductor = getIntent().getExtras().getString("email productor");
        final String nombreProductor = getIntent().getExtras().getString("nombre productor");

        //solo se permiten chats de ingeniero a productor o viceversa, pero nunca entre dos productores
        if(profesion.equals("productor")){
            envioMensaje.setVisibility(View.INVISIBLE);
        }

        int idConsulta = getIntent().getExtras().getInt("idConsulta");
        ConsultaRepository.getInstance().buscarConsultaPorIdConsulta(idConsulta, handlerDetalleConsulta);

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
                        //Si el productor cierra la consulta y esta tenia un encargado, se lo califica
                        if(profesion.equals("productor") && (consultaDetallada.getEncargadoConsulta() != null)){
                            String emailIngeniero = consultaDetallada.getEncargadoConsulta().getEmail();
                            IngenieroRepository.getInstance().buscarIngeniero(emailIngeniero, handlerDetalleConsulta);
                        }
                        consultaDetallada.setEstado(EstadoConsulta.FINALIZADA);
                        ConsultaRepository.getInstance().actualizarConsulta(consultaDetallada);
                        Toast.makeText(getApplicationContext(), "La consulta se ha finalizado con éxito", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Lo sentimos, no tiene permiso para finalizar esta consulta", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
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
                    //se setean los campos de la consulta
                    consultaDetallada = ConsultaRepository.getInstance().getConsulta();
                    nameProductor.setText(consultaDetallada.getRemitenteConsulta().getNombre());
                    consulta.setText(consultaDetallada.getTextoConsulta());
                    if(consultaDetallada.getFotoConsultaBase64() != null){
                        byte[] decoded = Base64.decode(consultaDetallada.getFotoConsultaBase64(),Base64.DEFAULT);
                        Bitmap imagen = BitmapFactory.decodeByteArray(decoded,0,decoded.length);
                        imagenConsulta.setImageBitmap(imagen);
                    }
                    if(consultaDetallada.getEstado().equals(EstadoConsulta.FINALIZADA)){
                        //si la consulta ya fue cerrada no se permite cerrarla de nuevo
                        cierreConsulta.setVisibility(View.INVISIBLE);
                    }
                    Log.d("HANDLER","Retorno con exito");
                    break;
                case IngenieroRepository._GET:
                    Ingeniero ingeniero = IngenieroRepository.getInstance().getIngeniero();
                    String nombreIngeniero = ingeniero.getNombre();
                    calificacionIngeniero = ingeniero.getCalificacion();
                    AlertDialog.Builder builder = new AlertDialog.Builder(DetalleConsultaActivity.this);
                    LayoutInflater inflater = getLayoutInflater();
                    View v = inflater.inflate(R.layout.dialog_calificacion_ingeniero, null);
                    ratingBarCalificacion = v.findViewById(R.id.ratingBarCalificacionIngeniero);
                    builder.setTitle("Que le ha parecido la experiencia con "+nombreIngeniero)
                            .setView(v)
                            .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Ingeniero ingeniero = consultaDetallada.getEncargadoConsulta();
                                    if(ingeniero.getCalificacion() == 0) {
                                        calificacionIngeniero = ratingBarCalificacion.getRating();
                                    }
                                    else {
                                        //Se calcula un "promedio" un poco diferente
                                        calificacionIngeniero = (ratingBarCalificacion.getRating() + calificacionIngeniero) / 2;
                                    }
                                    ingeniero.setCalificacion(calificacionIngeniero);
                                    IngenieroRepository.getInstance().actualizarIngeniero(ingeniero);
                                    Intent i1 = new Intent(getApplicationContext(), ListaConsultasActivity.class);
                                    i1.putExtra("profesion", profesion);
                                    startActivity(i1);
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    break;
                case ConsultaRepository._ERROR:
                    Log.d("HANDLER","Llego con error");
                    break;
            }
        }
    };
}
