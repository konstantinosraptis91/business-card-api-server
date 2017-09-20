package gr.bc.api.model.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;
import gr.bc.api.model.BusinessCard;
import gr.bc.api.model.Company;
import gr.bc.api.model.Profession;
import gr.bc.api.model.Template;

/**
 *
 * @author Konstantinos Raptis
 */
@JsonPropertyOrder(
        {
            "userFullName",
            "profession",
            "company",
            "template",
            "businessCard"
        })
public class BusinessCardResponseImpl implements BusinessCardResponse {

    private String userFullName;
    private Profession p;
    private Company c;
    private Template t;
    private BusinessCard bc;

    @Override
    @JsonSetter("userFullName")
    public void setUserFullName(String firstName, String lastName) {
        this.userFullName = firstName + " " + lastName;
    }

    @Override
    public String getUserFullName() {
        return userFullName;
    }

    @Override
    @JsonSetter("profession")
    public void setProfession(Profession p) {
        this.p = p;
    }

    @Override
    public Profession getProfession() {
        return p;
    }

    @Override
    @JsonSetter("company")
    public void setCompany(Company c) {
        this.c = c;
    }

    @Override
    public Company getCompany() {
        return c;
    }

    @Override
    @JsonSetter("template")
    public void setTemplate(Template t) {
        this.t = t;
    }

    @Override
    public Template getTemplate() {
        return t;
    }

    @Override
    @JsonSetter("businessCard")
    public void setBusinessCard(BusinessCard bc) {
        this.bc = bc;
    }

    @Override
    public BusinessCard getBusinessCard() {
        return bc;
    }

    @Override
    public String toString() {
        return "BusinessCardResponseImpl{" + "userFullName=" + userFullName + ", p=" + p + ", c=" + c + ", t=" + t + ", bc=" + bc + '}';
    }
    
}
