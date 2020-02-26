package com.example.nearbyimprovement.model;

import androidx.annotation.NonNull;

import com.example.nearbyimprovement.R;
import com.example.nearbyimprovement.interfaces.Receiver;
import com.example.nearbyimprovement.interfaces.Sender;
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

public class NearbyAccessObject {
    private final String SERVICE_ID;
    private Receiver receiver;
    private Sender sender;
    private String nickname;
    private Strategy strategy;

    public NearbyAccessObject(Receiver r, Sender s) {
        SERVICE_ID = GlobalApplication.getContext().getString(R.string.service_id);

        if(r != null){
            switch (r.getComportamento()){
                case PUBLISHER: strategy = Strategy.P2P_STAR;
                    break;
                case SUBSCRIBER: strategy = Strategy.P2P_POINT_TO_POINT;
                    break;
                case REQ_REPLY: strategy = Strategy.P2P_STAR;
                    break;
                default: strategy = Strategy.P2P_STAR;
                    break;
            }
        }
        if(s != null){
            switch (s.getComportamento()){
                case PUBLISHER: strategy = Strategy.P2P_STAR;
                    break;
                case SUBSCRIBER: strategy = Strategy.P2P_POINT_TO_POINT;
                    break;
                case REQ_REPLY: strategy = Strategy.P2P_STAR;
                    break;
                default: strategy = Strategy.P2P_STAR;
                    break;
            }
        }
        this.receiver = r;
        this.sender = s;
    }

    private final PayloadCallback mPayloadCallback = new PayloadCallback() {
        @Override
        public void onPayloadReceived(String endPointId, Payload payload) {
            //new String(payload.asBytes(), StandardCharsets.UTF_8)
            //Notifying the receivers


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
            //Notifying the receivers

        }

        @Override
        public void onConnectionResult(String endpointId, ConnectionResolution result) {

            switch (result.getStatus().getStatusCode()) {
                case ConnectionsStatusCodes.STATUS_OK:

                    break;
                case ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED:

                    break;
                case ConnectionsStatusCodes.STATUS_ERROR:

                    break;
            }
        }

        @Override
        public void onDisconnected(String endpointId) {

        }
    };

    public void startAdvertising() {
        AdvertisingOptions advertisingOptions = new AdvertisingOptions.Builder().setStrategy(strategy).build();
        Nearby.getConnectionsClient(GlobalApplication.getContext().getApplicationContext())
                .startAdvertising(nickname, SERVICE_ID, mConnectionLifecycleCallback, advertisingOptions)
                .addOnSuccessListener(
                        new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unusedResult) {
                                //Notifying the receivers

                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                //Notifying the receivers

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

                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
    }



}
