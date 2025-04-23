package sky.pro.recomendService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sky.pro.recomendService.model.Rules;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RulesRepository extends JpaRepository<Rules, UUID> {
    @Override
    Optional<Rules> findById(UUID uuid);
}
