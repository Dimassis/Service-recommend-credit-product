package sky.pro.recomendService.service;

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
public class RecommendationInvest500 implements RecommendationRuleSet {
    private final JdbcTemplate jdbcTemplate;
    private final String DESCRIPTION = "Откройте свой путь к успеху с индивидуальным инвестиционным счетом (ИИС) от нашего банка! Воспользуйтесь налоговыми льготами и начните инвестировать с умом";

    public RecommendationInvest500(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<List<Recommendation>> getRecommendation(UUID userId) {
        // использует ли пользователь хотя бы один продукт типа DEBIT
        String sql1 = "SELECT COUNT(*)\n" +
                "FROM TRANSACTIONS t\n" +
                "         JOIN PRODUCTS p ON t.PRODUCT_ID = p.ID\n" +
                "WHERE t.USER_ID = ?\n" +
                "  AND p.TYPE = 'DEBIT';";

        // пользователь не использует продукты типа INVEST
        String sql2 = "SELECT COUNT(*)\n" +
                "FROM TRANSACTIONS t\n" +
                "         JOIN PRODUCTS p ON t.PRODUCT_ID = p.ID\n" +
                "WHERE t.USER_ID = ?\n" +
                "  AND p.TYPE = 'INVEST';";

        // Сумма пополнений продуктов с типом SAVING больше 1000 ₽
        String sql3 = "SELECT COALESCE(SUM(t.AMOUNT), 0)\n" +
                "FROM TRANSACTIONS t\n" +
                "         JOIN PRODUCTS p ON t.PRODUCT_ID = p.ID\n" +
                "WHERE t.USER_ID = ? AND p.TYPE = 'SAVING' AND t.TYPE = 'DEPOSIT';";

        Map<String, Object> result2 = jdbcTemplate.queryForMap(sql2, userId);
        Boolean debitCondition = (Boolean) result2.get("debit_condition");
        Boolean savingCondition = (Boolean) result2.get("saving_condition");

        debitCondition = (debitCondition != null) ? debitCondition : false;
        savingCondition = (savingCondition != null) ? savingCondition : false;

        boolean exists2 = debitCondition || savingCondition;
        boolean exists1 = Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql1, Boolean.class, userId));
        boolean exists3 = Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql3, Boolean.class, userId));

        if (exists1 || exists2 || exists3) {
            return Optional.of(List.of(new Recommendation(userId, "Invest 500", DESCRIPTION)));
        } else {
            return Optional.of(List.of(new Recommendation(userId, "Invest 500", "No recommendation")));
        }
    }
}
