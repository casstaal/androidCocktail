package com.example.cocktailapplication.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.cocktailapplication.entities.Cocktail;

import java.util.List;

@Dao
public interface CocktailDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertCocktail(Cocktail cocktail);
    //void insertCockail(String name, String category, String cocktailImageUrl, String alcoholic, int id);

    @Query("DELETE FROM cocktail_table")
    void deleteAll();

    @Query("SELECT * FROM cocktail_table ORDER BY name ASC")
    LiveData<List<Cocktail>> getAllCocktails();

    //TODO add method deleteCocktail(Cocktail cocktail) to delete a cocktail

}
