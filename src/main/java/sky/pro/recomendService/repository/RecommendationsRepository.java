package sky.pro.recomendService.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.FileCopyUtils;
import sky.pro.recomendService.model.Recommendation;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public class RecommendationsRepository {
   /* public Optional<List<Recommendation>> getListRecommendation(Recommendation recommendation) {
        List<Recommendation> recommendations = new ArrayList<>();
        recommendations.add(recommendation);
        return Optional.of(recommendations);
    }*/
}