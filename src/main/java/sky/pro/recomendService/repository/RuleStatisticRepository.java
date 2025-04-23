package sky.pro.recomendService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import sky.pro.recomendService.model.RuleStatistic;

import java.util.UUID;

public interface RuleStatisticRepository extends JpaRepository<RuleStatistic, UUID> {
    RuleStatistic findByRuleId(UUID ruleId);

    @Transactional
    @Modifying
    @Query("DELETE FROM RuleStatistic rs WHERE rs.ruleId = :ruleId")
    void deleteByRuleId(UUID ruleId);
}