package gr.bc.api.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;
import gr.bc.api.model.BusinessCard;

/**
 *
 * @author Konstantinos Raptis
 */
@JsonPropertyOrder(
        {
            "professionName",
            "companyName",
            "businessCard"
        })
public class BusinessCardRequestImpl implements BusinessCardRequest {
    
    private String professionName;
    private String companyName;
    private BusinessCard c;
       
    @Override
    @JsonSetter("professionName")
    public void setProfessionName(String name) {
        this.professionName = name;
    }

    @Override
    @JsonProperty("professionName")
    public String getProfessionName() {
        return professionName;
    }

    @Override
    @JsonSetter("companyName")
    public void setCompanyName(String name) {
        this.companyName = name;
    }

    @Override
    @JsonProperty("companyName")
    public String getCompanyName() {
        return companyName;
    }

    @Override
    @JsonSetter("businessCard")
    public void setBusinessCard(BusinessCard c) {
        this.c = c;
    }

    @Override
    @JsonProperty("businessCard")
    public BusinessCard getBusinessCard() {
        return c;
    }
    
}
