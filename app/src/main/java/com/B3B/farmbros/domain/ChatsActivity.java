package com.B3B.farmbros.domain;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.B3B.farmbros.MensajeViewAdapter;
import com.B3B.farmbros.R;

import java.util.ArrayList;
import java.util.List;

public class ChatsActivity extends AppCompatActivity {
    private RecyclerView mRecyclerMensaje;
    private MensajeViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_chats);

        List<Mensaje> mensajes = new ArrayList<Mensaje>();

        mRecyclerMensaje = (RecyclerView) findViewById(R.id.recyclerChats);
        mAdapter = new MensajeViewAdapter(this, mensajes);
        mRecyclerMensaje.setLayoutManager(new LinearLayoutManager(this));
    }
}
