package com.wallpaperswitcher.activity;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.wallpaperswitcher.R;
import com.wallpaperswitcher.cards.CardsAdapter;
import com.wallpaperswitcher.tasks.ListPhotoFlickrTask;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by FlorianXPS on 12/07/2015.
 */
public class MainFragment extends Fragment {
    private View rootView;
    private Activity activity;

    //@Bind(R.id.recyclerView)
    //RecyclerView recyclerView;

    /*@Bind(R.id.fabLoadImage)
    FloatingActionButton fabLoadImage;

    @Bind(R.id.fabChangeWallpaper)
    FloatingActionButton fabChangeWallpaper;*/

    @Bind(R.id.imageView)
    ImageView imageView;

    @Bind(R.id.editTextRecherche)
    EditText editTextRecherche;

    @Bind(R.id.buttonRecherche)
    Button buttonRecherche;

    private List<String> images = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.fragment_main, container, false);
        this.activity = this.getActivity();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //init();
/*
        buttonRecherche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextRecherche.getText().toString() != null)
                    new ListPhotoFlickrTask().execute("flickr.photos.search", "61d8670199834d7d7d48bcf570071a7a", editTextRecherche.getText().toString(), "", "json", "1");
                else
                    new ListPhotoFlickrTask().execute("flickr.photos.search", "61d8670199834d7d7d48bcf570071a7a", "papillon", "", "json", "1");
            }
        });
        */
    }

    private void init(){
        // ButterKnife, à initialiser après le setContentView de l'activity
        ButterKnife.bind(this.getActivity());

        //définit l'agencement des cellules, ici de façon verticale, comme une ListView
        //recyclerView.setLayoutManager(new LinearLayoutManager(activity));

        //pour adapter en grille comme une RecyclerView, avec 1 cellules par ligne
        //recyclerView.setLayoutManager(new GridLayoutManager(activity,1));

        //puis créer un MyAdapter, lui fournir notre liste de villes.
        //cet adapter servira à remplir notre recyclerview
        //recyclerView.setAdapter(new CardsAdapter(images));
    }

    private void addImageToCard(String urlImage) {
        images.add(urlImage);
    }
}
