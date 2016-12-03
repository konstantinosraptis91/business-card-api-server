/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.bc.api.controller;

import gr.bc.api.entity.User;
import gr.bc.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author konstantinos
 */
@RestController
@RequestMapping("/api/user")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void addUser(@RequestBody User user) {
        userService.addUser(user);
    }
    
    @RequestMapping(method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public int updateUser(@RequestBody User user) {
        return  userService.updateUser(user);
    }
    
     @RequestMapping(value="/{email}", method = RequestMethod.DELETE)
    public int deleteUser(@PathVariable("email") String email) {
        return userService.deleteUser(email);
    }
    
    @RequestMapping(value="/{email}", method = RequestMethod.GET, 
            produces = MediaType.APPLICATION_JSON_VALUE)
    public User getUserByEmail(@PathVariable("email") String email) {
        return userService.getUserByEmail(email);
    }
        
    @RequestMapping(method = RequestMethod.GET, 
            produces = MediaType.APPLICATION_JSON_VALUE)
    public User getUserByName(@RequestParam(value = "firstName", required = false) String firstName,
                              @RequestParam(value = "lastName", required = false) String lastName) {
        return userService.getUserByName(firstName, lastName);
    }
    
}
