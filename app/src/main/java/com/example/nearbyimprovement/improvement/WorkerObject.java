package com.example.nearbyimprovement.improvement;

import com.example.nearbyimprovement.enums.Comportamento;
import com.example.nearbyimprovement.enums.TipoPacote;
import com.example.nearbyimprovement.interfaces.Receiver;
import com.example.nearbyimprovement.interfaces.Sender;

public abstract class WorkerObject extends PatternComunicationObject implements Sender, Receiver {

    public WorkerObject() {
        super();
        super.comportamento = Comportamento.WORKER;
    }

    public abstract void onComunicacaoDeConclusaoRecebida(String endpointID);

    @Override
    public void send(byte[] dados, String endPointID) {
        nearbyAccessObject.send(endPointID, dados, TipoPacote.CONTENT);
    }
}
