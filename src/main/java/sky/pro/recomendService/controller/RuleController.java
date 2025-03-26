package sky.pro.recomendService.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sky.pro.recomendService.model.RecommendationRule;
import sky.pro.recomendService.repository.RecommendationRuleRepository;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/rules")
public class RuleController {
    private final RecommendationRuleRepository ruleRepository;

    public RuleController(RecommendationRuleRepository ruleRepository) {
        this.ruleRepository = ruleRepository;
    }

    @PostMapping
    public ResponseEntity<RecommendationRule> createRule(@RequestBody RecommendationRule rule) {
        RecommendationRule saved = ruleRepository.save(rule);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<RecommendationRule>> getAllRules() {
        return ResponseEntity.ok(ruleRepository.findAll());
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteRule(@PathVariable UUID productId) {
        ruleRepository.deleteByProductId(productId);
        return ResponseEntity.noContent().build();
    }
}
