package com.example.cocktailapplication.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cocktailapplication.R;
import com.example.cocktailapplication.entities.Cocktail;
import com.squareup.picasso.Picasso;

public class CocktailDetailActivity extends AppCompatActivity {

    private final String LOG_TAG = this.getClass().getSimpleName();

    // variabelen voor textviews
    TextView mNameTextView;
    TextView mCategoryTextView;
    TextView mAlcoholicTextView;
    TextView mIdTextView;
    ImageView mCocktailImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cocktail_detail_activity);

        Log.i(LOG_TAG, "onCreate");

        ActionBar toolbar = getSupportActionBar();
        if(null != toolbar) {
            toolbar.setTitle("Cocktail details");
        }

        // Todo: meer schermvelden toevoegen.

        // Todo: views uit scherm koppelen aan Java variabelen
        mNameTextView = findViewById(R.id.cocktail_detail_activity_name);
        mCategoryTextView = findViewById(R.id.cocktail_detail_activity_category);
        mAlcoholicTextView = findViewById(R.id.cocktail_detail_activity_alcoholic);
        mIdTextView = findViewById(R.id.cocktail_detail_activity_id);
        mCocktailImage = findViewById(R.id.cocktail_detail_activity_image);


        // Todo: Data lezen uit de intent
        Intent intent = getIntent();
        Cocktail mCocktail = (Cocktail) intent.getSerializableExtra(MainActivity.EXTRA_ADDED_COCKTAIL);

        // Todo: ingelezen data in de textviews zetten
        if(mCocktail != null) {
            mNameTextView.setText(mCocktail.getName());
            mCategoryTextView.setText(mCocktail.getCategory());
            mAlcoholicTextView.setText(mCocktail.getAlcoholic());

            String idString = String.valueOf(mCocktail.getId());
            mIdTextView.setText(idString);

            Picasso.get().load(mCocktail.getCocktailImageURL()).into(mCocktailImage);
        }
    }
}
