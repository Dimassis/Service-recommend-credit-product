package sky.pro.recomendService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import sky.pro.recomendService.model.Recommendation;
import sky.pro.recomendService.repository.RecommendationsRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class RecommendationRepositoryTest {

    @Mock
    private final JdbcTemplate jdbcTemplate = mock(JdbcTemplate.class);

    @InjectMocks
    private final RecommendationsRepository recommendationsRepository = new RecommendationsRepository(jdbcTemplate);

    private static final String TOP_SAVING_NAME = "Top Saving";
    private static final String INVEST_500 = "Invest 500";
    private final String NOT_RECOMMENDATIONS = "Нет рекомендаций";

    @Test
    public void shouldBackTrue_testConditionsTopSaving() throws Exception {
        UUID userId = UUID.randomUUID();
        when(recommendationsRepository.recommendTopSaving(userId)).thenReturn(true);

        Optional<List<Recommendation>> result = recommendationsRepository.getListRecommendation(userId);

        assertTrue(result.isPresent());

        List<Recommendation> recommendations = result.get();

        assertEquals(1, recommendations.size());

        Recommendation recommendation = recommendations.get(0);


        assertEquals(TOP_SAVING_NAME, recommendation.getName());
    }

    @Test
    public void shouldBackFalse_testConditionsTopSaving() {
        UUID userId = UUID.randomUUID();
        when(recommendationsRepository.recommendTopSaving(userId)).thenReturn(false);

        Optional<List<Recommendation>> result = recommendationsRepository.getListRecommendation(userId);

        assertTrue(result.isPresent());

        List<Recommendation> recommendations = result.get();

        assertEquals(1, recommendations.size());

        Recommendation recommendation = recommendations.get(0);

        assertEquals(NOT_RECOMMENDATIONS, recommendation.getDescription());
    }
    @Test
    public void shouldBackTrue_testConditionsInvest500() throws Exception {
        UUID userId = UUID.randomUUID();
        when(recommendationsRepository.recommendInvest500(userId)).thenReturn(true);

        Optional<List<Recommendation>> result = recommendationsRepository.getListRecommendation(userId);

        assertTrue(result.isPresent());

        List<Recommendation> recommendations = result.get();

        assertEquals(1, recommendations.size());

        Recommendation recommendation = recommendations.get(0);


        assertEquals(INVEST_500, recommendation.getName());
    }

    @Test
    public void shouldBackFalse_testConditionsInvest500() {
        UUID userId = UUID.randomUUID();
        when(recommendationsRepository.recommendInvest500(userId)).thenReturn(false);

        Optional<List<Recommendation>> result = recommendationsRepository.getListRecommendation(userId);

        assertTrue(result.isPresent());

        List<Recommendation> recommendations = result.get();

        assertEquals(1, recommendations.size());

        Recommendation recommendation = recommendations.get(0);

        assertEquals(NOT_RECOMMENDATIONS, recommendation.getDescription());
    }

}
