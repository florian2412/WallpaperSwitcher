package com.wallpaperswitcher.cards;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.wallpaperswitcher.R;
import com.squareup.picasso.Picasso;

/**
 * Created by FlorianXPS on 05/07/2015.
 */
public class CardsViewHolder extends RecyclerView.ViewHolder{

    //private TextView textViewView;
    private ImageView imageView;

    // itemView est la vue correspondante à 1 cellule
    public CardsViewHolder (View itemView) {
        super(itemView);

        // C'est ici que l'on fait nos findView

        //textViewView = (TextView) itemView.findViewById(R.id.text);
        imageView = (ImageView) itemView.findViewById(R.id.image);
    }

    // Puis ajouter une fonction pour remplir la cellule en fonction d'un MyObject
    public void bind (String url){
        //textViewView.setText(url);
        Picasso.with(imageView.getContext()).load(url).centerCrop().fit().into(imageView);
    }
}