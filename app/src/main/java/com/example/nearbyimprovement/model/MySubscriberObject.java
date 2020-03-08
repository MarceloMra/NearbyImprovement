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
    public void receive(byte[] dados, String endpointID) {
        mainActivity.addNovaMensagem(new String(dados, StandardCharsets.UTF_8));
    }
}
