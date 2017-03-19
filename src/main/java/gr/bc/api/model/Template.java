package gr.bc.api.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Konstantinos Raptis
 */
public class Template {
    
    private long id;
    @NotNull
    private String name;
    @NotNull @Size(min = 6, max = 6)
    private String primaryColor;
    @NotNull @Size(min = 6, max = 6)
    private String secondaryColor;

    public Template() {
    }

    public Template(long id, String name, String primaryColor, String secondaryColor) {
        this.id = id;
        this.name = name;
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
        
    @Override
    public String toString() {
        return "Template{" + "name=" + name + ", primaryColor=" + primaryColor + ", secondaryColor=" + secondaryColor + '}';
    }
           
}
