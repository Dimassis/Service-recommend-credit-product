package sky.pro.recomendService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sky.pro.recomendService.model.Recommendation;
import sky.pro.recomendService.repository.RecommendationsRepository;
import sky.pro.recomendService.service.RecommendationService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RecommendationServiceTest {
    @Mock
    RecommendationService recommendationService;

    @InjectMocks
    RecommendationsRepository recommendationsRepository;

    @Test
    public void testRecommendationService() {
        UUID userId = UUID.randomUUID();
        String name = "Any Name";
        String description = "Any Description";

        List<Recommendation> recommendations = List.of(new Recommendation(userId, name, description));

        when(recommendationService.getRecommendation(userId)).thenReturn(Optional.of(recommendations));

        Optional<List<Recommendation>> result = recommendationService.getRecommendation(userId);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().size());

        Recommendation recommendation = result.get().get(0);

        assertEquals(name, recommendation.getName());
        assertEquals(description, recommendation.getDescription());
    }
}
