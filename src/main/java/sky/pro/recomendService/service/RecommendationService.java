package sky.pro.recomendService.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import sky.pro.recomendService.model.CacheKey;
import sky.pro.recomendService.model.Recommendation;
import sky.pro.recomendService.model.RecommendationRule;
import sky.pro.recomendService.model.RuleCondition;
import sky.pro.recomendService.repository.RecommendationRuleRepository;
import sky.pro.recomendService.repository.RecommendationsRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class RecommendationService implements RecommendationRuleSet {

    private final RecommendationsRepository repository;
    private final JdbcTemplate jdbcTemplate;
    private final RecommendationRuleRepository ruleRepository;

    private final Cache<CacheKey, Boolean> userOfCache;
    private final Cache<CacheKey, Boolean> activeUserCache;
    private final Cache<CacheKey, Integer> transactionSumCache;


    public RecommendationService(JdbcTemplate jdbcTemplate,
                                 RecommendationRuleRepository ruleRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.ruleRepository = ruleRepository;

        this.userOfCache = Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.HOURS)
                .maximumSize(10_000)
                .build();

        this.activeUserCache = Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.HOURS)
                .maximumSize(10_000)
                .build();

        this.transactionSumCache = Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.HOURS)
                .maximumSize(10_000)
                .build();
    }

    @Override
    public Optional<List<Recommendation>> getRecommendation(UUID userId) {
        return repository.getListRecommendation(userId);
    }

    public List<Recommendation> getRecommendationsForUser(UUID userId) {
        List<Recommendation> recommendations = new ArrayList<>();

        // Проверка статических правил
        if (isEligibleForInvest500(userId)) {
            recommendations.add(new Recommendation(
                    "Invest 500",
                    UUID.fromString("1476a0f-3b91-413b-ab99-87081d60d5a"),
                    "Описание Invest 500"
            ));
        }

        // Проверка динамических правил
        for (RecommendationRule rule : ruleRepository.findAll()) {
            if (checkRule(userId, rule.getRule())) {
                recommendations.add(new Recommendation(
                        rule.getProductName(),
                        rule.getProductId(),
                        rule.getProductText()
                ));
            }
        }

        return recommendations;
    }

    private boolean checkRule(UUID userId, List<RuleCondition> conditions) {
        return conditions.stream().allMatch(condition -> {
            boolean result = switch (condition.getQuery()) {
                case "USER_OF" -> checkUserOf(userId, condition);
                case "ACTIVE_USER_OF" -> checkActiveUserOf(userId, condition);
                case "TRANSACTION_SUM_COMPARE" -> checkTransactionSumCompare(userId, condition);
                case "TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW" -> checkDepositWithdrawCompare(userId, condition);
                default -> false;
            };
            return condition.isNegate() != result;
        });
    }

    private boolean checkUserOf(UUID userId, RuleCondition condition) {
        CacheKey key = new CacheKey("USER_OF", userId, condition.getArguments());
        return userOfCache.get(key, k -> {
            String productType = condition.getArguments().get(0);
            String sql = "SELECT COUNT(*) FROM TRANSACTIONS t JOIN PRODUCTS p ON t.PRODUCT_ID = p.ID " +
                    "WHERE t.USER_ID = ? AND p.TYPE = ?";
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId, productType);
            return count != null && count > 0;
        });
    }
}
