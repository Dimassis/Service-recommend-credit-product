package sky.pro.recomendService.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sky.pro.recomendService.model.Recommendation;
import sky.pro.recomendService.service.RecommendationService;

import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/recommendation")
public class RecommendationController {
    private final RecommendationService service;

    public RecommendationController(RecommendationService service) {
        this.service = service;
    }


    @GetMapping("/{id}")
    public Optional<Recommendation> getRecommendationById(@PathVariable() UUID id) {
        return service.getRecommendation(id);
    }
}
