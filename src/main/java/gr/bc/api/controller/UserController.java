/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.bc.api.controller;

import gr.bc.api.entity.User;
import gr.bc.api.service.UserService;
import java.util.ArrayList;
import java.util.List;
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

    // ADD USER    
    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public User addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    // UPDATE USER
    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public User updateUser(@PathVariable("id") int id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    // DELETE USER
    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.DELETE)
    public void deleteUser(@PathVariable("id") int id) {
        userService.deleteUser(id);
    }

    // GET USER (email, first name, last name)
    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> getUsers(
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "firstName", required = false) String firstName,
            @RequestParam(value = "lastName", required = false) String lastName) {
        List<User> users = new ArrayList<>();
        if (email != null) {
            users.add(userService.getUserByEmail(email));
            return users;
        }
        return userService.getUsersByName(firstName, lastName);
    }

    // GET USER (id)
    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public User getUserById(@PathVariable("id") int id) {
        return userService.getUserById(id);
    }

}
