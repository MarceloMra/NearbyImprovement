package com.example.nearbyimprovement.model;

import com.example.nearbyimprovement.activities.MainActivity;
import com.example.nearbyimprovement.enums.Comportamento;
import com.example.nearbyimprovement.improvement.ReqReplyObject;

import java.nio.charset.StandardCharsets;

public class MyReqReplyObject extends ReqReplyObject {
    private MainActivity mainActivity;

    public MyReqReplyObject(Comportamento c, MainActivity mainActivity) {
        super(c);
        this.mainActivity = mainActivity;
    }

    @Override
    public void receive(byte[] dados, String endpointID) {
        mainActivity.addNovaMensagem(new String(dados, StandardCharsets.UTF_8));
    }

    @Override
    protected void novaConexaoEfetuada(String endpointID) {
        mainActivity.liberarCamposEnvio();
    }
}
