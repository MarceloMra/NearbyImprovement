package com.example.nearbyimprovement.improvement;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.nearbyimprovement.R;
import com.example.nearbyimprovement.enums.Comportamento;
import com.example.nearbyimprovement.enums.TipoPacote;
import com.example.nearbyimprovement.interfaces.RecebedorDeConclusoes;
import com.example.nearbyimprovement.interfaces.Receiver;
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
                            if(podeAdiconarMaisUmaConexao(1)) {
                                adicionarNovoEndpointID(endPointId, Comportamento.PUBLISHER);
                            }else{
                                fecharConexao(endPointId);
                            }
                        }
                        break;
                    case "-@-sub-@-":
                        if(patternComunicationObject.getComportamento() != Comportamento.PUBLISHER){
                            //fecha a conexão por incompatibilidade dos comportamentos
                            fecharConexao(endPointId);
                        }else{
                            //notifica ao patternObject o endpointID do novo dispositivo conectado
                            adicionarNovoEndpointID(endPointId, Comportamento.SUBSCRIBER);
                        }
                        break;
                    case "-@-req-@-":
                        if(patternComunicationObject.getComportamento() != Comportamento.REPLYER){
                            //fecha a conexão por incompatibilidade dos comportamentos
                            fecharConexao(endPointId);
                        }else{
                            //notifica ao patternObject o endpointID do novo dispositivo conectado
                            if(podeAdiconarMaisUmaConexao(1)) {
                                adicionarNovoEndpointID(endPointId, Comportamento.REQUESTER);
                            }else{
                                fecharConexao(endPointId);
                            }
                        }
                        break;
                    case "-@-rep-@-":
                        if(patternComunicationObject.getComportamento() != Comportamento.REQUESTER){
                            //fecha a conexão por incompatibilidade dos comportamentos
                            fecharConexao(endPointId);
                        }else{
                            //notifica ao patternObject o endpointID do novo dispositivo conectado
                            if(podeAdiconarMaisUmaConexao(1)) {
                                adicionarNovoEndpointID(endPointId, Comportamento.REPLYER);
                            }else{
                                fecharConexao(endPointId);
                            }
                        }
                        break;
                    case "-@-vent-@-":
                        if(patternComunicationObject.getComportamento() != Comportamento.WORKER){
                            //fecha a conexão por incompatibilidade dos comportamentos
                            fecharConexao(endPointId);
                        }else{
                            //notifica ao patternObject o endpointID do novo dispositivo conectado
                            if(getQtdConexoesCompWorker(Comportamento.VENTILATOR) < 1){
                                adicionarNovoEndpointID(endPointId, Comportamento.VENTILATOR);
                            }else{
                                fecharConexao(endPointId);
                            }
                        }
                        break;
                    case "-@-work-@-":
                        if(patternComunicationObject.getComportamento() != Comportamento.SYNCR || patternComunicationObject.getComportamento() != Comportamento.VENTILATOR){
                            //fecha a conexão por incompatibilidade dos comportamentos
                            fecharConexao(endPointId);
                        }else{
                            //notifica ao patternObject o endpointID do novo dispositivo conectado
                            adicionarNovoEndpointID(endPointId, Comportamento.WORKER);
                        }
                        break;
                    case "-@-sync-@-":
                        if(patternComunicationObject.getComportamento() != Comportamento.WORKER){
                            //fecha a conexão por incompatibilidade dos comportamentos
                            fecharConexao(endPointId);
                        }else{
                            //notifica ao patternObject o endpointID do novo dispositivo conectado
                            if(getQtdConexoesCompWorker(Comportamento.SYNCR) < 1){
                                adicionarNovoEndpointID(endPointId, Comportamento.SYNCR);
                            }else{
                                fecharConexao(endPointId);
                            }
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
                    case "-@-OKprocessing-@-":
                        if(patternComunicationObject.getComportamento() != Comportamento.WORKER || patternComunicationObject.getComportamento() != Comportamento.SYNCR){
                            //fecha a conexão por incompatibilidade dos comportamentos ou retornar uma mensagem de controle
                            fecharConexao(endPointId);
                        }else{
                            //notifica ao patternObject o endpointID do novo dispositivo conectado
                            RecebedorDeConclusoes rcc = (RecebedorDeConclusoes) patternComunicationObject;
                            rcc.onComunicacaoDeConclusaoRecebida(endPointId);
                        }
                        break;
                }
            }else if(pacote.getTipo() == TipoPacote.CONTENT){
                //REPASSAR O PAYLOAD RECEBIDO PARA O patternObject
                /*
                if(patternComunicationObject.getComportamento() == Comportamento.SUBSCRIBER){
                    SubscriberObject so = (SubscriberObject) patternComunicationObject;
                    so.receive((byte[]) pacote.getConteudo(), endPointId);
                }else if(patternComunicationObject.getComportamento() == Comportamento.REPLYER || patternComunicationObject.getComportamento() == Comportamento.REQUESTER){
                    ReqReplyObject rro = (ReqReplyObject) patternComunicationObject;
                    rro.receive((byte[]) pacote.getConteudo(), endPointId);
                } else if(patternComunicationObject.getComportamento() == Comportamento.WORKER){
                    WorkerObject wo = (WorkerObject) patternComunicationObject;
                    wo.receive((byte[]) pacote.getConteudo(), endPointId);
                }else if(patternComunicationObject.getComportamento() == Comportamento.SYNCR){
                    SyncObject so = (SyncObject) patternComunicationObject;
                    so.receive((byte[]) pacote.getConteudo(), endPointId);
                }
                 */

                if(patternComunicationObject.getComportamento() == Comportamento.SUBSCRIBER ||
                        patternComunicationObject.getComportamento() == Comportamento.REPLYER ||
                        patternComunicationObject.getComportamento() == Comportamento.REQUESTER ||
                        patternComunicationObject.getComportamento() == Comportamento.WORKER ||
                        patternComunicationObject.getComportamento() == Comportamento.SYNCR){
                    Receiver r = (Receiver) patternComunicationObject;
                    r.receive((byte[]) pacote.getConteudo(), endPointId);
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
                            break;
                        case SUBSCRIBER: comp = "-@-sub-@-";
                            break;
                        case PUBLISHER: comp = "-@-pub-@-";
                            break;
                        case REPLYER: comp = "-@-rep-@-";
                            break;
                        case VENTILATOR: comp = "-@-vent-@-";
                            break;
                        case WORKER: comp = "-@-work-@-";
                            break;
                        case SYNCR: comp = "-@-sync-@-";
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
            patternComunicationObject.conexaoEncerrada(new EndpointInfo(endpointId, patternComunicationObject.getComportamentoDoEndpointID(endpointId)));

            Toast.makeText(GlobalApplication.getContext().getApplicationContext(), "Endpoint desconectou-se! ", Toast.LENGTH_LONG).show();
        }
    };

    private boolean podeAdiconarMaisUmaConexao(int qtdConexoesMaximas){
        if(patternComunicationObject.getEndpointIDsConnected().size() < qtdConexoesMaximas){
            return true;
        }else{
            return false;
        }
    }

    private int getQtdConexoesCompWorker(Comportamento compVerificar){
        int qtdCompConected = 0;

        //Caso expecional do Worker que só pode se conectar à no máximo 2 sendo 1 ventilator e um sync
        if(patternComunicationObject.getComportamento() == Comportamento.WORKER){
            for(EndpointInfo epi : patternComunicationObject.getEndpointIDsConnected()){
                if(epi.getComportamento() == compVerificar) {
                    qtdCompConected++;
                }
            }
        }
        return qtdCompConected;
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

    public void send(String endpointID, byte[] dados, TipoPacote tipoPacote){
        Pacote pac = new Pacote(tipoPacote, dados);
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

    private void adicionarNovoEndpointID(String endpointID, Comportamento c){
        patternComunicationObject.addNewEndpointID(endpointID, c);
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
