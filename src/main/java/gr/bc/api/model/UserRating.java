package gr.bc.api.model;

import java.util.Date;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author Konstantinos Raptis
 */
public class UserRating {
    
    private long id;
    private long userId;
    private long businessCardId;
    @NotNull @Min(1) @Max(5)
    private int stars;
    private String title;
    private String description;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastUpdated;
    
    public UserRating() {
    }

    public UserRating(long id, long userId, long businessCardId, int stars, String title, String description) {
        this.id = id;
        this.userId = userId;
        this.businessCardId = businessCardId;
        this.stars = stars;
        this.title = title;
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

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getBusinessCardId() {
        return businessCardId;
    }

    public void setBusinessCardId(long businessCardId) {
        this.businessCardId = businessCardId;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
        return "UserRating{" + "id=" + id + ", userId=" + userId + ", businessCardId=" + businessCardId + ", stars=" + stars + ", title=" + title + ", description=" + description + ", createdAt=" + createdAt + ", lastUpdated=" + lastUpdated + '}';
    }
        
}
