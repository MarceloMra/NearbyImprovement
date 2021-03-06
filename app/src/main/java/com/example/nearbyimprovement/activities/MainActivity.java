package com.example.nearbyimprovement.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.nearbyfenix.enums.Comportamento;
import com.example.nearbyfenix.improvement.GlobalApplication;
import com.example.nearbyfenix.improvement.NearbyAccessObject;
import com.example.nearbyfenix.improvement.PatternComunicationObject;
import com.example.nearbyfenix.interfaces.Concluivel;
import com.example.nearbyimprovement.R;
import com.example.nearbyimprovement.model.AdapterMensagens;
import com.example.nearbyimprovement.model.Mensagem;
import com.example.nearbyimprovement.model.MyPublisherObject;
import com.example.nearbyimprovement.model.MyReqReplyObject;
import com.example.nearbyimprovement.model.MySubscriberObject;
import com.example.nearbyimprovement.model.MySyncObject;
import com.example.nearbyimprovement.model.MyVentilatorObject;
import com.example.nearbyimprovement.model.MyWorkerObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Button btnConfirmComport, btnIniciarAnuncDesc, btnEnviar, btnSubscreverServico, btnOkProcess;
    private EditText txtNickName, txtMensagem;
    private Spinner spinComportamento, spinIdsConnected, spinModo;
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
        btnSubscreverServico = (Button) findViewById(R.id.btnSubscreverServico);
        btnOkProcess = (Button) findViewById(R.id.btnOkProcess);
        txtMensagem = (EditText) findViewById(R.id.txtMensagem);
        txtNickName = (EditText) findViewById(R.id.txtNickName);
        spinComportamento = (Spinner) findViewById(R.id.spinComportamento);
        spinIdsConnected = (Spinner) findViewById(R.id.spinIdsConnected);
        spinModo = (Spinner) findViewById(R.id.spinAnuncDesc);
        rvMensagens = (RecyclerView) findViewById(R.id.rvMensagens);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.comportamentos, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinComportamento.setAdapter(adapter);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.modos, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinModo.setAdapter(adapter2);

        adapt = new AdapterMensagens(mensagens);
        rvMensagens.setAdapter(adapt);
        LinearLayoutManager layoutManager = new LinearLayoutManager(GlobalApplication.getContext().getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        layoutManager.setStackFromEnd(true);
        rvMensagens.setLayoutManager(layoutManager);

        btnIniciarAnuncDesc.setEnabled(false);
        btnEnviar.setEnabled(false);
        btnSubscreverServico.setEnabled(false);
        txtMensagem.setEnabled(false);
        btnOkProcess.setEnabled(false);
        spinModo.setEnabled(false);

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
                    if(spinModo.getSelectedItemId() == 0){
                        patternComunicationObject.startAdvertising();
                    }else{
                        patternComunicationObject.startDiscovery();
                    }
                }
            }
        });

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(patternComunicationObject instanceof MyPublisherObject || patternComunicationObject instanceof MyReqReplyObject || patternComunicationObject instanceof MyVentilatorObject || patternComunicationObject instanceof MyWorkerObject){
                    String endpointIDSelected = (String) spinIdsConnected.getSelectedItem();
                    if(patternComunicationObject instanceof MyPublisherObject){
                        MyPublisherObject mpo = (MyPublisherObject) patternComunicationObject;
                        mpo.send(txtMensagem.getText().toString().getBytes(), null);
                        endpointIDSelected = "Broadcast";
                    }else if(patternComunicationObject instanceof MyReqReplyObject){
                        if(spinIdsConnected.getSelectedItem() != null) {
                            MyReqReplyObject mro = (MyReqReplyObject) patternComunicationObject;
                            mro.send(txtMensagem.getText().toString().getBytes(), endpointIDSelected);
                        }else{
                            mostrarMensagemDeControleEmTela("Nenhum endpointID selecionado!");
                        }
                    }else if(patternComunicationObject instanceof MyVentilatorObject){
                        if(spinIdsConnected.getSelectedItem() != null) {
                            MyVentilatorObject mro = (MyVentilatorObject) patternComunicationObject;
                            ArrayList<byte[]> a = new ArrayList<byte[]>();
                            a.add(txtMensagem.getText().toString().getBytes());
                            mro.send(a);
                        }else{
                            mostrarMensagemDeControleEmTela("Nenhum endpointID selecionado!");
                        }
                    }else if(patternComunicationObject instanceof MyWorkerObject){
                        if(spinIdsConnected.getSelectedItem() != null) {
                            MyWorkerObject mro = (MyWorkerObject) patternComunicationObject;
                            mro.send(txtMensagem.getText().toString().getBytes(),endpointIDSelected);
                        }else{
                            mostrarMensagemDeControleEmTela("Nenhum endpointID selecionado!");
                        }
                    }
                    addNovaMensagem(txtMensagem.getText().toString(), endpointIDSelected, "Enviado");
                    txtMensagem.setText("");

                    mostrarMensagemDeControleEmTela("Conteúdo enviado!");
                }
            }
        });

        btnSubscreverServico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(spinIdsConnected.getSelectedItem() != null) {
                    MySubscriberObject mso = (MySubscriberObject) patternComunicationObject;
                    mso.subscrever((String) spinIdsConnected.getSelectedItem());

                }else{
                    mostrarMensagemDeControleEmTela("Nenhum endpointID selecionado!");
                }
            }
        });

        btnOkProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(patternComunicationObject.getComportamento() == Comportamento.VENTILATOR || patternComunicationObject.getComportamento() == Comportamento.WORKER){
                    Concluivel concl = (Concluivel) patternComunicationObject;
                    concl.comunicarConclusao();
                }
            }
        });
    }

    private void inicializarObjetosDeComunicacao(){
        if(!txtNickName.getText().equals("")){
            if (spinComportamento.getSelectedItemId() == 0){
                patternComunicationObject = new MyPublisherObject(this);
                txtMensagem.setEnabled(true);
                btnEnviar.setEnabled(true);
            }else if (spinComportamento.getSelectedItemId() == 1){
                patternComunicationObject = new MySubscriberObject(this);
            }else if (spinComportamento.getSelectedItemId() == 2){
                patternComunicationObject = new MyReqReplyObject(Comportamento.REQUESTER, this);
            }else if (spinComportamento.getSelectedItemId() == 3){
                patternComunicationObject = new MyReqReplyObject(Comportamento.REPLYER, this);
            }else if (spinComportamento.getSelectedItemId() == 4){
                patternComunicationObject = new MyVentilatorObject(this);
                btnOkProcess.setEnabled(true);
            }else if (spinComportamento.getSelectedItemId() == 5){
                patternComunicationObject = new MyWorkerObject(this);
                btnOkProcess.setEnabled(true);
            }else if (spinComportamento.getSelectedItemId() == 6){
                patternComunicationObject = new MySyncObject(this);
            }

            nearbyAccessObject = new NearbyAccessObject(patternComunicationObject, txtNickName.getText().toString(), GlobalApplication.getContext().getApplicationContext().getString(R.string.service_id));
            patternComunicationObject.setNearbyAccessObject(nearbyAccessObject);
            if(arrayAdapter == null){
                arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, patternComunicationObject.getEndpointIDsConnectedString());
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinIdsConnected.setAdapter(arrayAdapter);
            }
            btnIniciarAnuncDesc.setEnabled(true);
            spinModo.setEnabled(true);
            btnConfirmComport.setEnabled(false);
            spinComportamento.setEnabled(false);
            txtNickName.setEnabled(false);


            Toast.makeText(GlobalApplication.getContext().getApplicationContext(), "NickName e Comportamento confirmados! O dispositivo está pronto para se conectar a outros dispositivos.", Toast.LENGTH_LONG).show();
        }
    }

    public void liberarCamposEnvio(){
        if(patternComunicationObject != null && (patternComunicationObject.getComportamento() == Comportamento.PUBLISHER || patternComunicationObject.getComportamento() == Comportamento.REQUESTER || patternComunicationObject.getComportamento() == Comportamento.REPLYER || patternComunicationObject.getComportamento() == Comportamento.VENTILATOR || patternComunicationObject.getComportamento() == Comportamento.WORKER)){
            txtMensagem.setEnabled(true);
            btnEnviar.setEnabled(true);


        }
        if(patternComunicationObject.getComportamento() == Comportamento.SUBSCRIBER){
            btnSubscreverServico.setEnabled(true);
        }

        if(patternComunicationObject.getComportamento() == Comportamento.VENTILATOR || patternComunicationObject.getComportamento() == Comportamento.WORKER){
            btnOkProcess.setEnabled(true);
        }
        arrayAdapter.notifyDataSetChanged();
    }

    public void mostrarMensagemDeControleEmTela(String s){
        Toast.makeText(GlobalApplication.getContext().getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }

    public void atualizarSpinIDsConectados(){
        if(patternComunicationObject.getEndpointIDsConnectedString().size() > 0) {
            arrayAdapter.notifyDataSetChanged();
        }else{
            arrayAdapter.clear();
        }
    }

    public void addNovaMensagem(String s, String endpointID, String modo){
        mensagens.add(new Mensagem(s, modo, endpointID));
        adapt.notifyDataSetChanged();
    }
}
