package com.example.nearbyimprovement.model;

import com.example.nearbyimprovement.enums.Comportamento;
import com.example.nearbyimprovement.interfaces.Receiver;
import com.example.nearbyimprovement.interfaces.Sender;
import com.google.android.gms.nearby.connection.Payload;

public class ReqReplyObject extends PatternComunicationObject implements Receiver, Sender {

    public ReqReplyObject() {
        super.comportamento = Comportamento.REQ_REPLY;
    }

    @Override
    public Payload receive() {
        return null;
    }

    @Override
    public void send(byte[] dados, String endPointID) {

    }

    @Override
    public Comportamento getComportamento() {
        return null;
    }
}
