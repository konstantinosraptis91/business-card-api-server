/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.bc.api.entity;

import javax.validation.constraints.NotNull;

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
    private boolean isPublic;

    public BusinessCard() {
    }

    public BusinessCard(long id, long professionId, long templateId, long userId, String title, String description, String phoneNumber1, String phoneNumber2, String linkedIn, String website, boolean isPublic) {
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
        this.isPublic = isPublic;
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

    public boolean isPublic() {
        return isPublic;
    }

    public void setIsPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    @Override
    public String toString() {
        return "BusinessCard{" + "professionId=" + professionId + ", templateId=" + templateId + ", userId=" + userId + ", title=" + title + ", description=" + description + ", phoneNumber1=" + phoneNumber1 + ", phoneNumber2=" + phoneNumber2 + ", linkedIn=" + linkedIn + ", website=" + website + ", isPublic=" + isPublic + '}';
    }
        
}
