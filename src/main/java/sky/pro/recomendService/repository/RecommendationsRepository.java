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
    private final JdbcTemplate jdbcTemplate;

    public RecommendationsRepository(@Qualifier("recommendationsJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<List<Recommendation>> getListRecommendation(UUID userId) {
        List<Recommendation> recommendations = new ArrayList<>();
        if (recommendTopSaving(userId)) {
            Path pathTopSaving = Paths.get("src/main/resources/recommendTopSavingDescription.txt");
            String description;
            try {
                description = Files.readString(pathTopSaving);
            } catch (IOException e) {
                throw new RuntimeException();
            }
            recommendations.add(new Recommendation(userId, "Top Saving", description));

        }

        if (recommendInvest500(userId)) {
            Path pathTopSaving = Paths.get("src/main/resources/recommendInvest500.txt");
            String description;
            try {
                description = Files.readString(pathTopSaving);
            } catch (IOException e) {
                throw new RuntimeException();
            }
            recommendations.add(new Recommendation(userId, "Invest 500", description));
        }

        if (recommendJustCredit(userId)) {
            //
        }

        if (!recommendations.isEmpty()) {
            return Optional.of(recommendations);
        }

        return Optional.of(List.of(new Recommendation(userId, "No Recommendation", "Нет рекомендаций")));
    }


    public boolean recommendTopSaving(UUID userId) {
        String sql1 = loadQuery("sql/TopSaving/sqlRequest1.sql");
        String sql2 = loadQuery("sql/TopSaving/sqlRequest2.sql");
        String sql3 = loadQuery("sql/TopSaving/sqlRequest3.sql");

        Boolean exists1 = Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql1, Boolean.class, userId));  //condition 1

        Map<String, Object> result2 = jdbcTemplate.queryForMap(sql2,userId);
        Boolean debitCondition = (Boolean) result2.get("debit_condition");
        Boolean savingCondition = (Boolean) result2.get("saving_condition");  //condition 2

        Boolean exists3 = jdbcTemplate.queryForObject(sql3, Boolean.class, userId);  //condition 3
        return Boolean.TRUE.equals(exists1) || Boolean.TRUE.equals(debitCondition) || Boolean.TRUE.equals(savingCondition) || Boolean.TRUE.equals(exists3);
    }


    public boolean recommendInvest500(UUID userId) {
        return false;
    }

    public boolean recommendJustCredit(UUID userId) {
        return false;
    }

    private String loadQuery(String path) {
        try {
            Resource resource = new ClassPathResource(path);
            byte[] bytes = FileCopyUtils.copyToByteArray(resource.getInputStream());
            return new String(bytes, StandardCharsets.UTF_8).trim();
        } catch (Exception e) {
            throw new RuntimeException("Ошибка загрузки SQL-запроса: " + path, e);
        }
    }
}