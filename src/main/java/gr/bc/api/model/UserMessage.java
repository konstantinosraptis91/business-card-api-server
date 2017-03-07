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
public class UserMessage extends User {

    private String message;
    
    public UserMessage() {
        super();
    }
    
    public UserMessage(String message) {
        this();
        this.message = message;
    }
    
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    @JsonIgnore
    public long getBusinessCardId() {
        return super.getBusinessCardId(); 
    }

    @Override
    @JsonIgnore
    public String getLastName() {
        return super.getLastName(); 
    }

    @Override
    @JsonIgnore
    public String getFirstName() {
        return super.getFirstName(); 
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        return super.getPassword(); 
    }

    @Override
    @JsonIgnore
    public String getEmail() {
        return super.getEmail(); 
    }

    @Override
    @JsonIgnore
    public long getId() {
        return super.getId(); 
    }
    
}
