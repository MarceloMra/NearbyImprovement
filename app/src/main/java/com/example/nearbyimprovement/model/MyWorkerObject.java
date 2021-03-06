package com.example.nearbyimprovement.model;

import com.example.nearbyfenix.improvement.EndpointInfo;
import com.example.nearbyfenix.improvement.WorkerObject;
import com.example.nearbyimprovement.activities.MainActivity;

import java.nio.charset.StandardCharsets;

public class MyWorkerObject extends WorkerObject {
    private MainActivity mainActivity;

    public MyWorkerObject(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
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
    public void receive(byte[] dados, String endpointID) {
        mainActivity.addNovaMensagem(new String(dados, StandardCharsets.UTF_8), endpointID, "Recebido");
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

    @Override
    public void onComunicacaoDeConclusaoRecebida(String endpointID) {
        mainActivity.addNovaMensagem("Recebido uma confirmação de processamento finalizado", endpointID, "recebido");
    }
}
