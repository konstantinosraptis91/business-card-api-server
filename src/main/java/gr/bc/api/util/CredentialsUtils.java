package gr.bc.api.util;

import gr.bc.api.model.authentication.Credentials;
import java.util.StringTokenizer;
import org.apache.tomcat.util.codec.binary.Base64;

/**
 *
 * @author Konstantinos Raptis
 */
public class CredentialsUtils {
    
    public static final String AUTHORIZATION_HEADER_PREFIX = "Basic";
    
    public static Credentials extractCredentials(String authToken) {
        authToken = authToken.replaceFirst(AUTHORIZATION_HEADER_PREFIX, "");
        String decodedAuthToken = new String(Base64.decodeBase64(authToken));
        StringTokenizer tokenizer = new StringTokenizer(decodedAuthToken, ":");
        Credentials cr = new Credentials();
        cr.setUsername(tokenizer.nextToken());
        cr.setPassword(tokenizer.nextToken());
        return cr;
    }
    
}
