package gr.bc.api.util;

import java.security.SecureRandom;

/**
 *
 * @author Konstantinos Raptis
 */
public class TokenUtils {
        
    private final String VOCABULARY = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private final SecureRandom srnd;
    public static final int TOKEN_SIZE = 64;
    
    public TokenUtils() {
        srnd = new SecureRandom();
    }
    
    public String createToken(int size) {
        
        StringBuilder builder = new StringBuilder(size);
        
        for (int i = 0; i < size; i++) {
            builder.append(VOCABULARY.charAt(srnd.nextInt(VOCABULARY.length())));
        }
        
        return builder.toString();
    }
    
}
