package sky.pro.recomendService.service;

import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import sky.pro.recomendService.model.Recommendation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class RecommendationSimpleCredit implements RecommendationRuleSet {

    private final JdbcTemplate jdbcTemplate;
    private final Cache<String, Boolean> cache;
    private final String DESCRIPTION = "Откройте мир выгодных кредитов с нами! Ищете способ быстро и без лишних хлопот получить нужную сумму? Тогда наш выгодный кредит — именно то, что вам нужно";
    private final String NO_RECOMMENDATION = "Нет рекомендации";

    public RecommendationSimpleCredit(JdbcTemplate jdbcTemplate, Cache<String, Boolean> cache) {
        this.jdbcTemplate = jdbcTemplate;
        this.cache = cache;
    }

    @Override
    public Optional<List<Recommendation>> getRecommendation(UUID userId) {
        // Генерируем уникальные ключи для кэша
        String cacheKeyNoCredit = userId + "_NO_CREDIT_PRODUCTS";
        String cacheKeyIncomeGreater = userId + "_INCOME_GREATER_THAN_SPENDING";
        String cacheKeySpendingGreater = userId + "_SPENDING_GREATER_THAN_100K";

        // Проверяем кэш или выполняем запросы
        Boolean noCreditProducts = cache.getIfPresent(cacheKeyNoCredit);
        if (noCreditProducts == null) {
            noCreditProducts = Boolean.TRUE.equals(jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) = 0 FROM PRODUCTS p " +
                            "JOIN TRANSACTIONS t ON p.ID = t.PRODUCT_ID " +
                            "WHERE p.TYPE = 'CREDIT' AND t.USER_ID = ?",
                    Boolean.class,
                    userId
            ));
            cache.put(cacheKeyNoCredit, noCreditProducts);
        }

        Boolean debitIncomeGreaterThanSpending = cache.getIfPresent(cacheKeyIncomeGreater);
        if (debitIncomeGreaterThanSpending == null) {
            debitIncomeGreaterThanSpending = Boolean.TRUE.equals(jdbcTemplate.queryForObject(
                    "SELECT SUM(CASE WHEN t.AMOUNT > 0 THEN t.AMOUNT ELSE 0 END) > " +
                            "SUM(CASE WHEN t.AMOUNT < 0 THEN ABS(t.AMOUNT) ELSE 0 END) " +
                            "FROM PRODUCTS p JOIN TRANSACTIONS t ON p.ID = t.PRODUCT_ID " +
                            "WHERE p.TYPE = 'DEBIT' AND t.USER_ID = ?",
                    Boolean.class,
                    userId
            ));
            cache.put(cacheKeyIncomeGreater, debitIncomeGreaterThanSpending);
        }

        Boolean debitSpendingGreaterThan100k = cache.getIfPresent(cacheKeySpendingGreater);
        if (debitSpendingGreaterThan100k == null) {
            debitSpendingGreaterThan100k = Boolean.TRUE.equals(jdbcTemplate.queryForObject(
                    "SELECT SUM(CASE WHEN t.AMOUNT < 0 THEN ABS(t.AMOUNT) ELSE 0 END) > 100000 " +
                            "FROM PRODUCTS p JOIN TRANSACTIONS t ON p.ID = t.PRODUCT_ID " +
                            "WHERE p.TYPE = 'DEBIT' AND t.USER_ID = ?",
                    Boolean.class,
                    userId
            ));
            cache.put(cacheKeySpendingGreater, debitSpendingGreaterThan100k);
        }

        if (noCreditProducts || debitIncomeGreaterThanSpending || debitSpendingGreaterThan100k) {
            return Optional.of(List.of(new Recommendation(userId,"Простой кредит", DESCRIPTION)));
        } else {
            return Optional.of(List.of(new Recommendation(userId, "Простой кредит", NO_RECOMMENDATION)));
        }
    }
}