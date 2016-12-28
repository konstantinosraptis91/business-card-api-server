/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.bc.api.entity;

import javax.validation.constraints.NotNull;

/**
 *
 * @author Konstantinos Raptis
 */
public class Profession {
    
    private long id;
    @NotNull
    private String name;
    private String description;

    public Profession() {
    }

    public Profession(long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Profession{" + "name=" + name + ", description=" + description + '}';
    }
       
}
