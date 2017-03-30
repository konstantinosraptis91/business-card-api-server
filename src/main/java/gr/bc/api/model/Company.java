package gr.bc.api.model;

/**
 *
 * @author Konstantinos Raptis
 */
public class Company extends DBEntity {

    private String name;

    public Company() {
        super();
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Company{"
                + "id=" + id
                + ", name=" + name
                + ", lastUpdated=" + lastUpdated
                + ", createdAt=" + createdAt
                + '}';
    }

}
