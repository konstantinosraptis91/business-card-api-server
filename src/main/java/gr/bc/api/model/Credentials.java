/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.bc.api.model;

import gr.bc.api.util.Constants;
import java.util.StringTokenizer;
import org.apache.tomcat.util.codec.binary.Base64;

/**
 *
 * @author Konstantinos Raptis
 */
public class Credentials {
    
    private String username;
    private String password;

    public Credentials() {
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
        authToken = authToken.replaceFirst(Constants.AUTHORIZATION_HEADER_PREFIX, "");
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
