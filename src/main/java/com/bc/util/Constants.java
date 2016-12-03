/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bc.util;

import java.text.SimpleDateFormat;

/**
 *
 * @author konstantinos
 */
public class Constants {
    
    public enum SCOPE {
        READ("READ"), WRITE("WRITE");
        
        private final String value;
        
        SCOPE(String value) {
            this.value = value;
        }
        
        public String getValue() {
            return value;
        }
        
    }
    
    public static final String APPLICATION_NAME = "bc_wallet";
    public static final SimpleDateFormat LOG_DATE_FORMAT = new SimpleDateFormat("HH:mm:ss");
    
}
