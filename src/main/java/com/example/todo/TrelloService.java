package com.example.todo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Service
public class TrelloService {

    @Value("${trello.apiKey}")   private String key;
    @Value("${trello.token") private String token;
    @Value("${trello.list-id}") private String listId;

    private final RestTemplate rest = new RestTemplate();

    public TrelloCard createCardForTask(Task t) {
        // Trellos endpoint för att skapa kort
        String url = "https://api.trello.com/1/cards"
                + "?idList=" + listId
                + "&key=" + key
                + "&token=" + token;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();
        body.put("name", t.getTitle());
        body.put("desc", "Skapad från Spring-app");
        if (t.getDueDate() != null) {
            body.put("due", t.getDueDate().toString());
        }

        HttpEntity<Map<String, Object>> req = new HttpEntity<>(body, headers);
        ResponseEntity<TrelloCard> resp = rest.postForEntity(URI.create(url), req, TrelloCard.class);

        if (resp.getStatusCode().is2xxSuccessful() && resp.getBody() != null) {
            return resp.getBody();
        }
        throw new RuntimeException("Kunde inte skapa Trello-kort: " + resp.getStatusCode());
    }

    // Minimal DTO för Trellos svar
    public static class TrelloCard {
        public String id;
        public String shortUrl;
        public String url;
        public String name;
    }
}
