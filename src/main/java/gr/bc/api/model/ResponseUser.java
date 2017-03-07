/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.bc.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 * @author Konstantinos Raptis
 */
public class ResponseUser extends User {

    public ResponseUser() {
        super();
    }
    
    @Override
    @JsonIgnore
    public String getEmail() {
        return super.getEmail();
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        return super.getPassword(); 
    }

    @Override
    @JsonIgnore
    public long getBusinessCardId() {
        return super.getBusinessCardId();
    }
    
}
