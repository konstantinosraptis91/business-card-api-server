package gr.bc.api.model;

import javax.validation.constraints.NotNull;

/**
 *
 * @author Konstantinos Raptis
 */
public class Profession extends DBEntity {
   
    private String name;
            
    public Profession() {
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
        return "Profession{"
                + "id=" + id
                + ", name=" + name 
                + ", lastUpdated=" + lastUpdated
                + ", createdAt=" + createdAt
                + '}';
    }
    
    
       
}
