package sky.pro.recomendService.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table(name = "TRANSACTIONS")
public class Transactions {

    @Id
    @Column("ID")
    private UUID id;

    @Column("PRODUCT_ID")
    private UUID productId;

    @Column("USER_ID")
    private UUID userId;

    @Column("TYPE")
    private String type;

    @Column("AMOUNT")
    private int amount;

    public Transactions() {
    }

    public Transactions(UUID id, UUID productId, UUID userId, String type, int amount) {
        this.id = id;
        this.productId = productId;
        this.userId = userId;
        this.type = type;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Transactions{" +
                "id=" + id +
                ", productId=" + productId +
                ", userId=" + userId +
                ", type='" + type + '\'' +
                ", amount=" + amount +
                '}';
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
