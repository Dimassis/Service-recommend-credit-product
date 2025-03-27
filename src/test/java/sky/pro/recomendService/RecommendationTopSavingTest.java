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
import sky.pro.recomendService.service.RecommendationService;
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

    @InjectMocks
    private RecommendationTopSaving topSavingService;

    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
    }

    @Test
    void shouldReturnRecommendation_WhenConditionsAreMet() {

        String description = "Откройте свою собственную «Копилку» с нашим банком! «Копилка» — это уникальный банковский инструмент, который поможет вам легко и удобно накапливать деньги на важные цели";
        Recommendation expectedRecommendation = new Recommendation(userId, "Top saving", description);

        when(jdbcTemplate.queryForObject(anyString(), eq(Boolean.class), eq(userId))).thenReturn(true); // Conditions met

        Optional<List<Recommendation>> recommendations = topSavingService.getRecommendation(userId);

        assertTrue(recommendations.isPresent());
        assertEquals("Top saving", recommendations.get().get(0).getName());
        assertEquals(description, recommendations.get().get(0).getDescription());
    }

    @Test
    void shouldReturnNoRecommendation_WhenConditionsAreNotMet() {
        String expectedDescription = "No recommendation";
        Recommendation expectedRecommendation = new Recommendation(userId, "Top saving", expectedDescription);

        when(jdbcTemplate.queryForObject(anyString(), eq(Boolean.class), eq(userId)))
                .thenReturn(false);

        Optional<List<Recommendation>> recommendations = topSavingService.getRecommendation(userId);

        assertTrue(recommendations.isPresent());
        assertEquals("Top saving", recommendations.get().get(0).getName());
    }
}