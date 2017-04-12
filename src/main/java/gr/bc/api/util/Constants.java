package gr.bc.api.util;

import java.text.SimpleDateFormat;

/**
 *
 * @author Konstantinos Raptis
 */
public class Constants {
    
    public static final String AUTHORIZATION_HEADER_KEY = "Authorization";
    public static final String AUTHORIZATION_HEADER_PREFIX = "Basic";
   
    public static final SimpleDateFormat LOG_DATE_FORMAT = new SimpleDateFormat("HH:mm:ss");
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
   
    public static final String DEFAULT_IMAGE_PATH = "default_image";
}
