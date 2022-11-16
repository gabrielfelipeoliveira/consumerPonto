package com.consumer.ponto.listener;

import com.consumer.ponto.model.EStatusPonto;
import com.consumer.ponto.model.InfoPontoDTO;
import com.google.gson.Gson;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class PontoListener {

    private static String URL_PONTO = "http://localhost:8085/";
    private static String URL_LEGADO = "https://api.mockytonk.com/proxy/ab2198a3-cafd-49d5-8ace-baac64e72222";

    @KafkaListener(topics = "topic-1", groupId = "group-1")
    public void listen(String message) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(message))
                .uri(URI.create(URL_LEGADO))
                .build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .join();

        atualizarPonto(message);
    }

    private void atualizarPonto(String pontoJson) {
        InfoPontoDTO ponto = new Gson().fromJson(pontoJson, InfoPontoDTO.class);
        ponto.setStatusPonto(EStatusPonto.SUCESSO);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(URL_PONTO + "atualizar/" + ponto.getId()))
                .build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .join();
    }

}
