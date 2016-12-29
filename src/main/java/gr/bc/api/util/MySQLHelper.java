/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
    public static final String USER_ID = "user_id";
    public static final String USER_EMAIL = "user_email";
    public static final String USER_PASSWORD = "user_password";
    public static final String USER_FIRSTNAME = "user_firstname";
    public static final String USER_LASTNAME = "user_lastname";
           
    // business card table attributes
    public static final String BUSINESS_CARD_ID = "business_card_id";
    public static final String BUSINESS_CARD_TITLE = "business_card_title";
    public static final String BUSINESS_CARD_DESCRIPTION = "business_card_description";
    public static final String BUSINESS_CARD_PHONE_NUMBER1 = "business_card_phone_number_1";
    public static final String BUSINESS_CARD_PHONE_NUMBER2 = "business_card_phone_number_2";
    public static final String BUSINESS_CARD_LINKEDIN = "business_card_linkedin";
    public static final String BUSINESS_CARD_WEBSITE = "business_card_website";
    public static final String BUSINESS_CARD_UNIVERSAL = "business_card_universal";
       
    // template table attributes
    public static final String TEMPLATE_ID = "template_id";
    public static final String TEMPLATE_NAME = "template_name";
    public static final String TEMPLATE_PRIMARY_COLOR = "template_primary_color";
    public static final String TEMPLATE_SECONDARY_COLOR = "template_secondary_color";
    
    // profession table attributes
    public static final String PROFESSION_ID = "profession_id";
    public static final String PROFESSION_NAME = "profession_name";
    public static final String PROFESSION_DESCRIPTION = "profession_description";
    
    // user rating table attributes
    public static final String USER_RATING_ID = "user_rating_id";
    public static final String USER_RATING_STARS = "user_rating_stars";
    public static final String USER_RATING_TITLE = "user_rating_title";
    public static final String USER_RATING_DESCRIPTION = "user_rating_description";
       
}
