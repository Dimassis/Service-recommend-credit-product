package sky.pro.recomendService.model;

import jakarta.persistence.Embeddable;

import java.util.List;
import java.util.Objects;

@Embeddable
public class RuleCondition {
    private String query;
    private List<String> arguments;
    private boolean negate;

    // Конструкторы
    public RuleCondition() {
    }

    public RuleCondition(String query, List<String> arguments, boolean negate) {
        this.query = query;
        this.arguments = arguments;
        this.negate = negate;
    }

    // Геттеры и сеттеры
    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public List<String> getArguments() {
        return arguments;
    }

    public void setArguments(List<String> arguments) {
        this.arguments = arguments;
    }

    public boolean isNegate() {
        return negate;
    }

    public void setNegate(boolean negate) {
        this.negate = negate;
    }

    // equals и hashCode для корректной работы с коллекциями
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RuleCondition that = (RuleCondition) o;
        return negate == that.negate &&
                Objects.equals(query, that.query) &&
                Objects.equals(arguments, that.arguments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(query, arguments, negate);
    }

    @Override
    public String toString() {
        return "RuleCondition{" +
                "query='" + query + '\'' +
                ", arguments=" + arguments +
                ", negate=" + negate +
                '}';
    }
}
