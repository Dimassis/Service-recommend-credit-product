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
import java.util.Optional;
import java.util.UUID;

@Component
public class RecommendationSimpleCredit implements RecommendationRuleSet {

    private final JdbcTemplate jdbcTemplate;
    private final String DESCRIPTION = "Откройте мир выгодных кредитов с нами! Ищете способ быстро и без лишних хлопот получить нужную сумму? Тогда наш выгодный кредит — именно то, что вам нужно";

    public RecommendationSimpleCredit(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<List<Recommendation>> getRecommendation(UUID userId) {

        String sql1 = "SELECT COUNT(*) = 0\n" +
                "FROM PRODUCTS p\n" +
                "JOIN TRANSACTIONS t ON p.ID = t.PRODUCT_ID\n" +
                "WHERE p.TYPE = 'CREDIT'\n" +
                "AND t.USER_ID = ?;";

        String sql2 = "SELECT\n" +
                "    (SUM(CASE WHEN t.AMOUNT > 0 THEN t.AMOUNT ELSE 0 END) >\n" +
                "     SUM(CASE WHEN t.AMOUNT < 0 THEN ABS(t.AMOUNT) ELSE 0 END))\n" +
                "FROM PRODUCTS p\n" +
                "JOIN TRANSACTIONS t ON p.ID = t.PRODUCT_ID\n" +
                "WHERE p.TYPE = 'DEBIT'\n" +
                "AND t.USER_ID = ?;";

        String sql3 = "SELECT\n" +
                "    (SUM(CASE WHEN t.AMOUNT < 0 THEN ABS(t.AMOUNT) ELSE 0 END) > 100000)\n" +
                "FROM PRODUCTS p\n" +
                "JOIN TRANSACTIONS t ON p.ID = t.PRODUCT_ID\n" +
                "WHERE p.TYPE = 'DEBIT'\n" +
                "AND t.USER_ID = ?;";

        boolean noCreditProducts = Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql1, Boolean.class, userId));
        boolean debitIncomeGreaterThanSpending = Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql2, Boolean.class, userId));
        boolean debitSpendingGreaterThan100k = Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql3, Boolean.class, userId));

        if (noCreditProducts || debitIncomeGreaterThanSpending || debitSpendingGreaterThan100k) {

            return Optional.of(List.of(new Recommendation(userId,"Простой кредит", DESCRIPTION)));
        } else {
            return Optional.of(List.of(new Recommendation(userId, "Простой кредит", "Нет рекомендации")));
        }
    }
}
