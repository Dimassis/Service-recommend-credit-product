package sky.pro.recomendService.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import sky.pro.recomendService.model.Recommendation;
import sky.pro.recomendService.model.RecommendationResponse;
import sky.pro.recomendService.service.RecommendationService;

import java.util.List;

@RestController
public class RecommendationController {
    private RecommendationService recommendationService;

    @GetMapping("/recommendation/{userId}")
    public ResponseEntity<RecommendationResponse> getRecommendations(@PathVariable String userId) {
        List<Recommendation> recommendations = recommendationService.getRecommendationsForUser(userId);
        RecommendationResponse response = new RecommendationResponse(userId, recommendations);
        return ResponseEntity.ok(response);
    }
}
