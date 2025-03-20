package sky.pro.recomendService.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import sky.pro.recomendService.model.Recommendation;

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
        if (hasDebitProduct(userId)) {
            return Optional.of(new Recommendation(
                    UUID.randomUUID(),
                    "Debit Product",
                    "You have a debit product! Here's your recommendation."
            ));
        }
        return Optional.empty();
    }

    private boolean hasDebitProduct(UUID userId) {
        String sql = """
                 SELECT COUNT(*) FROM PRODUCTS p WHERE id = '250ada57-6f5b-4f30-b1ec-513786d008f9' and p.TYPE = 'DEBIT'""";

        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId);
        return count != null && count > 0;
    }
}