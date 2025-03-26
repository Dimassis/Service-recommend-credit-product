package sky.pro.recomendService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import sky.pro.recomendService.model.Recommendation;
import sky.pro.recomendService.repository.RecommendationsRepository;
import sky.pro.recomendService.service.RecommendationTopSaving;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RecommendationTopSavingTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private RecommendationsRepository repository;

    @InjectMocks
    private RecommendationTopSaving topSavingService;

    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
    }

    @Test
    void shouldReturnRecommendation() throws Exception {
        String description = "Text";
        Recommendation expectedRecommendation = new Recommendation(userId, "Top saving", description);

        when(jdbcTemplate.queryForObject(anyString(), eq(Boolean.class), eq(userId))).thenReturn(true);

        when(repository.getListRecommendation(any(Recommendation.class))).thenReturn(Optional.of(List.of(expectedRecommendation)));

        Optional<List<Recommendation>> recommendations = topSavingService.getRecommendation(userId);

        assertEquals("Top saving", recommendations.get().get(0).getName());
        assertEquals(description, recommendations.get().get(0).getDescription());
    }

    @Test
    void shouldReturnNoRecommendation_WhenConditionsAreNotMet() throws Exception {
        String expectedDescription = "Any String";

        Recommendation expectedRecommendation = new Recommendation(userId, "Top saving", expectedDescription);

        when(jdbcTemplate.queryForObject(anyString(), eq(Boolean.class), eq(userId))).thenReturn(false);
        when(repository.getListRecommendation(any(Recommendation.class)))
                .thenReturn(Optional.of(List.of(expectedRecommendation)));

        Optional<List<Recommendation>> recommendations = topSavingService.getRecommendation(userId);

        assertEquals("Top saving", recommendations.get().get(0).getName());
        assertNotEquals("No recommendsdation", recommendations.get().get(0).getDescription());
    }

}