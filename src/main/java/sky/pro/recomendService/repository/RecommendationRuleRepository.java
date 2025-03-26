package sky.pro.recomendService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sky.pro.recomendService.model.RecommendationRule;

import java.util.Optional;
import java.util.UUID;

public interface RecommendationRuleRepository extends JpaRepository<RecommendationRule, Long> {
    Optional<RecommendationRule> findByProductId(UUID productId);
    void deleteByProductId(UUID productId);
}
