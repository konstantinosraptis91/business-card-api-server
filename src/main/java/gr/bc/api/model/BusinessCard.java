package gr.bc.api.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.hibernate.validator.constraints.Email;

/**
 *
 * @author Konstantinos Raptis
 */
@JsonPropertyOrder(
        {
            "id",
            "userId",
            "templateId",
            "professionId",
            "companyId",
            "email1",
            "email2",
            "phoneNumber1",
            "phoneNumber2",
            "linkedIn",
            "website",
            "universal",
            "address1",
            "address2",
            "lastUpdated",
            "createdAt"
        })
public class BusinessCard extends DBEntity {
      
    private long userId;
    private long templateId;
    private long professionId;
    private long companyId;
    @Email
    private String email1;
    private String email2;
    private String phoneNumber1;
    private String phoneNumber2;
    private String linkedIn;
    private String website;
    private Boolean universal;
    private String address1;
    private String address2;
        
    public BusinessCard() {
        super();
    }
   
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(long templateId) {
        this.templateId = templateId;
    }

    public long getProfessionId() {
        return professionId;
    }

    public void setProfessionId(long professionId) {
        this.professionId = professionId;
    }

    public long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }

    public String getEmail1() {
        return email1;
    }

    public void setEmail1(String email1) {
        this.email1 = email1;
    }

    public String getEmail2() {
        return email2;
    }

    public void setEmail2(String email2) {
        this.email2 = email2;
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

    public Boolean isUniversal() {
        return universal;
    }

    public void setUniversal(Boolean universal) {
        this.universal = universal;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    @Override
    public String toString() {
        return "BusinessCard{"
                + "id=" + id
                + ", userId=" + userId 
                + ", templateId=" + templateId 
                + ", professionId=" + professionId 
                + ", companyId=" + companyId 
                + ", email1=" + email1 
                + ", email2=" + email2 
                + ", phoneNumber1=" + phoneNumber1 
                + ", phoneNumber2=" + phoneNumber2 
                + ", linkedIn=" + linkedIn 
                + ", website=" + website 
                + ", universal=" + universal 
                + ", address1=" + address1 
                + ", address2=" + address2 
                + ", lastUpdated=" + lastUpdated
                + ", createdAt=" + createdAt
                + '}';
    }
        
}
