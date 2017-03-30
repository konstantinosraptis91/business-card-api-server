package gr.bc.api.util;

/**
 *
 * @author Konstantinos Raptis
 */
public class MySQLHelper {
    
    // tables
    public static final String USER_TABLE = "user";
    public static final String BUSINESS_CARD_TABLE = "business_card";
    public static final String TEMPLATE_TABLE = "template";
    public static final String PROFESSION_TABLE = "profession";
    public static final String COMPANY_TABLE = "company";
    public static final String USER_BUSINESS_CARD_TABLE = USER_TABLE + "_" + BUSINESS_CARD_TABLE;
    
    // user table attributes
    public static final String USER_ID = "id";
    public static final String USER_EMAIL = "email";
    public static final String USER_PASSWORD = "password";
    public static final String USER_FIRSTNAME = "firstname";
    public static final String USER_LASTNAME = "lastname";
    public static final String USER_TOKEN = "token";
    public static final String USER_IMAGE_PATH = "image_filename";
    public static final String USER_LAST_UPDATED = "last_updated";
    public static final String USER_CREATED_AT = "created_at";
    
    // business card table attributes
    public static final String BUSINESS_CARD_ID = "id";
    public static final String BUSINESS_CARD_EMAIL_1 = "email_1";
    public static final String BUSINESS_CARD_EMAIL_2 = "email_2";
    public static final String BUSINESS_CARD_PHONE_NUMBER1 = "phone_number_1";
    public static final String BUSINESS_CARD_PHONE_NUMBER2 = "phone_number_2";
    public static final String BUSINESS_CARD_LINKEDIN = "linked_in";
    public static final String BUSINESS_CARD_WEBSITE = "website";
    public static final String BUSINESS_CARD_UNIVERSAL = "universal";
    public static final String BUSINESS_CARD_ADDRESS_1 = "address_1";    
    public static final String BUSINESS_CARD_ADDRESS_2 = "address_2";    
    public static final String BUSINESS_CARD_LAST_UPDATED = "last_updated";
    public static final String BUSINESS_CARD_CREATED_AT = "created_at";
    
    // template table attributes
    public static final String TEMPLATE_ID = "id";
    public static final String TEMPLATE_NAME = "name";
    public static final String TEMPLATE_PRIMARY_COLOR = "primary_color";
    public static final String TEMPLATE_SECONDARY_COLOR = "secondary_color";
    public static final String TEMPLATE_LAST_UPDATED = "last_updated";
    public static final String TEMPLATE_CREATED_AT = "created_at";
    
    // profession table attributes
    public static final String PROFESSION_ID = "id";
    public static final String PROFESSION_NAME = "name";
    public static final String PROFESSION_LAST_UPDATED = "last_updated";
    public static final String PROFESSION_CREATED_AT = "created_at";
      
    // company table attributes
    public static final String COMPANY_ID = "id";
    public static final String COMPANY_NAME = "name";
    public static final String COMPANY_LAST_UPDATED = "last_updated";
    public static final String COMPANY_CREATED_AT = "created_at";
    
    // user_business_card table
    public static final String USER_BUSINESS_CARD_LAST_UPDATED = "last_updated";
    public static final String USER_BUSINESS_CARD_CREATED_AT = "created_at";
    
}
