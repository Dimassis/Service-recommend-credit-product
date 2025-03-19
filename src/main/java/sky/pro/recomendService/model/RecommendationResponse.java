package sky.pro.recomendService.model;

import java.util.List;

// отвечает за формирование структуры ответа,
// который будет возвращаться клиенту при запросе рекомендаций для конкретного пользователя.
public class RecommendationResponse {  //
    private String userId;
    private List<Recommendation> recommendations;

    public RecommendationResponse(String userId, List<Recommendation> recommendations) {
        this.userId = userId;
        this.recommendations = recommendations;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<Recommendation> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(List<Recommendation> recommendations) {
        this.recommendations = recommendations;
    }
}
