package sky.pro.recomendService.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import sky.pro.recomendService.model.DynamicRule;

import java.util.UUID;

public interface DynamicRuleRepository extends JpaRepository<DynamicRule, UUID>{
<<<<<<< HEAD
=======
    boolean existsById(Long id);

    void deleteById(Long id);
>>>>>>> feature-3
}
