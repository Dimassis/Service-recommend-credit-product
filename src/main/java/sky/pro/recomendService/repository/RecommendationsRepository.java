package sky.pro.recomendService.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import sky.pro.recomendService.model.Recommendation;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public class RecommendationsRepository {
    private final JdbcTemplate jdbcTemplate;

    public RecommendationsRepository(@Qualifier("recommendationsJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<List<Recommendation>> getListRecommendation(UUID userId) {
        List<Recommendation> recommendations = new ArrayList<>();
        if (recommendTopService(userId)) {
            Path pathTopService = Paths.get("src/main/resources/recommendTopServiceDescription.txt");
            String description;
            try {
                description = Files.readString(pathTopService);
            } catch (IOException e) {
                throw new RuntimeException();
            }
            recommendations.add(new Recommendation(userId, "Top Saving", description));

        }

        if (recommendInvest500(userId)) {
            //
        }

        if (recommendJustCredit(userId)) {
            //
        }

        if (!recommendations.isEmpty()) {
            return Optional.of(recommendations);
        }

        return Optional.of(List.of(new Recommendation(userId, "No Recommendation", "Нет рекомендаций")));
    }


    public boolean recommendTopService(UUID userId) {
        Boolean exists1 = jdbcTemplate.queryForObject(
                """
                        SELECT COUNT(*) > 0
                        FROM PRODUCTS p
                        JOIN TRANSACTIONS t ON p.ID = t.PRODUCT_ID
                        WHERE p.TYPE = 'DEBIT'
                        AND t.USER_ID = ?
                        """,
                Boolean.class,
                userId
        );

        Map<String, Object> result2 = jdbcTemplate.queryForMap(
                """
                        SELECT
                            (SUM(CASE WHEN p.TYPE = 'DEBIT' THEN t.AMOUNT ELSE 0 END) >= 50000) AS debit_condition,
                            (SUM(CASE WHEN p.TYPE = 'SAVING' THEN t.AMOUNT ELSE 0 END) >= 50000) AS saving_condition
                        FROM PRODUCTS p
                        JOIN TRANSACTIONS t ON p.ID = t.PRODUCT_ID
                        WHERE t.AMOUNT > 0
                        AND t.USER_ID = ?
                        """,
                userId
        );
        Boolean debitCondition = (Boolean) result2.get("debit_condition");
        Boolean savingCondition = (Boolean) result2.get("saving_condition");

        Boolean exists3 = jdbcTemplate.queryForObject(
                """
                        SELECT
                            (SUM(CASE WHEN t.AMOUNT > 0 THEN t.AMOUNT ELSE 0 END) >
                             SUM(CASE WHEN t.AMOUNT < 0 THEN ABS(t.AMOUNT) ELSE 0 END))
                        FROM PRODUCTS p
                        JOIN TRANSACTIONS t ON p.ID = t.PRODUCT_ID
                        WHERE p.TYPE = 'DEBIT'
                        AND t.USER_ID = ?
                        """,
                Boolean.class,
                userId
        );
        return Boolean.TRUE.equals(exists1) || Boolean.TRUE.equals(debitCondition) || Boolean.TRUE.equals(savingCondition) || Boolean.TRUE.equals(exists3);
    }


    public boolean recommendInvest500(UUID userId) {
        return false;
    }

    public boolean recommendJustCredit(UUID userId) {
        return false;
    }

}