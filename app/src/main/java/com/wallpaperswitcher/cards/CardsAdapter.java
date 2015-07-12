package com.wallpaperswitcher.cards;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wallpaperswitcher.R;

import java.util.List;

/**
 * Created by FlorianXPS on 05/07/2015.
 */
public class CardsAdapter extends RecyclerView.Adapter<CardsViewHolder> {

    List<String> list;

    //ajouter un constructeur prenant en entrée une liste
    public CardsAdapter(List<String> list) {
        this.list = list;
    }

    //cette fonction permet de créer les viewHolder
    //et par la même indiquer la vue à inflater (à partir des layout xml)
    @Override
    public CardsViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cell_cards,viewGroup,false);
        return new CardsViewHolder(view);
    }

    //c'est ici que nous allons remplir notre cellule avec le texte/image de chaque MyObjects
    @Override
    public void onBindViewHolder(CardsViewHolder myViewHolder, int position) {
        String url = list.get(position);
        myViewHolder.bind(url);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}