package gr.bc.api.model;

import java.util.Date;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

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
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastUpdated;
    
    public Template() {
    }

    public Template(long id, String name, String primaryColor, String secondaryColor) {
        this.id = id;
        this.name = name;
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
    }

    public void init() {
        this.createdAt = new Date();
        this.lastUpdated = this.createdAt;
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public String toString() {
        return "Template{" + "id=" + id + ", name=" + name + ", primaryColor=" + primaryColor + ", secondaryColor=" + secondaryColor + ", createdAt=" + createdAt + ", lastUpdated=" + lastUpdated + '}';
    }
           
}
