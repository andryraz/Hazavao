package com.example.demo.endpoint.rest.controller;

import com.example.demo.service.OpenAIService;
import java.util.HashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.*;

@RestController
public class HazavaoController {

  private final OpenAIService openAIService;

  public HazavaoController(OpenAIService openAIService) {
    this.openAIService = openAIService;
  }

  @GetMapping("/hazavao")
  public Map<String, String> hazavao(@RequestParam String teny) {
    Map<String, String> result = new HashMap<>();
    try {
      String definition = openAIService.getMalagasyDefinition(teny);
      result.put("mot", teny);
      result.put("definition", definition.trim());
    } catch (Exception e) {
      result.put("erreur", "Tsy afaka nahazo valiny: " + e.getMessage());
    }
    return result;
  }
}
