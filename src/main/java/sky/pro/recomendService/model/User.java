package sky.pro.recomendService.model;


public class User {
    private Long id;
    private String name;
    private List<Product> usedProducts;
    private List<Transaction> transactions;

    public User() {
    }

    public User(Long id, String name, List<Product> usedProducts, List<Transaction> transactions) {
        this.id = id;
        this.name = name;
        this.usedProducts = usedProducts;
        this.transactions = transactions;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", usedProducts=" + usedProducts +
                ", transactions=" + transactions +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Product> getUsedProducts() {
        return usedProducts;
    }

    public void setUsedProducts(List<Product> usedProducts) {
        this.usedProducts = usedProducts;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
}
