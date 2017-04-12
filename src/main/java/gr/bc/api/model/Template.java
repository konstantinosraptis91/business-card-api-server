package gr.bc.api.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import javax.validation.constraints.Size;

/**
 *
 * @author Konstantinos Raptis
 */
@JsonPropertyOrder(
        {
            "id",
            "name",
            "primaryColor",
            "secondaryColor",
            "createdAt",
            "lastUpdated"
        })
public class Template extends DBEntity {
        
    private String name;
    @Size(min = 6, max = 6)
    private String primaryColor;
    @Size(min = 6, max = 6)
    private String secondaryColor;
        
    public Template() {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrimaryColor() {
        return primaryColor;
    }

    public void setPrimaryColor(String primaryColor) {
        this.primaryColor = primaryColor;
    }

    public String getSecondaryColor() {
        return secondaryColor;
    }

    public void setSecondaryColor(String secondaryColor) {
        this.secondaryColor = secondaryColor;
    }

    @Override
    public String toString() {
        return "Template{" 
                + "id=" + id
                + ", name=" + name 
                + ", primaryColor=" + primaryColor 
                + ", secondaryColor=" + secondaryColor
                + ", lastUpdated=" + lastUpdated
                + ", createdAt=" + createdAt
                + '}';
    }
    
}
