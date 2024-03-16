package com.example.cocktailapplication.ui;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.cocktailapplication.data.CocktailRepository;
import com.example.cocktailapplication.entities.Cocktail;

import java.util.List;

public class CocktailViewModel extends AndroidViewModel {

    private CocktailRepository mRepository;

    private LiveData<List<Cocktail>> mAllCocktails;

    public CocktailViewModel (Application application) {
        super(application);
        mRepository = new CocktailRepository(application);
        mAllCocktails = mRepository.getAllCocktails();
    }

    public LiveData<List<Cocktail>> getAllCocktails() {
        return mAllCocktails;
    }

    public void insert(Cocktail cocktail) {
        mRepository.insert(cocktail);
    }
}
