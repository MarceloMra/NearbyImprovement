package com.example.nearbyimprovement.model;

import com.example.nearbyimprovement.enums.Comportamento;
import com.example.nearbyimprovement.interfaces.Receiver;
import com.google.android.gms.nearby.connection.Payload;

public class SubscriberObject extends PatternComunicationObject implements Receiver {


    public SubscriberObject() {
        super.comportamento = Comportamento.SUBSCRIBER;
    }

    @Override
    public Payload receive() {
        return null;
    }

    @Override
    public Comportamento getComportamento() {
        return null;
    }
}
