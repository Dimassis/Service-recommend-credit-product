package sky.pro.recomendService.service;

import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final RecommendationService recommendationService;

    public UserService(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    public List<RecommendationDTO> getRecommendationsForUser(long id) {
        // Вызов сервиса рекомендаций для получения списка рекомендаций
        return recommendationService.getRecommendations(id);
    }
}
