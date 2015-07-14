package com.wallpaperswitcher.activity;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.pwittchen.networkevents.library.ConnectivityStatus;
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

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    /*@Bind(R.id.fabLoadImage)
    FloatingActionButton fabLoadImage;

    @Bind(R.id.fabChangeWallpaper)
    FloatingActionButton fabChangeWallpaper;*/

    //@Bind(R.id.imageView)
    //ImageView imageView;

    @Bind(R.id.editTextRecherche)
    EditText editTextRecherche;

    @Bind(R.id.buttonRecherche)
    Button buttonRecherche;

    private List<String> images = new ArrayList<>();

    private ConnectivityStatus connectivityStatus;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.fragment_main, container, false);
        this.activity = this.getActivity();
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.activity.setTitle("Titre principal");



        buttonRecherche.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextRecherche.getText().toString() != null)
                    new ListPhotoFlickrTask().execute(activity.getResources().getString(R.string.flickr_photos_search),
                            activity.getResources().getString(R.string.flickr_cle_api), editTextRecherche.getText().toString(), "",
                            activity.getResources().getString(R.string.flickr_output),
                            activity.getResources().getString(R.string.flickr_no_json_callback));
                else
                    new ListPhotoFlickrTask().execute(activity.getResources().getString(R.string.flickr_photos_search),
                            activity.getResources().getString(R.string.flickr_cle_api), "papillon", "",
                            activity.getResources().getString(R.string.flickr_output),
                            activity.getResources().getString(R.string.flickr_no_json_callback));
            }
        });

        // Définit l'agencement des cellules, ici de façon verticale, comme une ListView
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));

        // Pour adapter en grille comme une RecyclerView, avec 1 cellules par ligne
        recyclerView.setLayoutManager(new GridLayoutManager(activity,1));

        // Puis créer un MyAdapter, lui fournir notre liste de villes.
        // Cet adapter servira à remplir notre recyclerview
        recyclerView.setAdapter(new CardsAdapter(images));
    }
}
