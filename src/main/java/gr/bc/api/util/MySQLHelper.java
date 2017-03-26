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
    public static final String USER_RATING_TABLE = "user_rating";
    public static final String USER_BUSINESS_CARD_TABLE = USER_TABLE + "_" + BUSINESS_CARD_TABLE;
    
    // user table attributes
    public static final String USER_ID = USER_TABLE + "_id";
    public static final String USER_EMAIL = USER_TABLE + "_email";
    public static final String USER_PASSWORD = USER_TABLE + "_password";
    public static final String USER_FIRSTNAME = USER_TABLE + "_firstname";
    public static final String USER_LASTNAME = USER_TABLE + "_lastname";
    public static final String USER_LAST_UPDATED = USER_TABLE + "_last_updated";
    public static final String USER_CREATED_AT = USER_TABLE + "_created_at";
    public static final String USER_TOKEN = USER_TABLE + "_token";
    public static final String USER_IMAGE_PATH = USER_TABLE + "_image_path";
    
    // business card table attributes
    public static final String BUSINESS_CARD_ID = BUSINESS_CARD_TABLE + "_id";
    public static final String BUSINESS_CARD_TITLE = BUSINESS_CARD_TABLE + "_title";
    public static final String BUSINESS_CARD_DESCRIPTION = BUSINESS_CARD_TABLE + "_description";
    public static final String BUSINESS_CARD_PHONE_NUMBER1 = BUSINESS_CARD_TABLE + "_phone_number_1";
    public static final String BUSINESS_CARD_PHONE_NUMBER2 = BUSINESS_CARD_TABLE + "_phone_number_2";
    public static final String BUSINESS_CARD_LINKEDIN = BUSINESS_CARD_TABLE + "_linkedin";
    public static final String BUSINESS_CARD_WEBSITE = BUSINESS_CARD_TABLE + "_website";
    public static final String BUSINESS_CARD_UNIVERSAL = BUSINESS_CARD_TABLE + "_universal";
    public static final String BUSINESS_CARD_ADDRESS_1 = BUSINESS_CARD_TABLE + "_address_1";    
    public static final String BUSINESS_CARD_ADDRESS_2 = BUSINESS_CARD_TABLE + "_address_2";    
    public static final String BUSINESS_CARD_EMAIL = BUSINESS_CARD_TABLE + "_email";
    public static final String BUSINESS_CARD_LAST_UPDATED = BUSINESS_CARD_TABLE + "_last_updated";
    public static final String BUSINESS_CARD_CREATED_AT = BUSINESS_CARD_TABLE + "_created_at";
    
    // template table attributes
    public static final String TEMPLATE_ID = TEMPLATE_TABLE + "_id";
    public static final String TEMPLATE_NAME = TEMPLATE_TABLE + "_name";
    public static final String TEMPLATE_PRIMARY_COLOR = TEMPLATE_TABLE + "_primary_color";
    public static final String TEMPLATE_SECONDARY_COLOR = TEMPLATE_TABLE + "_secondary_color";
    public static final String TEMPLATE_LAST_UPDATED = TEMPLATE_TABLE + "_last_updated";
    public static final String TEMPLATE_CREATED_AT = TEMPLATE_TABLE + "_created_at";
    
    // profession table attributes
    public static final String PROFESSION_ID = "profession_id";
    public static final String PROFESSION_NAME = "profession_name";
    public static final String PROFESSION_DESCRIPTION = "profession_description";
    public static final String PROFESSION_LAST_UPDATED = PROFESSION_TABLE + "_last_updated";
    public static final String PROFESSION_CREATED_AT = PROFESSION_TABLE + "_created_at";
    
    // user rating table attributes
    public static final String USER_RATING_ID = USER_RATING_TABLE + "_id";
    public static final String USER_RATING_STARS = USER_RATING_TABLE + "_stars";
    public static final String USER_RATING_TITLE = USER_RATING_TABLE + "_title";
    public static final String USER_RATING_DESCRIPTION = USER_RATING_TABLE + "_description";
    public static final String USER_RATING_LAST_UPDATED = USER_RATING_TABLE + "_last_updated";
    public static final String USER_RATING_CREATED_AT = USER_RATING_TABLE + "_created_at";
    
    // user_business_card table
    public static final String USER_BUSINESS_CARD_LAST_UPDATED = USER_BUSINESS_CARD_TABLE + "_last_updated";
    public static final String USER_BUSINESS_CARD_CREATED_AT = USER_BUSINESS_CARD_TABLE + "_created_at";
    
}
