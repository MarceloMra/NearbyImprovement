package com.example.nearbyimprovement.model;

import androidx.annotation.NonNull;

import com.example.nearbyimprovement.R;
import com.example.nearbyimprovement.enums.Comportamento;
import com.example.nearbyimprovement.enums.TipoPacote;
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class NearbyAccessObject {
    private final String SERVICE_ID;
    private PatternComunicationObject patternComunicationObject;
    private String nickname;
    private Strategy strategy;

    private final PayloadCallback mPayloadCallback = new PayloadCallback() {
        @Override
        public void onPayloadReceived(String endPointId, Payload payload) {
            Pacote pacote = null;
            try {
                pacote = (Pacote) deserialize(payload.asBytes());
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
                }
            }else if(pacote.getTipo() == TipoPacote.CONTENT){
                //REPASSAR O PAYLOAD RECEBIDO PARA O patternObject
                if(patternComunicationObject.getComportamento() == Comportamento.SUBSCRIBER){
                    SubscriberObject so = (SubscriberObject) patternComunicationObject;
                    so.receive(payload.asBytes(), endPointId);
                }else if(patternComunicationObject.getComportamento() == Comportamento.REPLYER || patternComunicationObject.getComportamento() == Comportamento.REQUESTER){
                    ReqReplyObject rro = (ReqReplyObject) patternComunicationObject;
                    rro.receive(payload.asBytes(), endPointId);
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


                                }
                            })
                    .addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {


                                }
                            });
        }

        @Override
        public void onEndpointLost(String endpointId) {

        }
    };

    private final ConnectionLifecycleCallback mConnectionLifecycleCallback = new ConnectionLifecycleCallback() {
        @Override
        public void onConnectionInitiated(final String endpointId, ConnectionInfo connectionInfo) {
            com.google.android.gms.nearby.Nearby.getConnectionsClient(GlobalApplication.getContext().getApplicationContext()).acceptConnection(endpointId, mPayloadCallback);
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
                    }
                    Pacote pac = new Pacote(TipoPacote.CONTROL, comp);
                    Payload p = null;
                    try {
                        p = Payload.fromBytes(serialize(pac));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Nearby.getConnectionsClient(GlobalApplication.getContext().getApplicationContext()).sendPayload(endpointId, p);

                    break;
                case ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED:

                    break;
                case ConnectionsStatusCodes.STATUS_ERROR:

                    break;
            }
        }

        @Override
        public void onDisconnected(String endpointId) {
            patternComunicationObject.removeEndpointID(endpointId);
        }
    };

    public NearbyAccessObject(PatternComunicationObject patternComunicationObject, String nickname) {
        SERVICE_ID = GlobalApplication.getContext().getString(R.string.service_id);
        this.nickname = nickname;

        if(patternComunicationObject != null){
            switch (patternComunicationObject.getComportamento()) {
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
            }

            this.patternComunicationObject = patternComunicationObject;
        }
    }

    private byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(obj);
        return out.toByteArray();
    }

    private Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        return is.readObject();
    }

    public void send(String endpointID, byte[] dados){
        Pacote pac = new Pacote(TipoPacote.CONTENT, dados);
        Payload p = null;
        try {
            p = Payload.fromBytes(serialize(pac));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Nearby.getConnectionsClient(GlobalApplication.getContext().getApplicationContext()).sendPayload(endpointID, p);
    }

    private void fecharConexao(String endpointID){
        com.google.android.gms.nearby.Nearby.getConnectionsClient(GlobalApplication.getContext().getApplicationContext()).disconnectFromEndpoint(endpointID);
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
