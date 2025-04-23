package sky.pro.recomendService.service;

import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import sky.pro.recomendService.model.Recommendation;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Component
public class RecommendationTopSaving implements RecommendationRuleSet {
    private final JdbcTemplate jdbcTemplate;
    private final Cache<String, Boolean> cache;
    private final String DESCRIPTION = "Откройте свою собственную «Копилку» с нашим банком! «Копилка» — это уникальный банковский инструмент, который поможет вам легко и удобно накапливать деньги на важные цели";
    private final String NO_RECOMMENDATION = "No recommendation";
    private final DynamicRuleService dynamicRuleService;
    private final String ruleId = "TOP_SAVING_RULE";

    public RecommendationTopSaving(JdbcTemplate jdbcTemplate, Cache<String, Boolean> cache, DynamicRuleService dynamicRuleService) {
        this.jdbcTemplate = jdbcTemplate;
        this.cache = cache;
        this.dynamicRuleService = dynamicRuleService;
    }

    @Override
    public Optional<List<Recommendation>> getRecommendation(UUID userId) {
        // Ключи кэша
        String cacheKeyHasDebit = userId + "_HAS_DEBIT_TRANSACTIONS";
        String cacheKeyIncomeOver50k = userId + "_INCOME_OVER_50K_DEBIT_OR_SAVING";
        String cacheKeyIncomeMoreThanSpending = userId + "_DEBIT_INCOME_MORE_THAN_SPENDING";

        // Проверка наличия дебетовых транзакций
        Boolean hasDebit = cache.getIfPresent(cacheKeyHasDebit);
        if (hasDebit == null) {
            hasDebit = Boolean.TRUE.equals(jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) > 0 " +
                            "FROM PRODUCTS p " +
                            "JOIN TRANSACTIONS t ON p.ID = t.PRODUCT_ID " +
                            "WHERE p.TYPE = 'DEBIT' AND t.USER_ID = ?",
                    Boolean.class,
                    userId
            ));
            cache.put(cacheKeyHasDebit, hasDebit);
        }

        // Суммы по дебету и сбережениям >= 50000
        Boolean incomeOver50k = cache.getIfPresent(cacheKeyIncomeOver50k);
        if (incomeOver50k == null) {
            Map<String, Object> result = jdbcTemplate.queryForMap(
                    "SELECT " +
                            "(SUM(CASE WHEN p.TYPE = 'DEBIT' THEN t.AMOUNT ELSE 0 END) >= 50000) AS debit_condition, " +
                            "(SUM(CASE WHEN p.TYPE = 'SAVING' THEN t.AMOUNT ELSE 0 END) >= 50000) AS saving_condition " +
                            "FROM PRODUCTS p " +
                            "JOIN TRANSACTIONS t ON p.ID = t.PRODUCT_ID " +
                            "WHERE t.AMOUNT > 0 AND t.USER_ID = ?",
                    userId
            );
            Boolean debitCondition = (Boolean) result.get("debit_condition");
            Boolean savingCondition = (Boolean) result.get("saving_condition");

            incomeOver50k = (debitCondition != null && debitCondition) ||
                    (savingCondition != null && savingCondition);
            cache.put(cacheKeyIncomeOver50k, incomeOver50k);
        }

        // Доходы > расходы по дебетовым транзакциям
        Boolean incomeMoreThanSpending = cache.getIfPresent(cacheKeyIncomeMoreThanSpending);
        if (incomeMoreThanSpending == null) {
            incomeMoreThanSpending = Boolean.TRUE.equals(jdbcTemplate.queryForObject(
                    "SELECT SUM(CASE WHEN t.AMOUNT > 0 THEN t.AMOUNT ELSE 0 END) > " +
                            "SUM(CASE WHEN t.AMOUNT < 0 THEN ABS(t.AMOUNT) ELSE 0 END) " +
                            "FROM PRODUCTS p " +
                            "JOIN TRANSACTIONS t ON p.ID = t.PRODUCT_ID " +
                            "WHERE p.TYPE = 'DEBIT' AND t.USER_ID = ?",
                    Boolean.class,
                    userId
            ));
            cache.put(cacheKeyIncomeMoreThanSpending, incomeMoreThanSpending);
        }
        // Если хоть одно условие выполнено то даём рекомендацию
        if (hasDebit || incomeOver50k || incomeMoreThanSpending) {
            // Увеличиваем счетчик срабатываний для соответствующих правил
            dynamicRuleService.incrementRuleCounter(UUID.fromString(ruleId));
            return Optional.of(List.of(new Recommendation(userId, "Top saving", DESCRIPTION)));
        } else {
            return Optional.of(List.of(new Recommendation(userId, "Top saving", NO_RECOMMENDATION)));
        }
    }
}
