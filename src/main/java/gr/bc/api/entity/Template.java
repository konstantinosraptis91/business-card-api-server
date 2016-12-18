/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.bc.api.entity;

/**
 *
 * @author Konstantinos Raptis
 */
public class Template {
    
    private long id;
    private String name;
    private String primaryColor;
    private String secondaryColor;

    public Template() {
    }

    public Template(long id, String name, String primaryColor, String secondaryColor) {
        this.id = id;
        this.name = name;
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPrimaryColor() {
        return primaryColor;
    }

    public void setPrimaryColor(String primaryColor) {
        this.primaryColor = primaryColor;
    }

    public String getSecondaryColor() {
        return secondaryColor;
    }

    public void setSecondaryColor(String secondaryColor) {
        this.secondaryColor = secondaryColor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public String toString() {
        return "Template{" + "id=" + id + ", name=" + name + ", primaryColor=" + primaryColor + ", secondaryColor=" + secondaryColor + '}';
    }
       
}
