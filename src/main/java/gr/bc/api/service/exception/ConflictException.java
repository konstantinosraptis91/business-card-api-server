package gr.bc.api.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author Konstantinos Raptis
 */
public class ConflictException extends ServiceException {
    
    public ConflictException(String message) {
        super(message);
    }

    @Override
    public ResponseEntity<?> getResponse() {
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }
    
}
