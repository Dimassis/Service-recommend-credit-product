package sky.pro.recomendService.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import sky.pro.recomendService.model.Recommendation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class RecommendationsRepository {
    private final JdbcTemplate jdbcTemplate;

    public RecommendationsRepository(@Qualifier("recommendationsJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Recommendation> getListRecommendation(UUID userId) {
        String query = "SELECT COUNT(*) FROM products p JOIN TRANSACTIONS t ON p.ID = t.PRODUCT_ID WHERE p.TYPE = 'DEBIT' AND t.USER_ID = ?";
        Integer count = jdbcTemplate.queryForObject(query, Integer.class, userId);
        System.out.println(count);
        if (count != null && count > 0) {
            // Пример создания Recommendation (можно заменить на реальную логику)
            Recommendation recommendation = new Recommendation(userId, "Some Product", "Some Description");
            return Optional.of(recommendation);
        } else {
            return Optional.empty();
        }
    }
}