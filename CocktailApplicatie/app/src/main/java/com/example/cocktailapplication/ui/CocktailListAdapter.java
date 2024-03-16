package com.example.cocktailapplication.ui;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cocktailapplication.R;
import com.example.cocktailapplication.data.CocktailDAO;
import com.example.cocktailapplication.entities.Cocktail;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CocktailListAdapter extends RecyclerView.Adapter<CocktailListAdapter.CocktailListViewHolder> {

    private ArrayList<Cocktail> mCocktailList;
//    private ArrayList<Cocktail> mFavoriteCocktails = new ArrayList<>();
    private LayoutInflater mInflater;



    public CocktailListAdapter(ArrayList<Cocktail> mCocktailList, Context context) {
        this.mCocktailList = mCocktailList;
        mInflater = LayoutInflater.from(context);
//        mFavoriteCocktails = new ArrayList<>();
    }

    public void setCocktailsList(ArrayList<Cocktail> cocktails) {
        this.mCocktailList = cocktails;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CocktailListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View mItemView = mInflater.inflate(R.layout.cocktail_list_item, parent, false);
        return new CocktailListViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull CocktailListViewHolder holder, int position) {
        Cocktail mCocktail = mCocktailList.get(position);
        holder.tvCocktailName.setText(mCocktail.getName());
        holder.tvCocktailCategory.setText(mCocktail.getCategory());
        holder.tvCocktailAlcoholic.setText(mCocktail.getAlcoholic());

        String idString = String.valueOf(mCocktail.getId());
        holder.tvCocktailID.setText(idString);

        String url = mCocktail.getCocktailImageURL();

        Picasso.get().load(url).into(holder.imgCocktailImage);
    }

    @Override
    public int getItemCount() {

        if(mCocktailList != null) {
            return mCocktailList.size();
        } else return 0;
    }

    class CocktailListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final String LOG_TAG = this.getClass().getSimpleName();
        public TextView tvCocktailName;
        public TextView tvCocktailCategory;
        public TextView tvCocktailAlcoholic;
        public TextView tvCocktailID;
        public ImageView imgCocktailImage;

        public CocktailListViewHolder(@NonNull View itemView, CocktailListAdapter cocktailAdapter) {
            super(itemView);
            tvCocktailName = itemView.findViewById(R.id.cocktail_list_item_name);
            tvCocktailCategory = itemView.findViewById(R.id.cocktail_list_item_category);
            tvCocktailAlcoholic = itemView.findViewById(R.id.cocktail_list_item_alcoholic);
            tvCocktailID = itemView.findViewById(R.id.cocktail_list_item_id);
            imgCocktailImage = itemView.findViewById(R.id.cocktail_list_item_image);
            itemView.setOnClickListener(this);

//            String name = tvCocktailName.getText().toString();




            Button button = itemView.findViewById(R.id.favorite_button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Cocktail mCocktail = mCocktailList.get(position);
                    Log.d(LOG_TAG, "User tapped favorite button from cocktail: " + mCocktail.getName());

                    // todo insert mCocktail in de favorite database

                    // todo favorite button rood maken
//                    button.setBackgroundResource(R.drawable.baseline_favorite_red_24);

                    if(mCocktail.getFavStatus() == 0) {
                        mCocktail.setFavStatus(1);
                        button.setBackgroundResource(R.drawable.baseline_favorite_red_24);
//                        mFavoriteCocktails.add(mCocktail);

                    } else {
                        mCocktail.setFavStatus(0);
                        button.setBackgroundResource(R.drawable.baseline_favorite_shadow_24);
//                        mFavoriteCocktails.remove(position);
                    }
                }
            });
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Log.d(LOG_TAG, "Listitem onClick: " + position);

            // Cocktail op index 'position' uit de lijst lezen
            Cocktail mCocktail = mCocktailList.get(position);

            // Nieuwe intent maken om CocktailDetailActivity te openen
            Intent intent = new Intent(view.getContext(), CocktailDetailActivity.class);

            // Cocktail data in intent stoppen
            intent.putExtra(MainActivity.EXTRA_ADDED_COCKTAIL, mCocktail);

            // Activity starten
            view.getContext().startActivity(intent);
        }
    }

//    public ArrayList<Cocktail> getFavoriteCocktails() {
//        return mFavoriteCocktails;
//    }
}
