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
public class User {
    
    private long id;
    private long businessCardId;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
        
    public User() {
        
    }

    public User(long id, String email, String password, String firstName, String lastName) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
        
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public long getBusinessCardId() {
        return businessCardId;
    }

    public void setBusinessCardId(long businessCardId) {
        this.businessCardId = businessCardId;
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", businessCardId=" + businessCardId + ", email=" + email + ", password=" + password + ", firstName=" + firstName + ", lastName=" + lastName + '}';
    }
          
}
