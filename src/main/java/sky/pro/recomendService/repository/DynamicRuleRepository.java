package sky.pro.recomendService.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import sky.pro.recomendService.model.DynamicRule;

import java.util.UUID;

public interface DynamicRuleRepository extends JpaRepository<DynamicRule, UUID>{
    boolean existsById(UUID id);

    void deleteById(UUID id);
}
