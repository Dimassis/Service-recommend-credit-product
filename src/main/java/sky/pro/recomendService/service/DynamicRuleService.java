package sky.pro.recomendService.service;
import org.springframework.stereotype.Service;
import sky.pro.recomendService.model.DynamicRule;
import sky.pro.recomendService.model.RuleStatistic;
import sky.pro.recomendService.repository.DynamicRuleRepository;
import sky.pro.recomendService.repository.RuleStatisticRepository;

import java.util.List;
import java.util.UUID;

@Service
public class DynamicRuleService {
    private final DynamicRuleRepository repository;
    private final RuleStatisticRepository statisticRepository;

    public DynamicRuleService(DynamicRuleRepository repository, RuleStatisticRepository statisticRepository) {
        this.repository = repository;
        this.statisticRepository = statisticRepository;
    }

    public DynamicRule createRule(DynamicRule rule) {
        // связь RuleCondition и DynamicRule
        if (rule.getConditions() != null) {
            rule.getConditions().forEach(condition -> condition.setDynamicRule(rule));
        }
        DynamicRule savedRule = repository.save(rule);

        // Создаем запись статистики для нового правила
        RuleStatistic statistic = new RuleStatistic();
        statistic.setRuleId(savedRule.getId());
        statisticRepository.save(statistic);

        return savedRule;
    }

    public List<DynamicRule> getAllRules() {
        return repository.findAll();
    }

    public boolean deleteRule(UUID id) {
        if (repository.existsById(id)) {
            // Удаляем статистику при удалении правила
            statisticRepository.deleteByRuleId(id);
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    public void incrementRuleCounter(UUID ruleId) {
        RuleStatistic statistic = statisticRepository.findByRuleId(ruleId);
        if (statistic != null) {
            statistic.setCount(statistic.getCount() + 1);
            statisticRepository.save(statistic);
        }
    }
}
