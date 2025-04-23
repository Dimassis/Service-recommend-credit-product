package sky.pro.recomendService.model;


import java.util.UUID;

public class Recommendation {
    private final UUID id;
    private final String name;
    private final String description;

    public Recommendation(UUID id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public UUID getId() {
        return id;
    }
}
