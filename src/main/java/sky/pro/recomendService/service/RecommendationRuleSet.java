package sky.pro.recomendService.service;

import sky.pro.recomendService.model.Recommendation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RecommendationRuleSet {
    Optional<List<Recommendation>> getRecommendation(UUID userId);
}
