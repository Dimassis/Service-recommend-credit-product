package sky.pro.recomendService.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sky.pro.recomendService.model.Recommendation;
import sky.pro.recomendService.service.UserService;

@RestController
@RequestMapping("/recommendation")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Recommendation> getRecommendations(@PathVariable long id) {
        return null;
    }
}
