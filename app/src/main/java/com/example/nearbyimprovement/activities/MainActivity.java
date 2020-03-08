package com.example.nearbyimprovement.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nearbyimprovement.R;
import com.example.nearbyimprovement.model.AdapterMensagens;
import com.example.nearbyimprovement.model.GlobalApplication;
import com.example.nearbyimprovement.model.Mensagem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Button btnConfirmComport, btnIniciarAnuncDesc, btnEnviar;
    private EditText txtNickName, txtMensagem;
    private Spinner spinComportamento;
    private RecyclerView rvMensagens;
    private ArrayList<Mensagem> mensagens;
    private AdapterMensagens adapt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mensagens = new ArrayList<>();

        btnConfirmComport = (Button) findViewById(R.id.btnConfirmarComportamento);
        btnEnviar = (Button) findViewById(R.id.btnEnviar);
        btnIniciarAnuncDesc = (Button) findViewById(R.id.btnIniciarDescobAnuncio);
        txtMensagem = (EditText) findViewById(R.id.txtMensagem);
        txtNickName = (EditText) findViewById(R.id.txtNickName);
        spinComportamento = (Spinner) findViewById(R.id.spinComportamento);
        rvMensagens = (RecyclerView) findViewById(R.id.rvMensagens);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.comportamentos, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinComportamento.setAdapter(adapter);

        adapt = new AdapterMensagens(mensagens);
        rvMensagens.setAdapter(adapt);
        LinearLayoutManager layoutManager = new LinearLayoutManager(GlobalApplication.getContext().getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        layoutManager.setStackFromEnd(true);
        rvMensagens.setLayoutManager(layoutManager);

        btnIniciarAnuncDesc.setEnabled(false);
        btnEnviar.setEnabled(false);
        txtMensagem.setEnabled(false);

        btnConfirmComport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnIniciarAnuncDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
