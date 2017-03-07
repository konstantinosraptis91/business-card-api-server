/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.bc.api.util;

import gr.bc.api.model.ResponseUser;
import gr.bc.api.model.User;

/**
 *
 * @author Konstantinos Raptis
 */
public class UserUtils {
    
    public static ResponseUser convertToResponseUser(User user) {
        ResponseUser responseUser = new ResponseUser();
        responseUser.setId(user.getId());
        responseUser.setFirstName(user.getFirstName());
        responseUser.setLastName(user.getLastName());
        return responseUser;
    }
    
}
