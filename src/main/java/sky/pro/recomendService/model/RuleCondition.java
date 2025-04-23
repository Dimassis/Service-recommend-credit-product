package sky.pro.recomendService.model;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "rule_conditions")
public class RuleCondition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String query;
    private String arguments; // json строка
    private boolean negate;

    @ManyToOne
    @JoinColumn(name = "dynamic_rule_id")
    @JsonBackReference
    private DynamicRule dynamicRule;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getArguments() {
        return arguments;
    }

    public void setArguments(String arguments) {
        this.arguments = arguments;
    }

    public boolean isNegate() {
        return negate;
    }

    public void setNegate(boolean negate) {
        this.negate = negate;
    }

    public DynamicRule getDynamicRule() {
        return dynamicRule;
    }

    public void setDynamicRule(DynamicRule dynamicRule) {
        this.dynamicRule = dynamicRule;
    }
}
