package com.example.cocktailapplication.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "cocktail_table")
public class Cocktail implements Serializable {

    @NonNull
    @PrimaryKey
    private String name;
    private String category;
    @ColumnInfo(name = "cocktail_image_url")
    private String cocktailImageURL;
    private String alcoholic;
    private int id;
    private int favStatus;

    public Cocktail(@NonNull String name, String category, String cocktailImageURL, String alcoholic, int id) {
        this.name = name;
        this.category = category;
        this.cocktailImageURL = cocktailImageURL;
        this.alcoholic = alcoholic;
        this.id = id;
        this.favStatus = 0;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getCocktailImageURL() {
        return cocktailImageURL;
    }

    public String getAlcoholic() {
        return alcoholic;
    }

    public int getId() {
        return id;
    }

    public void setFavStatus(int status) {
        this.favStatus = status;
    }

    public int getFavStatus() {
        return  this.favStatus;
    }
}
