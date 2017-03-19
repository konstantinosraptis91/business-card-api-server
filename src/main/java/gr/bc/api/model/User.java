package gr.bc.api.model;

import gr.bc.api.util.Constants;
import gr.bc.api.util.TokenUtils;
import java.util.Date;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.Email;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author Konstantinos Raptis
 */
public class User {
   
    private long id;
    private long businessCardId;
    @NotNull @Size(min = 1, max = 254) @Email
    private String email;
    @NotNull @Size(min = 1, max = 15)
    private String password;
    @NotNull @Size(min = 1, max = 30)
    private String firstName;
    @NotNull @Size(min = 1, max = 30)
    private String lastName;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;
    private String token;
    private Date tokenLastUpdated;
    // Profile Image File Name
    private String fileName;
    
    public User() {
        
    }
        
    public User(long id, String email, String password, String firstName, String lastName, String fileName) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.createdAt = new Date();
        this.token = new TokenUtils().createToken(TokenUtils.TOKEN_SIZE);
        this.tokenLastUpdated = new Date();
        this.fileName = fileName;
    }

    public void init() {
        createdAt = new Date();
        tokenLastUpdated = createdAt;
        token = new TokenUtils().createToken(TokenUtils.TOKEN_SIZE);
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getTokenLastUpdated() {
        return tokenLastUpdated;
    }

    public void setTokenLastUpdated(Date tokenLastUpdated) {
        this.tokenLastUpdated = tokenLastUpdated;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    @Override
    public String toString() {
        return "User{" + "email=" + email + ", firstName=" + firstName + ", lastName=" + lastName + '}';
    }
          
}
