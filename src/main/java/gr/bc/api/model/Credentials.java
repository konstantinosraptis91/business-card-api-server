package gr.bc.api.model;

import java.util.StringTokenizer;
import org.apache.tomcat.util.codec.binary.Base64;

/**
 *
 * @author Konstantinos Raptis
 */
public class Credentials {
    
    public static final String AUTHORIZATION_HEADER_PREFIX = "Basic";
    
    private String username;
    private String password;

    public Credentials() {
        super();
    }

    public Credentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public static Credentials getCredentials(String authToken) {
        authToken = authToken.replaceFirst(AUTHORIZATION_HEADER_PREFIX, "");
        String decodedAuthToken = new String(Base64.decodeBase64(authToken));
        StringTokenizer tokenizer = new StringTokenizer(decodedAuthToken, ":");
        Credentials cr = new Credentials();
        cr.setUsername(tokenizer.nextToken());
        cr.setPassword(tokenizer.nextToken());
        return cr;
    }
        
    @Override
    public String toString() {
        return "Credentials{" + "username=" + username + ", password=" + password + '}';
    }
        
}
