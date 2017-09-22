package gr.bc.api.service.exception;

import org.springframework.http.ResponseEntity;

/**
 *
 * @author Konstantinos Raptis
 */
public abstract class ServiceException extends Exception {
        
    public ServiceException(String message) {
        super(message);
    }
    
    public abstract ResponseEntity<?> getResponse();
    
}
