package sky.pro.recomendService.service;


import jakarta.persistence.EntityNotFoundException;
import org.hibernate.StaleObjectStateException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sky.pro.recomendService.model.Rules;
import sky.pro.recomendService.repository.RulesRepository;

import java.util.UUID;

@Service
public class RulesService {
    private RulesRepository rulesRepository;

    public RulesService(RulesRepository rulesRepository) {
        this.rulesRepository = rulesRepository;
    }

    @Transactional
    public Rules createRules() {
        UUID id = UUID.randomUUID();
        String productName = "Super Product";
        UUID productId = UUID.randomUUID();
        String productText = "This is a fantastic product.";
        String rules = "Recommended for everyone.";

        // Создание нового объекта Rules
        Rules rule = new Rules(id, productName, productId, productText, rules);

        System.out.println(rule);

        // Сохранение объекта в репозитории
        try {
            rulesRepository.save(rule);  // Исправлено на использование объекта rule
        } catch (StaleObjectStateException e) {
            // Логирование и перезагрузка данных перед сохранением
            Rules refreshedRules = rulesRepository.findById(rule.getId())  // Используем rule.getId()
                    .orElseThrow(() -> new EntityNotFoundException("Rules not found"));
            // Обновить данные и сохранить снова, если необходимо
            // Например, можно скопировать данные из refreshedRules в rule и повторить сохранение
            rule = refreshedRules;
            rulesRepository.save(rule);
        }
        return rule;
    }
}