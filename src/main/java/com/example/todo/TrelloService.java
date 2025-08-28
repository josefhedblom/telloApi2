package com.example.todo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Service
public class TrelloService {

    @Value("${trello.url}")     private String baseUrl;     // ex: https://api.trello.com/1
    @Value("${trello.apiKey}")  private String apiKey;      // din key
    @Value("${trello.token}")   private String token;       // ditt token
    @Value("${trello.listId}")  private String listId;      // listan där kort skapas

    private final RestTemplate rest = new RestTemplate();

    public TrelloCard createCardForTask(Task t) {
        // defensiv koll för att ge tydligt fel i UI
        if (isBlank(baseUrl) || isBlank(apiKey) || isBlank(token) || isBlank(listId)) {
            throw new IllegalStateException("Trello-konfiguration saknas (trello.url/apiKey/token/listId).");
        }

        URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl + "/cards")
                .queryParam("idList", listId)
                .queryParam("key", apiKey)
                .queryParam("token", token)
                .build(true) // behåll encoding
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();
        body.put("name", t.getTitle());
        body.put("desc", "Skapad från Spring-app");
        if (t.getDueDate() != null) body.put("due", t.getDueDate().toString());

        HttpEntity<Map<String, Object>> req = new HttpEntity<>(body, headers);
        ResponseEntity<TrelloCard> resp = rest.postForEntity(uri, req, TrelloCard.class);

        if (resp.getStatusCode().is2xxSuccessful() && resp.getBody() != null) {
            return resp.getBody();
        }
        throw new RuntimeException("Kunde inte skapa Trello-kort: " + resp.getStatusCode());
    }

    private boolean isBlank(String s) { return s == null || s.trim().isEmpty(); }

    // Minimal DTO för Trellos svar
    public static class TrelloCard {
        public String id;
        public String shortUrl;
        public String url;
        public String name;
    }
}
