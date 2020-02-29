package com.example.nearbyimprovement.model;

import androidx.annotation.NonNull;

import com.example.nearbyimprovement.R;
import com.example.nearbyimprovement.enums.Comportamento;
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

import java.nio.charset.StandardCharsets;

public class NearbyAccessObject {
    private final String SERVICE_ID;
    private PatternComunicationObject patternComunicationObject;
    private String nickname;
    private Strategy strategy;

    private final PayloadCallback mPayloadCallback = new PayloadCallback() {
        @Override
        public void onPayloadReceived(String endPointId, Payload payload) {
            String recv = new String(payload.asBytes(), StandardCharsets.UTF_8);
            switch (recv){
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
                default:
                    //REPASSAR O PAYLOAD RECEBIDO PARA O patternObject
                    if(patternComunicationObject instanceof SubscriberObject){
                        SubscriberObject so = (SubscriberObject) patternComunicationObject;
                        so.receive(payload);
                    }else if(patternComunicationObject instanceof ReqReplyObject){
                        ReqReplyObject rro = (ReqReplyObject) patternComunicationObject;
                        rro.receive(payload);
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
                    Payload p = Payload.fromBytes(comp.getBytes());
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
            if(patternComunicationObject instanceof SubscriberObject){
                strategy = Strategy.P2P_POINT_TO_POINT;
            }else if(patternComunicationObject instanceof PublisherObject){
                strategy = Strategy.P2P_STAR;
            }else if(patternComunicationObject instanceof ReqReplyObject){
                if(patternComunicationObject.getComportamento() == Comportamento.REQUESTER){
                    strategy = Strategy.P2P_POINT_TO_POINT;
                }else if(patternComunicationObject.getComportamento() == Comportamento.REPLYER){
                    strategy = Strategy.P2P_STAR;
                }
            }

            this.patternComunicationObject = patternComunicationObject;
        }
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
