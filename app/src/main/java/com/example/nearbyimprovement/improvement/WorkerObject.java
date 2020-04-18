package com.example.nearbyimprovement.improvement;

import com.example.nearbyimprovement.enums.Comportamento;
import com.example.nearbyimprovement.enums.TipoPacote;
import com.example.nearbyimprovement.interfaces.Concluivel;
import com.example.nearbyimprovement.interfaces.RecebedorDeConclusoes;
import com.example.nearbyimprovement.interfaces.Receiver;
import com.example.nearbyimprovement.interfaces.Sender;

public abstract class WorkerObject extends PatternComunicationObject implements Sender, Receiver, Concluivel, RecebedorDeConclusoes {

    public WorkerObject() {
        super();
        super.comportamento = Comportamento.WORKER;
    }

    @Override
    public void send(byte[] dados, String endPointID) {
        for(EndpointInfo e : getEndpointIDsConnected()){
            if(e.getEndpointID().equals(endPointID)){
                if(e.getComportamento() == Comportamento.SYNCR){
                    nearbyAccessObject.send(endPointID, dados, TipoPacote.CONTENT);
                }
                break;
            }
        }
    }

    @Override
    public void comunicarConclusao(){
        for (EndpointInfo epi : super.getEndpointIDsConnected()){
            if(epi.getComportamento() == Comportamento.SYNCR){
                nearbyAccessObject.comunicaConclusao(epi.getEndpointID(), "-@-OKprocessing-@-");
            }
        }
    }
}
