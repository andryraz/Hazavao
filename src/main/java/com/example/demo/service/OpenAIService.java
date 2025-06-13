package com.example.demo.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.springframework.stereotype.Service;

@Service
public class OpenAIService {

  private static final String API_URL = "https://api.openai.com/v1/chat/completions";
  private static final String API_KEY =
      "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";

  public String getMalagasyDefinition(String word) throws Exception {
    String prompt = "Hazavao amin'ny teny malagasy ny dikan'ny teny: " + word;

    // Création du corps de la requête JSON
    String requestBody =
        """
        {
          "model": "gpt-3.5-turbo",
          "messages": [
            {"role": "user", "content": "%s"}
          ]
        }
        """
            .formatted(prompt);

    // Création de la requête HTTP
    HttpRequest request =
        HttpRequest.newBuilder()
            .uri(URI.create(API_URL))
            .header("Authorization", "Bearer " + API_KEY)
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
            .build();

    // Envoi de la requête
    HttpClient client = HttpClient.newHttpClient();
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

    // Affichage de la réponse brute pour debug
    System.out.println("Réponse OpenAI brute : " + response.body());

    // Traitement de la réponse JSON
    ObjectMapper mapper = new ObjectMapper();
    JsonNode root = mapper.readTree(response.body());

    // Vérification et extraction
    if (root.has("choices") && root.get("choices").isArray() && root.get("choices").size() > 0) {
      JsonNode contentNode = root.get("choices").get(0).get("message").get("content");
      return contentNode != null ? contentNode.asText().trim() : "Tsy nisy valiny azo.";
    } else if (root.has("error")) {
      String errorMessage = root.get("error").get("message").asText();
      throw new RuntimeException("Erreur OpenAI: " + errorMessage);
    } else {
      throw new RuntimeException("Réponse inattendue de l'API OpenAI.");
    }
  }
}
