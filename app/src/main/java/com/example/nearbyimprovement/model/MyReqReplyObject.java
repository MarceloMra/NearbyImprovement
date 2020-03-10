package com.example.nearbyimprovement.model;

import com.example.nearbyimprovement.activities.MainActivity;
import com.example.nearbyimprovement.enums.Comportamento;
import com.example.nearbyimprovement.improvement.ReqReplyObject;
import com.example.nearbyimprovement.improvement.Services;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class MyReqReplyObject extends ReqReplyObject {
    private MainActivity mainActivity;

    public MyReqReplyObject(Comportamento c, MainActivity mainActivity) {
        super(c);
        this.mainActivity = mainActivity;
    }

    @Override
    public void receive(byte[] dados, String endpointID) {
        try {
            mainActivity.addNovaMensagem(new String(((byte[]) (Services.deserialize(dados))), StandardCharsets.UTF_8), endpointID, "Recebido");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void novaConexaoEfetuada(String endpointID) {
        mainActivity.liberarCamposEnvio();
        mainActivity.atualizarSpinIDsConectados();
        mainActivity.mostrarMensagemDeControleEmTela("Nova conexão com "+endpointID+"!");
    }

    @Override
    protected void conexaoEncerrada(String endpointID) {
        mainActivity.mostrarMensagemDeControleEmTela("Conexão com "+endpointID+" foi encerrada!");
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