package gr.bc.api.model.authentication;

/**
 *
 * @author Konstantinos Raptis
 */
public class TokenProperties {

    private long id;
    private String token;

    public TokenProperties() {
    }
    
    /**
     * 
     * @param id User id
     * @param token User token
     */
    public TokenProperties(long id, String token) {
        this.id = id;
        this.token = token;
    }
    
    /**
     * 
     * @return The user id
     */    
    public long getId() {
        return id;
    }

    /**
     * 
     * @param id The user id
     */
    public void setId(long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
