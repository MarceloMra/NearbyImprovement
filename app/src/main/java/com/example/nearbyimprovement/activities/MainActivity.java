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
import com.example.nearbyimprovement.enums.Comportamento;
import com.example.nearbyimprovement.improvement.NearbyAccessObject;
import com.example.nearbyimprovement.improvement.PatternComunicationObject;
import com.example.nearbyimprovement.model.AdapterMensagens;
import com.example.nearbyimprovement.model.GlobalApplication;
import com.example.nearbyimprovement.model.Mensagem;
import com.example.nearbyimprovement.model.MyPublisherObject;
import com.example.nearbyimprovement.model.MyReqReplyObject;
import com.example.nearbyimprovement.model.MySubscriberObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Button btnConfirmComport, btnIniciarAnuncDesc, btnEnviar;
    private EditText txtNickName, txtMensagem;
    private Spinner spinComportamento, spinIdsConnected;
    private RecyclerView rvMensagens;
    private ArrayList<Mensagem> mensagens;
    private AdapterMensagens adapt;
    private NearbyAccessObject nearbyAccessObject;
    private PatternComunicationObject patternComunicationObject;
    private ArrayAdapter<String> arrayAdapter;

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
        spinIdsConnected = (Spinner) findViewById(R.id.spinIdsConnected);
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
                inicializarObjetosDeComunicacao();
            }
        });

        btnIniciarAnuncDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (patternComunicationObject != null && nearbyAccessObject != null){
                    patternComunicationObject.startAdvertising();
                    patternComunicationObject.startDiscovery();
                }
            }
        });

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(patternComunicationObject instanceof MyPublisherObject || patternComunicationObject instanceof MyReqReplyObject){
                    if(patternComunicationObject instanceof MyPublisherObject){
                        MyPublisherObject mpo = (MyPublisherObject) patternComunicationObject;
                        mpo.send(txtMensagem.getText().toString().getBytes(), null);
                    }else if(patternComunicationObject instanceof MyReqReplyObject){
                        MyReqReplyObject mro = (MyReqReplyObject) patternComunicationObject;
                        //ENVIANDO SEMPRE PARA O PRIMEIRO, PORÉM PODE ACEITAR QUALQUER ENDPOINTID QUE ESTEJA CONECTADO
                        mro.send(txtMensagem.getText().toString().getBytes(), patternComunicationObject.getEndpointIDsConnected().get(0));
                    }
                }
            }
        });
    }

    private void inicializarObjetosDeComunicacao(){
        if(!txtNickName.getText().equals("")){
            if (spinComportamento.getSelectedItem() == 0){
                patternComunicationObject = new MyPublisherObject(this);
            }else if (spinComportamento.getSelectedItem() == 1){
                patternComunicationObject = new MySubscriberObject(this);
            }else if (spinComportamento.getSelectedItem() == 2){
                patternComunicationObject = new MyReqReplyObject(Comportamento.REQUESTER, this);
            }else if (spinComportamento.getSelectedItem() == 3){
                patternComunicationObject = new MyReqReplyObject(Comportamento.REPLYER, this);
            }

            nearbyAccessObject = new NearbyAccessObject(patternComunicationObject, txtNickName.getText().toString());
            if(arrayAdapter == null){
                arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, patternComunicationObject.getEndpointIDsConnected());
            }
        }
    }

    public void liberarCamposEnvio(){
        if(patternComunicationObject != null && (patternComunicationObject.getComportamento() == Comportamento.PUBLISHER || patternComunicationObject.getComportamento() == Comportamento.REQUESTER || patternComunicationObject.getComportamento() == Comportamento.REPLYER )){
            txtMensagem.setEnabled(true);
            btnEnviar.setEnabled(true);
            arrayAdapter.notifyDataSetChanged();
        }
    }

    public void addNovaMensagem(String s){
        mensagens.add(new Mensagem(s));
        adapt.notifyDataSetChanged();
    }
}
