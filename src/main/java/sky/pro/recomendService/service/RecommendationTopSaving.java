package sky.pro.recomendService.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import sky.pro.recomendService.model.Recommendation;
import sky.pro.recomendService.repository.RecommendationsRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Component
public class RecommendationTopSaving implements RecommendationRuleSet {
    private final JdbcTemplate jdbcTemplate;
    private final RecommendationsRepository repository;

    public RecommendationTopSaving(JdbcTemplate jdbcTemplate, RecommendationsRepository repository) {
        this.jdbcTemplate = jdbcTemplate;
        this.repository = repository;
    }

    @Override
    public Optional<List<Recommendation>> getRecommendation(UUID userId) {
        String sql1 = "SELECT COUNT(*) > 0\n" +
                "FROM PRODUCTS p\n" +
                "JOIN TRANSACTIONS t ON p.ID = t.PRODUCT_ID\n" +
                "WHERE p.TYPE = 'DEBIT'\n" +
                "AND t.USER_ID = ?;";

        String sql2 = "SELECT\n" +
                "    (SUM(CASE WHEN p.TYPE = 'DEBIT' THEN t.AMOUNT ELSE 0 END) >= 50000) AS debit_condition,\n" +
                "    (SUM(CASE WHEN p.TYPE = 'SAVING' THEN t.AMOUNT ELSE 0 END) >= 50000) AS saving_condition\n" +
                "FROM PRODUCTS p\n" +
                "         JOIN TRANSACTIONS t ON p.ID = t.PRODUCT_ID\n" +
                "WHERE t.AMOUNT > 0\n" +
                "  AND t.USER_ID = ?";


        Map<String, Object> result2 = jdbcTemplate.queryForMap(sql2, userId);
        Boolean debitCondition = (Boolean) result2.get("debit_condition");
        Boolean savingCondition = (Boolean) result2.get("saving_condition");

        debitCondition = (debitCondition != null) ? debitCondition : false;
        savingCondition = (savingCondition != null) ? savingCondition : false;

        boolean exists2 = debitCondition || savingCondition;

        String sql3 = "SELECT\n" +
                "    (SUM(CASE WHEN t.AMOUNT > 0 THEN t.AMOUNT ELSE 0 END) >\n" +
                "     SUM(CASE WHEN t.AMOUNT < 0 THEN ABS(t.AMOUNT) ELSE 0 END))\n" +
                "FROM PRODUCTS p\n" +
                "         JOIN TRANSACTIONS t ON p.ID = t.PRODUCT_ID\n" +
                "WHERE p.TYPE = 'DEBIT'\n" +
                "  AND t.USER_ID = ?";

        boolean exists1 = Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql1, Boolean.class, userId));
        boolean exists3 = Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql3, Boolean.class, userId));

        if (exists1 || exists2 || exists3) {
            Path path = Paths.get("src/main/resources/recommendTopSavingDescription.txt");
            String description;
            try {
                description = Files.readString(path);
            } catch (IOException e) {
                throw new RuntimeException();
            }
            return repository.getListRecommendation(new Recommendation(userId, "Top saving", description));
        } else {
            return repository.getListRecommendation(new Recommendation(userId, "Top saving", "No recommendation"));
        }
    }
}
