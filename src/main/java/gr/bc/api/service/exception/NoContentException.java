package gr.bc.api.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author Konstantinos Raptis
 */
public class NoContentException extends ServiceException {

    public NoContentException(String message) {
        super(message);
    }

    @Override
    public ResponseEntity<?> getResponse() {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
}
