package com.example.nearbyimprovement.improvement;

import com.example.nearbyimprovement.enums.Comportamento;
import com.example.nearbyimprovement.interfaces.RecebedorDeConclusoes;
import com.example.nearbyimprovement.interfaces.Receiver;

public abstract class SyncObject extends PatternComunicationObject implements Receiver, RecebedorDeConclusoes {

    public SyncObject() {
        super();
        super.comportamento = Comportamento.SYNCR;
    }
}
