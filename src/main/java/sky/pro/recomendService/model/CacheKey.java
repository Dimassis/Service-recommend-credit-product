package sky.pro.recomendService.model;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class CacheKey {
    private final String queryType;      // Тип запроса (USER_OF, ACTIVE_USER_OF и т.д.)
    private final UUID userId;          // Идентификатор пользователя
    private final List<String> arguments; // Аргументы запроса
    private final int hash;             // Предвычисленный хеш-код

    public CacheKey(String queryType, UUID userId, List<String> arguments) {
        this.queryType = queryType;
        this.userId = userId;
        this.arguments = arguments;
        this.hash = calculateHash();
    }

    private int calculateHash() {
        return Objects.hash(queryType, userId, arguments);
    }

    // Геттеры
    public String getQueryType() {
        return queryType;
    }

    public UUID getUserId() {
        return userId;
    }

    public List<String> getArguments() {
        return arguments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CacheKey cacheKey = (CacheKey) o;
        return Objects.equals(queryType, cacheKey.queryType) &&
                Objects.equals(userId, cacheKey.userId) &&
                Objects.equals(arguments, cacheKey.arguments);
    }

    @Override
    public int hashCode() {
        return hash;
    }

    @Override
    public String toString() {
        return "CacheKey{" +
                "queryType='" + queryType + '\'' +
                ", userId=" + userId +
                ", arguments=" + arguments +
                '}';
    }
}
