package com.example.nearbyimprovement.improvement;

import android.widget.Toast;

import com.example.nearbyimprovement.enums.Comportamento;
import com.example.nearbyimprovement.interfaces.Receiver;
import com.example.nearbyimprovement.model.GlobalApplication;

public class SubscriberObject extends PatternComunicationObject implements Receiver {


    public SubscriberObject() {
        super.comportamento = Comportamento.SUBSCRIBER;
    }

    public void subscrever(String endpointID){
        nearbyAccessObject.comunicacaoSubscricao(endpointID, "-@-subscribe-@-");
        Toast.makeText(GlobalApplication.getContext().getApplicationContext(), "requisição de Subscrição efetuada! ", Toast.LENGTH_LONG).show();
    }


    public void onOkSubscription(String endpointID){

    }

    public void onFailSubscription(String endpointID){

    }

    @Override
    protected void novaConexaoEfetuada(String endpointID) {

    }

    @Override
    protected void conexaoEncerrada(String endpointID) {

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

    @Override
    public void receive(byte[] dados, String endpointID) {

    }

}
