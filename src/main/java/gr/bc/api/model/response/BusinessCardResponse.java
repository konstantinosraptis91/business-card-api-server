package gr.bc.api.model.response;

import gr.bc.api.model.BusinessCard;
import gr.bc.api.model.Company;
import gr.bc.api.model.Profession;
import gr.bc.api.model.Template;

/**
 *
 * @author Konstantinos Raptis
 */
public interface BusinessCardResponse {
    
    void setUserFullName(String firstName, String lastName);
    
    String getUserFullName();
    
    void setProfession(Profession p);
    
    Profession getProfession();
    
    void setCompany(Company c);
    
    Company getCompany();
    
    void setTemplate(Template t);
    
    Template getTemplate();
    
    void setBusinessCard(BusinessCard c);
    
    BusinessCard getBusinessCard();
    
}
