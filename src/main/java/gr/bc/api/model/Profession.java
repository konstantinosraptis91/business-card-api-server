package gr.bc.api.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 *
 * @author Konstantinos Raptis
 */
@JsonPropertyOrder(
        {
            "id",
            "name",
            "createdAt",
            "lastUpdated"
        })
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
