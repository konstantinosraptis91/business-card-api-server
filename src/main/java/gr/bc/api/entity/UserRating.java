/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.bc.api.entity;

/**
 *
 * @author Konstantinos Raptis
 */
public class UserRating {
    
    private long id;
    private long userId;
    private long businessCardId;
    private int stars;
    private String title;
    private String description;

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

    @Override
    public String toString() {
        return "UserRating{" + "id=" + id + ", userId=" + userId + ", businessCardId=" + businessCardId + ", stars=" + stars + ", title=" + title + ", description=" + description + '}';
    }
        
}
