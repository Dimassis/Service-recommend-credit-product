package sky.pro.recomendService.model;


import java.util.UUID;

public class Recommendation {
    private final UUID id;
    private final String product;
    private final String description;

    public Recommendation(UUID id, String product, String description) {
        this.id = id;
        this.product = product;
        this.description = description;
    }

    public UUID getId() {
        return id;
    }

    public String getProduct() {
        return product;
    }

    public String getDescription() {
        return description;
    }
}
