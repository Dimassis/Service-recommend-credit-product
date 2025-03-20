package sky.pro.recomendService.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import sky.pro.recomendService.model.Recommendation;
import sky.pro.recomendService.model.RecommendationResponse;
import sky.pro.recomendService.service.RecommendationService;

import java.util.List;
import java.util.UUID;

@RestController
public class RecommendationController {

    private RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping("/recommendation/{userId}")
    public ResponseEntity<RecommendationResponse> getRecommendations(@PathVariable UUID userId) {
        List<Recommendation> recommendations = recommendationService.getRecommendationsForUser(userId);
        RecommendationResponse response = new RecommendationResponse(userId.toString(), recommendations);
        return ResponseEntity.ok(response);
    }
}
