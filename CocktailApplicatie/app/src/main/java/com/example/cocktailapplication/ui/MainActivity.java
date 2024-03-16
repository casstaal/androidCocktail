package com.example.cocktailapplication.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.example.cocktailapplication.R;
import com.example.cocktailapplication.data.CocktailRoomDatabase;
import com.example.cocktailapplication.entities.Cocktail;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RandomCocktailApiTask.OnRandomCocktailListener {


    private final String LOG_TAG = this.getClass().getSimpleName();
    public static final String COCKTAILS_LIST = "cocktails_list";
    public static final String EXTRA_ADDED_COCKTAIL = "added_cocktail";
    public static final int ADD_PERSON_REQUEST = 1;
    //private Button mAddPersonButton;
    private ArrayList<Cocktail> mCocktails = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private CocktailListAdapter mAdapter;
    private CoordinatorLayout coordinatorLayout;
    private SwipeRefreshLayout swipeRefreshLayout;
    private CocktailViewModel mCocktailViewModel;

    private final String randomCocktailURL = "https://www.thecocktaildb.com/api/json/v1/1/search.php?f=a";

    /**
     * Creates the content view, the toolbar, and the floating action button.
     *
     * This method is provided in the Basic Activity template.
     *
     * @param savedInstanceState Saved instance state bundle.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Button button = findViewById(R.id.favorite_button);

        Log.i(LOG_TAG, "onCreate");

        // Check if recreating a previously destroyed instance.
        if (savedInstanceState != null) {
            // Restore value of members from saved state.
            mCocktails = (ArrayList<Cocktail>) savedInstanceState.getSerializable(COCKTAILS_LIST);
        } else {
            // Initialize members with default values for a new instance.
            mCocktails = new ArrayList<>();

            loadRandomCocktailsFromApi();
        }

        mRecyclerView = findViewById(R.id.rv_cocktail_list);
        mAdapter = new CocktailListAdapter(mCocktails, this);
        mRecyclerView.setAdapter(mAdapter);

        RecyclerView.LayoutManager layoutManager;

        // Is het scherm horizontaal of verticaal?
        Configuration configuration = this.getResources().getConfiguration();
        if(configuration.orientation == Configuration.ORIENTATION_PORTRAIT){
            // Verticaal: LinearLayoutMgr
            layoutManager = new LinearLayoutManager(this);
        } else {
            // Horizontaal: GridLayoutMgr, 1 columns
            layoutManager = new LinearLayoutManager(this);
        }
        mRecyclerView.setLayoutManager(layoutManager);

//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent addPersonIntent = new Intent(MainActivity.this, AddPersonActivity.class);
//                startActivityForResult(addPersonIntent, ADD_PERSON_REQUEST);
//            }
//        });

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(LOG_TAG, "onRefresh");
                loadRandomCocktailsFromApi();
            }
        });

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_activity_coordinator_layout);

        //mWordViewModel = ViewModelProviders.of(this).get(WordViewModel.class);
        mCocktailViewModel = new ViewModelProvider(this).get(CocktailViewModel.class);
        // Update the cached copy of the words in the adapter.
        mCocktailViewModel.getAllCocktails().observe(this, new Observer<List<Cocktail>>() {
            @Override
            public void onChanged(List<Cocktail> cocktails) {
                 // TODO let the adapter know that the words are changed

            }

//            @Override
//            public void onChanged(List<Word> words) {
//                // the LiveData<List<Word>> of the ViewModel has changed so:
//                // TODO let the adapter know that the words are changed
//
//            }
        });


//        if(mCocktail.getFavStatus() == 0) {
//            button.setBackgroundResource(R.drawable.baseline_favorite_shadow_24);
////                        mFavoriteCocktails.add(mCocktail);
//
//        } else {
//            button.setBackgroundResource(R.drawable.baseline_favorite_red_24);
////                        mFavoriteCocktails.remove(position);
//        }
    }

    private void loadRandomCocktailsFromApi() {
        RandomCocktailApiTask task = new RandomCocktailApiTask(this);
        task.execute(randomCocktailURL);
    }

    /**
     * Inflates the menu, and adds items to the action bar if it is present.
     *
     * @param menu Menu to inflate.
     * @return Returns true if the menu inflated.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate();
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Handles app bar item clicks.
     *
     * @param item Item clicked.
     * @return True if one of the defined items was clicked.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // This comment suppresses the Android Studio warning about simplifying
        // the return statements.
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_favorites) {
            Intent favoritesIntent = new Intent(this, FavoritesActivity.class);
            startActivity(favoritesIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Displays a Toast with the message.
     *
     * @param message Message to display.
     */
    public void displayToast(String message) {
        Toast.makeText(getApplicationContext(), message,
                Toast.LENGTH_LONG).show();
    }

    /**
     * https://www.digitalocean.com/community/tutorials/android-snackbar-example-tutorial
     *
     * @param message
     */
    public void displaySnackbar(String message){
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_PERSON_REQUEST) {
            if (resultCode == RESULT_OK) {
                Cocktail mCocktail = (Cocktail) data.getSerializableExtra(EXTRA_ADDED_COCKTAIL);
                if(mCocktail != null) {
                    // Person exists
                    Log.i(LOG_TAG, mCocktail.toString());

                    mCocktails.add(mCocktail);
                    mAdapter.setCocktailsList(mCocktails);
                    displaySnackbar(getString(R.string.msg_cocktail_successfully_added));

                } else {
                    Log.w(LOG_TAG, "Cocktail not found.");
                }
            } else if (resultCode == RESULT_CANCELED){
                Log.i(LOG_TAG, getString(R.string.msg_add_cocktail_cancelled));
                displaySnackbar(getString(R.string.msg_add_cocktail_cancelled));
            }
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(LOG_TAG, "onSaveInstanceState");
        outState.putSerializable(COCKTAILS_LIST, mCocktails);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(LOG_TAG, "onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(LOG_TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(LOG_TAG, "onDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(LOG_TAG, "onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(LOG_TAG, "onResume");
    }


//    public void onPeopleAvailable(ArrayList<Cocktail> cocktails) {
//        Log.d(LOG_TAG, "onCocktailsAvailable, # cocktails = " + cocktails.size());
//        // Todo: people in recyclerview zetten
//        mAdapter.setCocktailsList(cocktails);
//        swipeRefreshLayout.setRefreshing(false);
//    }

    @Override
    public void onCocktailAvailable(ArrayList<Cocktail> cocktails) {
        Log.d(LOG_TAG, "onCocktailsAvailable, # cocktails = " + cocktails.size());
        // Todo: people in recyclerview zetten
        mAdapter.setCocktailsList(cocktails);
        swipeRefreshLayout.setRefreshing(false);
    }
}