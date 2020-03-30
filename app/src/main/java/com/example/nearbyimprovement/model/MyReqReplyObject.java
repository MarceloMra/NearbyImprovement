package com.example.nearbyimprovement.model;

import com.example.nearbyimprovement.activities.MainActivity;
import com.example.nearbyimprovement.enums.Comportamento;
import com.example.nearbyimprovement.improvement.EndpointInfo;
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
        mainActivity.addNovaMensagem(new String(dados, StandardCharsets.UTF_8), endpointID, "Recebido");
    }

    @Override
    protected void novaConexaoEfetuada(EndpointInfo endpointInfo) {
        mainActivity.liberarCamposEnvio();
        mainActivity.atualizarSpinIDsConectados();
        mainActivity.mostrarMensagemDeControleEmTela("Nova conexão com "+endpointInfo.getEndpointID()+"!");
    }

    @Override
    protected void conexaoEncerrada(EndpointInfo endpointInfo) {
        mainActivity.mostrarMensagemDeControleEmTela("Conexão com "+endpointInfo.getEndpointID()+" foi encerrada!");
        mainActivity.atualizarSpinIDsConectados();
    }

    @Override
    public void onSuccessStartAdvertising() {
        mainActivity.mostrarMensagemDeControleEmTela("Anunciamento iniciado!");
    }

    @Override
    public void onFeilureStartAdvertising(Exception e) {
        mainActivity.mostrarMensagemDeControleEmTela("Erro ao iniciar Descoberta: "+e.getMessage());
    }

    @Override
    public void onSuccessStartDiscovery() {
        mainActivity.mostrarMensagemDeControleEmTela("Descoberta iniciada!");
    }

    @Override
    public void onFeilureStartDiscovery(Exception e) {
        mainActivity.mostrarMensagemDeControleEmTela("Erro ao iniciar Descoberta: "+e.getMessage());
    }
}
