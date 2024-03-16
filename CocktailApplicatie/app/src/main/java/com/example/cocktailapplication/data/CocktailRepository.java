package com.example.cocktailapplication.data;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.cocktailapplication.entities.Cocktail;
import java.util.List;


public class CocktailRepository {
    private CocktailDAO mCocktailDao;
    private LiveData<List<Cocktail>> mAllCocktails;

    public CocktailRepository(Application application) {
        CocktailRoomDatabase db = CocktailRoomDatabase.getDatabase(application);
        mCocktailDao = db.cocktailDao();
        mAllCocktails = mCocktailDao.getAllCocktails();
    }

    public LiveData<List<Cocktail>> getAllCocktails() {
        return mAllCocktails;
    }

    public void insert (Cocktail cocktail) {
        new insertAsyncTask(mCocktailDao).execute(cocktail);
    }

    private static class insertAsyncTask extends AsyncTask<Cocktail, Void, Void> {

        private CocktailDAO mAsyncTaskDao;

        insertAsyncTask(CocktailDAO dao) {
            mAsyncTaskDao = dao;
        }

//        @Override
//        protected Void doInBackground(final Word... params) {
//            mAsyncTaskDao.insert(params[0]);
//            return null;
//        }

        @Override
        protected Void doInBackground(Cocktail... cocktails) {
            mAsyncTaskDao.insertCocktail(cocktails[0]);
            return null;
        }
    }
}
