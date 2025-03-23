package sky.pro.recomendService.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import sky.pro.recomendService.model.Recommendation;
import sky.pro.recomendService.repository.RecommendationsRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RecommendationService implements RecommendationRuleSet {

   private final RecommendationsRepository repository;

    public RecommendationService(RecommendationsRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<List<Recommendation>> getRecommendation(UUID userId) {
        return repository.getListRecommendation(userId);
    }
}
