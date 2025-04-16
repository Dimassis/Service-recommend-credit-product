package sky.pro.recomendService.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sky.pro.recomendService.model.DynamicRule;
import sky.pro.recomendService.service.DynamicRuleService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/rule")
public class DynamicRuleController {
    private final DynamicRuleService service;

    public DynamicRuleController(DynamicRuleService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<DynamicRule> addRule(@RequestBody DynamicRule rule) {
        DynamicRule savedRule = service.createRule(rule);
        return ResponseEntity.ok(savedRule);
    }

    @GetMapping("/getRules")
    public List<DynamicRule> getRules() {
        List<DynamicRule> getRules = service.getAllRules();
        return getRules;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRule(@PathVariable UUID id) {
        boolean isDeleted = service.deleteRule(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
