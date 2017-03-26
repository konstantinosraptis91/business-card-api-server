package gr.bc.api.model;

import java.util.Date;
import javax.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author Konstantinos Raptis
 */
public class Profession {
    
    private long id;
    @NotNull
    private String name;
    private String description;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastUpdated;
    
    public Profession() {
    }

    public Profession(long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        return "Profession{" + "id=" + id + ", name=" + name + ", description=" + description + ", createdAt=" + createdAt + ", lastUpdated=" + lastUpdated + '}';
    }
       
}
