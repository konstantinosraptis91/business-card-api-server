package gr.bc.api.model;

import java.util.Date;
import javax.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author Konstantinos Raptis
 */
public class BusinessCard {
    
    private long id;
    private long professionId;
    private long templateId;
    private long userId;
    @NotNull
    private String title;
    @NotNull
    private String description;
    @NotNull
    private String phoneNumber1;
    private String phoneNumber2;
    private String linkedIn;
    private String website;
    @NotNull
    private boolean universal;
    private String email;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastUpdated;
    
    public BusinessCard() {
    }

    public BusinessCard(long id, long professionId, long templateId, long userId, 
            String title, String description, String phoneNumber1, String phoneNumber2, 
            String linkedIn, String website, boolean universal, String email) {
        this.id = id;
        this.professionId = professionId;
        this.templateId = templateId;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.phoneNumber1 = phoneNumber1;
        this.phoneNumber2 = phoneNumber2;
        this.linkedIn = linkedIn;
        this.website = website;
        this.universal = universal;
        this.email = email;
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

    public long getProfessionId() {
        return professionId;
    }

    public void setProfessionId(long professionId) {
        this.professionId = professionId;
    }

    public long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(long templateId) {
        this.templateId = templateId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
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

    public String getPhoneNumber1() {
        return phoneNumber1;
    }

    public void setPhoneNumber1(String phoneNumber1) {
        this.phoneNumber1 = phoneNumber1;
    }

    public String getPhoneNumber2() {
        return phoneNumber2;
    }

    public void setPhoneNumber2(String phoneNumber2) {
        this.phoneNumber2 = phoneNumber2;
    }

    public String getLinkedIn() {
        return linkedIn;
    }

    public void setLinkedIn(String linkedIn) {
        this.linkedIn = linkedIn;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public boolean isUniversal() {
        return universal;
    }

    public void setUniversal(boolean universal) {
        this.universal = universal;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
        return "BusinessCard{" + "id=" + id + ", professionId=" + professionId + ", templateId=" + templateId + ", userId=" + userId + ", title=" + title + ", description=" + description + ", phoneNumber1=" + phoneNumber1 + ", phoneNumber2=" + phoneNumber2 + ", linkedIn=" + linkedIn + ", website=" + website + ", universal=" + universal + ", email=" + email + ", createdAt=" + createdAt + ", lastUpdated=" + lastUpdated + '}';
    }
        
}
