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
    private int primaryColor;
    private int secondaryColor;

    public Template() {
    }

    public Template(long id, int primaryColor, int secondaryColor) {
        this.id = id;
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getPrimaryColor() {
        return primaryColor;
    }

    public void setPrimaryColor(int primaryColor) {
        this.primaryColor = primaryColor;
    }

    public int getSecondaryColor() {
        return secondaryColor;
    }

    public void setSecondaryColor(int secondaryColor) {
        this.secondaryColor = secondaryColor;
    }

    @Override
    public String toString() {
        return "Template{" + "id=" + id + ", primaryColor=" + primaryColor + ", secondaryColor=" + secondaryColor + '}';
    }
        
}
