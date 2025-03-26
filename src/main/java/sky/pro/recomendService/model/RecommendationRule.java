package sky.pro.recomendService.model;

import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
public class RecommendationRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String productName;
    private UUID productId;
    private String productText;
    @Column(columnDefinition = "jsonb")
    @Convert(converter = RuleListConverter.class)
    private List<RuleCondition> rule;

    public RecommendationRule(Long id, String productName, UUID productId, String productText, List<RuleCondition> rule) {
        this.id = id;
        this.productName = productName;
        this.productId = productId;
        this.productText = productText;
        this.rule = rule;
    }

    public RecommendationRule() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public String getProductText() {
        return productText;
    }

    public void setProductText(String productText) {
        this.productText = productText;
    }

    public List<RuleCondition> getRule() {
        return rule;
    }

    public void setRule(List<RuleCondition> rule) {
        this.rule = rule;
    }
}
