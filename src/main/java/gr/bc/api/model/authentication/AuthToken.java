package gr.bc.api.model.authentication;

/**
 *
 * @author Konstantinos Raptis
 */
public class AuthToken {
    
    private long userId;
    private String token;

    public AuthToken() {
    }
    
    public AuthToken(long userId, String token) {
        this.userId = userId;
        this.token = token;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "AuthToken{" + "userId=" + userId + ", token=" + token + '}';
    }
    
}
