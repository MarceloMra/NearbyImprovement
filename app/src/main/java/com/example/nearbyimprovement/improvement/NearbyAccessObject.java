package com.example.nearbyimprovement.improvement;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.nearbyimprovement.R;
import com.example.nearbyimprovement.enums.Comportamento;
import com.example.nearbyimprovement.enums.TipoPacote;
import com.example.nearbyimprovement.model.GlobalApplication;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.AdvertisingOptions;
import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback;
import com.google.android.gms.nearby.connection.ConnectionResolution;
import com.google.android.gms.nearby.connection.ConnectionsStatusCodes;
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo;
import com.google.android.gms.nearby.connection.DiscoveryOptions;
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate;
import com.google.android.gms.nearby.connection.Strategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;

public class NearbyAccessObject {
    private final String SERVICE_ID;
    private PatternComunicationObject patternComunicationObject;
    private String nickname;
    private Strategy strategy;

    private final PayloadCallback mPayloadCallback = new PayloadCallback() {
        @Override
        public void onPayloadReceived(String endPointId, Payload payload) {
            Toast.makeText(GlobalApplication.getContext().getApplicationContext(), "Payload recebido! ", Toast.LENGTH_LONG).show();
            Pacote pacote = null;
            try {
                pacote = (Pacote) Services.deserialize(payload.asBytes());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            if(pacote.getTipo() == TipoPacote.CONTROL){
                switch ((String) pacote.getConteudo()){
                    case "-@-pub-@-":
                        if(patternComunicationObject.getComportamento() != Comportamento.SUBSCRIBER){
                            //fecha a conexão por incompatibilidade dos comportamentos
                            fecharConexao(endPointId);
                        }else{
                            //notifica ao patternObject o endpointID do novo dispositivo conectado
                            adicionarNovoEndpointID(endPointId);
                        }
                        break;
                    case "-@-sub-@-":
                        if(patternComunicationObject.getComportamento() != Comportamento.PUBLISHER){
                            //fecha a conexão por incompatibilidade dos comportamentos
                            fecharConexao(endPointId);
                        }else{
                            //notifica ao patternObject o endpointID do novo dispositivo conectado
                            adicionarNovoEndpointID(endPointId);
                        }
                        break;
                    case "-@-req-@-":
                        if(patternComunicationObject.getComportamento() != Comportamento.REPLYER){
                            //fecha a conexão por incompatibilidade dos comportamentos
                            fecharConexao(endPointId);
                        }else{
                            //notifica ao patternObject o endpointID do novo dispositivo conectado
                            adicionarNovoEndpointID(endPointId);
                        }
                        break;
                    case "-@-rep-@-":
                        if(patternComunicationObject.getComportamento() != Comportamento.REQUESTER){
                            //fecha a conexão por incompatibilidade dos comportamentos
                            fecharConexao(endPointId);
                        }else{
                            //notifica ao patternObject o endpointID do novo dispositivo conectado
                            adicionarNovoEndpointID(endPointId);
                        }
                        break;
                    case "-@-subscribe-@-":
                        if(patternComunicationObject.getComportamento() != Comportamento.PUBLISHER){
                            //fecha a conexão por incompatibilidade dos comportamentos ou retornar uma mensagem de controle
                            comunicacaoSubscricao(endPointId, "-@-Failsubscribe-@-");
                            fecharConexao(endPointId);
                        }else{
                            //notifica ao patternObject o endpointID do novo dispositivo conectado
                            PublisherObject po = (PublisherObject) patternComunicationObject;
                            po.addNovaSubscricao(endPointId);
                            comunicacaoSubscricao(endPointId, "-@-OKsubscribe-@-");
                        }
                        break;
                    case "-@-OKsubscribe-@-":
                        if(patternComunicationObject.getComportamento() != Comportamento.SUBSCRIBER){
                            //fecha a conexão por incompatibilidade dos comportamentos ou retornar uma mensagem de controle
                            fecharConexao(endPointId);
                        }else{
                            //notifica ao patternObject o endpointID do novo dispositivo conectado
                            SubscriberObject so = (SubscriberObject) patternComunicationObject;
                            so.onOkSubscription(endPointId);
                        }
                        break;
                    case "-@-Failsubscribe-@-":
                        if(patternComunicationObject.getComportamento() != Comportamento.SUBSCRIBER){
                            //fecha a conexão por incompatibilidade dos comportamentos ou retornar uma mensagem de controle
                            fecharConexao(endPointId);
                        }else{
                            //notifica ao patternObject o endpointID do novo dispositivo conectado
                            SubscriberObject so = (SubscriberObject) patternComunicationObject;
                            so.onFailSubscription(endPointId);
                        }
                        break;
                }
            }else if(pacote.getTipo() == TipoPacote.CONTENT){
                //REPASSAR O PAYLOAD RECEBIDO PARA O patternObject
                if(patternComunicationObject.getComportamento() == Comportamento.SUBSCRIBER){
                    SubscriberObject so = (SubscriberObject) patternComunicationObject;
                    try {
                        so.receive(Services.serialize(pacote.getConteudo()), endPointId);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else if(patternComunicationObject.getComportamento() == Comportamento.REPLYER || patternComunicationObject.getComportamento() == Comportamento.REQUESTER){
                    ReqReplyObject rro = (ReqReplyObject) patternComunicationObject;
                    try {
                        rro.receive(Services.serialize(pacote.getConteudo()), endPointId);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        @Override
        public void onPayloadTransferUpdate(String endpointId, PayloadTransferUpdate payloadTransferUpdate) {
            //Notifying the receivers

        }
    };

    private final EndpointDiscoveryCallback mEndpointDiscoveryCallback = new EndpointDiscoveryCallback() {
        @Override
        public void onEndpointFound(final String endpointId, final DiscoveredEndpointInfo discoveredEndpointInfo) {
            com.google.android.gms.nearby.Nearby.getConnectionsClient(GlobalApplication.getContext().getApplicationContext()).requestConnection(
                    nickname,
                    endpointId,
                    mConnectionLifecycleCallback)
                    .addOnSuccessListener(
                            new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unusedResult) {
                                    Toast.makeText(GlobalApplication.getContext().getApplicationContext(), "Endpoint encontrado! ", Toast.LENGTH_LONG).show();

                                }
                            })
                    .addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(GlobalApplication.getContext().getApplicationContext(), "Falha no endpoint encontrado! ", Toast.LENGTH_LONG).show();

                                }
                            });
        }

        @Override
        public void onEndpointLost(String endpointId) {
            Toast.makeText(GlobalApplication.getContext().getApplicationContext(), "Endpoint perdido! ", Toast.LENGTH_LONG).show();
        }
    };

    private final ConnectionLifecycleCallback mConnectionLifecycleCallback = new ConnectionLifecycleCallback() {
        @Override
        public void onConnectionInitiated(final String endpointId, ConnectionInfo connectionInfo) {
            com.google.android.gms.nearby.Nearby.getConnectionsClient(GlobalApplication.getContext().getApplicationContext()).acceptConnection(endpointId, mPayloadCallback);
            Toast.makeText(GlobalApplication.getContext().getApplicationContext(), "Conexão aceita! ", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onConnectionResult(String endpointId, ConnectionResolution result) {

            switch (result.getStatus().getStatusCode()) {
                case ConnectionsStatusCodes.STATUS_OK:

                    String comp = "";
                    switch (patternComunicationObject.getComportamento()){
                        case REQUESTER: comp = "-@-req-@-";
                            if(verificarQuantidadeConexoes(endpointId, 1)){
                                return;
                            }
                            break;
                        case SUBSCRIBER: comp = "-@-sub-@-";
                            if(verificarQuantidadeConexoes(endpointId, 1)){
                                return;
                            }
                            break;
                        case PUBLISHER: comp = "-@-pub-@-";
                            break;
                        case REPLYER: comp = "-@-rep-@-";
                            if(verificarQuantidadeConexoes(endpointId, 1)){
                                return;
                            }
                            break;
                    }
                    Pacote pac = new Pacote(TipoPacote.CONTROL, comp);
                    Payload p = null;
                    try {
                        p = Payload.fromBytes(Services.serialize(pac));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Nearby.getConnectionsClient(GlobalApplication.getContext().getApplicationContext()).sendPayload(endpointId, p);
                    Toast.makeText(GlobalApplication.getContext().getApplicationContext(), "Endpoint conectado com sucesso! ", Toast.LENGTH_LONG).show();
                    break;
                case ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED:
                    Toast.makeText(GlobalApplication.getContext().getApplicationContext(), "Conexão rejeitada! ", Toast.LENGTH_LONG).show();
                    break;
                case ConnectionsStatusCodes.STATUS_ERROR:
                    Toast.makeText(GlobalApplication.getContext().getApplicationContext(), "Erro após aceitar a conexão! ", Toast.LENGTH_LONG).show();
                    break;
            }
        }

        @Override
        public void onDisconnected(String endpointId) {
            patternComunicationObject.removeEndpointID(endpointId);
            patternComunicationObject.conexaoEncerrada(endpointId);
            Toast.makeText(GlobalApplication.getContext().getApplicationContext(), "Endpoint desconectou-se! ", Toast.LENGTH_LONG).show();
        }
    };

    private boolean verificarQuantidadeConexoes(String endpointID, int qtdConexõesMaximas){
        if(patternComunicationObject.getEndpointIDsConnected().size() > qtdConexõesMaximas){
            fecharConexao(endpointID);
            return true;
        }else{
            return false;
        }
    }

    public NearbyAccessObject(PatternComunicationObject patternComunicationObject, String nickname) {
        SERVICE_ID = GlobalApplication.getContext().getString(R.string.service_id);
        this.nickname = nickname;
        if(patternComunicationObject != null){
            strategy = Strategy.P2P_STAR;

            /*switch (patternComunicationObject.getComportamento()) {
                case SUBSCRIBER:
                    strategy = Strategy.P2P_POINT_TO_POINT;
                    break;
                case PUBLISHER:
                    strategy = Strategy.P2P_STAR;
                    break;
                case REQUESTER:
                    strategy = Strategy.P2P_POINT_TO_POINT;
                    break;
                case REPLYER:
                    strategy = Strategy.P2P_STAR;
                    break;
            }*/
            Toast.makeText(GlobalApplication.getContext().getApplicationContext(), "NearbyAccesObject instanciado com Strategy."+strategy.toString(), Toast.LENGTH_LONG).show();
            this.patternComunicationObject = patternComunicationObject;
        }
    }

    public void send(String endpointID, byte[] dados){
        Pacote pac = new Pacote(TipoPacote.CONTENT, dados);
        Payload p = null;
        try {
            p = Payload.fromBytes(Services.serialize(pac));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Nearby.getConnectionsClient(GlobalApplication.getContext().getApplicationContext()).sendPayload(endpointID, p);
    }

    public void comunicacaoSubscricao(String endpointID, String dadoControle){
        Pacote pac = new Pacote(TipoPacote.CONTROL, dadoControle);
        Payload p = null;
        try {
            p = Payload.fromBytes(Services.serialize(pac));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Nearby.getConnectionsClient(GlobalApplication.getContext().getApplicationContext()).sendPayload(endpointID, p);
    }

    private void fecharConexao(String endpointID){
        com.google.android.gms.nearby.Nearby.getConnectionsClient(GlobalApplication.getContext().getApplicationContext()).disconnectFromEndpoint(endpointID);
        Toast.makeText(GlobalApplication.getContext().getApplicationContext(), "Conexão encerrada!", Toast.LENGTH_LONG).show();
    }

    private void adicionarNovoEndpointID(String endpointID){
        patternComunicationObject.addNewEndpointID(endpointID);
    }

    public void startAdvertising() {
        AdvertisingOptions advertisingOptions = new AdvertisingOptions.Builder().setStrategy(strategy).build();
        Nearby.getConnectionsClient(GlobalApplication.getContext().getApplicationContext())
                .startAdvertising(nickname, SERVICE_ID, mConnectionLifecycleCallback, advertisingOptions)
                .addOnSuccessListener(
                        new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unusedResult) {
                                //Notifying the receivers
                                patternComunicationObject.onSuccessStartAdvertising();
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                //Notifying the receivers
                                patternComunicationObject.onFeilureStartAdvertising(e);
                            }
                        });
    }

    public void startDiscovery() {
        DiscoveryOptions discoveryOptions = new DiscoveryOptions.Builder().setStrategy(strategy).build();
        Nearby.getConnectionsClient(GlobalApplication.getContext().getApplicationContext())
                .startDiscovery(SERVICE_ID, mEndpointDiscoveryCallback, discoveryOptions)
                .addOnSuccessListener(
                        new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unusedResult) {
                                patternComunicationObject.onSuccessStartDiscovery();
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                patternComunicationObject.onFeilureStartDiscovery(e);
                            }
                        });
    }

    public void setPatternComunicationObject(PatternComunicationObject patternComunicationObject) {
        this.patternComunicationObject = patternComunicationObject;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

}
