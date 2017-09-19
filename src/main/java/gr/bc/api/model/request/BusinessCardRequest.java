package gr.bc.api.model.request;

import gr.bc.api.model.BusinessCard;

/**
 *
 * @author Konstantinos Raptis
 */
public interface BusinessCardRequest {
    
    void setProfessionName(String name);
    
    String getProfessionName();
    
    void setCompanyName(String name);
    
    String getCompanyName();
    
    void setBusinessCard(BusinessCard c);
    
    BusinessCard getBusinessCard();
    
}
