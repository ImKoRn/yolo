package com.korn.im.yolo.objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Photograph representation. Has name, price, description and list genres this photographer.
 */
public class Photograph implements Serializable{
    private String name;
    private int price;
    private String description;

    private List<String> listOfPhotoGenre = new ArrayList<>();

    public Photograph(String name, int price, String description) {
        this.name = name;
        this.price = price;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getListOfPhotoGenre() {
        return listOfPhotoGenre;
    }

    public void addToListOfPhotoGenre(List<String> listOfPhotoGenre) {
        this.listOfPhotoGenre.addAll(listOfPhotoGenre);
    }

    public void addToListOfPhotoGenre(String... genres) {
        Collections.addAll(this.listOfPhotoGenre, genres);
    }
}
