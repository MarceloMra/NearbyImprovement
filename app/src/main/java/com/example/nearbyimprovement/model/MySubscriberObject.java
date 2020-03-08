package com.example.nearbyimprovement.model;

import com.example.nearbyimprovement.activities.MainActivity;
import com.example.nearbyimprovement.improvement.SubscriberObject;

import java.nio.charset.StandardCharsets;

public class MySubscriberObject extends SubscriberObject {
    private MainActivity mainActivity;

    public MySubscriberObject(MainActivity mainActivity) {
        super();
        this.mainActivity = mainActivity;
    }

    @Override
    public void onOkSubscription(String endpointID){

    }

    @Override
    public void onFailSubscription(String endpointID){

    }

    @Override
    protected void novaConexaoEfetuada(String endpointID) {
        mainActivity.liberarCamposEnvio();
    }

    @Override
    public void receive(byte[] dados, String endpointID) {
        mainActivity.addNovaMensagem(new String(dados, StandardCharsets.UTF_8));
    }

    @Override
    public void onSuccessStartAdvertising() {
        mainActivity.onSuccessStartAdvertising();
    }

    @Override
    public void onFeilureStartAdvertising(Exception e) {
        mainActivity.onFeilureStartAdvertising(e);
    }

    @Override
    public void onSuccessStartDiscovery() {
        mainActivity.onSuccessStartDiscovery();
    }

    @Override
    public void onFeilureStartDiscovery(Exception e) {
        mainActivity.onFeilureStartDiscovery(e);
    }
}
