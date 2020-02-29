package com.example.nearbyimprovement.model;

import com.example.nearbyimprovement.enums.Comportamento;
import com.example.nearbyimprovement.interfaces.Receiver;
import com.example.nearbyimprovement.interfaces.Sender;
import com.google.android.gms.nearby.connection.Payload;

public class ReqReplyObject extends PatternComunicationObject implements Receiver, Sender {

    public ReqReplyObject(Comportamento c) {
        super.comportamento = c;
    }

    @Override
    public void receive(Payload p) {

    }

    @Override
    public void send(byte[] dados, String endPointID) {

    }

    @Override
    protected void novaConexaoEfetuada(String endpointID) {

    }

    @Override
    public void onSuccessStartAdvertising() {

    }

    @Override
    public void onFeilureStartAdvertising(Exception e) {

    }

    @Override
    public void onSuccessStartDiscovery() {

    }

    @Override
    public void onFeilureStartDiscovery(Exception e) {

    }
}
