package gr.bc.api.model;

import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.Email;

/**
 *
 * @author Konstantinos Raptis
 */
public class User extends DBEntity {
    
    @Size(min = 1, max = 254) @Email
    private String email;
    @Size(min = 1, max = 15)
    private String password;
    @Size(min = 1, max = 30)
    private String firstName;
    @Size(min = 1, max = 30)
    private String lastName;
    private String token;
    // Profile Image File Name
    private String fileName;
        
    public User() {
        super();
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return "User{"
                + "id=" + id
                + ", email=" + email 
                + ", password=" + password 
                + ", firstName=" + firstName 
                + ", lastName=" + lastName 
                + ", token=" + token 
                + ", fileName=" + fileName
                + ", lastUpdated=" + lastUpdated
                + ", createdAt=" + createdAt
                + '}';
    }
          
}
