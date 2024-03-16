package com.example.cocktailapplication.data;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.cocktailapplication.entities.Cocktail;
import com.example.cocktailapplication.ui.CocktailListAdapter;

import java.util.ArrayList;
import java.util.List;

@Database(entities = {Cocktail.class}, version = 2, exportSchema = false)
public abstract class CocktailRoomDatabase extends RoomDatabase {

    public abstract CocktailDAO cocktailDao();

    private static CocktailRoomDatabase INSTANCE;

    public static CocktailRoomDatabase getDatabase(final Context context) {
        Log.d("WordRoomDatabase", "getDatabase test");
        if(INSTANCE == null) {
            synchronized (CocktailRoomDatabase.class) {
                if(INSTANCE == null) {
                    // Create database
                    Log.d("CocktailRoomDatabase", "Build database test 1");
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), CocktailRoomDatabase.class, "cocktail_database")
                            .fallbackToDestructiveMigration()
                            .build();
                    Log.d("CocktailRoomDatabase", "Build database test 2");
                }
            }
        }

        new PopulateDbAsync(INSTANCE).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        return INSTANCE;
    }


    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            new PopulateDbAsync(INSTANCE).execute();
        }
    };


    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final CocktailDAO mDao;
        //words to initial populate the database
//        String[] words = {"Espresso", "Ristretto", "Americano", "Lungo", "Cappuccino", "Café Latte",
//                "Flat White", "Cortado", "Double Espresso", "Piccolo Latte", "Mocha", "Affogato",
//                "Irish Coffee", "Macchiato"};

        Cocktail cocktail1 = new Cocktail("test1", "test1", "https://www.thecocktaildb.com/images/media/drink/2x8thr1504816928.jpg", "test1", 1);
//        Cocktail cocktail2 = new Cocktail("test2", "test2", "https://www.thecocktaildb.com/images/media/drink/2x8thr1504816928.jpg", "test2", 2);
//        Cocktail cocktail3 = new Cocktail("test3", "test4", "https://www.thecocktaildb.com/images/media/drink/2x8thr1504816928.jpg", "test3", 3);
//        Cocktail cocktail4 = new Cocktail("test3", "test4", "https://www.thecocktaildb.com/images/media/drink/2x8thr1504816928.jpg", "test4", 4);
//
//        ArrayList<Cocktail> cocktails = new ArrayList<Cocktail>();
//        cocktails.add


        public PopulateDbAsync(CocktailRoomDatabase instance) {
            mDao = instance.cocktailDao();
        }


        @Override
        protected Void doInBackground(Void... voids) {
//            LiveData<List<Cocktail>> cocktailList = mDao.getAllCocktails();


            // Start the app with a clean database every time.
            // is not ideal situation, but migration is not implemented
            // TODO: If we have no words, then create the initial list of words
            // TODO: delete the following method (why?)
            mDao.deleteAll();

            // TODO: only if we have no words, then create the initial list of words
//            for (Cocktail cocktail : ) {
////                mDao.insertWord(new Word(word));
//                mDao.insertCocktail(cocktail);
//            }
            mDao.insertCocktail(cocktail1);
            //mDao.insertCockail(cocktail1.getName(), cocktail1.getCategory(), cocktail1.getCocktailImageURL(), cocktail1.getAlcoholic(), cocktail1.getId());
            Log.d("CocktailRoomDatabase", "Inserted cocktail: " + cocktail1.getName());

            return null;
        }
    }


}
