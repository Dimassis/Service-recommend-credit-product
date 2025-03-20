package sky.pro.recomendService.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import sky.pro.recomendService.model.Recommendation;

import java.util.List;
import java.util.UUID;

@Service
public class RecommendationService {
    //RecommendationService — это отдельный сервис, который будет отвечать за проверку правил и формирование рекомендаций.

    private JdbcTemplate jdbcTemplate;

    public RecommendationService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean usesDebitProducts(UUID userId) {
        String sql = "SELECT COUNT(*) FROM TRANSACTIONS t JOIN PRODUCTS p ON t.PRODUCT_ID = p.ID WHERE t.USER_ID = ? AND p.TYPE = 'DEBIT'";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId);
        return count != null && count > 0;
    }

    public boolean doesNotUseInvestProducts(UUID userId) {
        String sql = "SELECT COUNT(*) FROM TRANSACTIONS t JOIN PRODUCTS p ON t.PRODUCT_ID = p.ID WHERE t.USER_ID = ? AND p.TYPE = 'INVEST'";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId);
        return count == null || count == 0;
    }

    public int getTotalDepositsForDebit(UUID userId) {
        String sql = "SELECT COALESCE(SUM(t.AMOUNT), 0) FROM TRANSACTIONS t JOIN PRODUCTS p ON t.PRODUCT_ID = p.ID WHERE t.USER_ID = ? AND p.TYPE = 'DEBIT' AND t.TYPE = 'DEPOSIT'";
        return jdbcTemplate.queryForObject(sql, Integer.class, userId);
    }

    public int getTotalWithdrawalsForDebit(UUID userId) {
        String sql = "SELECT COALESCE(SUM(t.AMOUNT), 0) FROM TRANSACTIONS t JOIN PRODUCTS p ON t.PRODUCT_ID = p.ID WHERE t.USER_ID = ? AND p.TYPE = 'DEBIT' AND t.TYPE = 'WITHDRAW'";
        return jdbcTemplate.queryForObject(sql, Integer.class, userId);
    }

    public List<Recommendation> getRecommendationsForUser(UUID userId) {
        // Логика для проверки правил и формирования рекомендаций
        // Пример:
        if (usesDebitProducts(userId) && doesNotUseInvestProducts(userId)) {
            // Возвращаем рекомендацию Invest 500
            return List.of(new Recommendation("Invest 500", "147fea0f-3b9f-413b-ab9e-87f08fd60d5a", "Описание Invest 500"));
        }
        return List.of();
    }
}
