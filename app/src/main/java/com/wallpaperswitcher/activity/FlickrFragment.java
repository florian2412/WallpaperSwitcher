package com.wallpaperswitcher.activity;

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.wallpaperswitcher.R;
import com.wallpaperswitcher.cards.CardsAdapter;
import com.wallpaperswitcher.flickrService.FlickrService;
import com.wallpaperswitcher.flickrService.model.FlickSearch;
import com.wallpaperswitcher.flickrService.model.Photo;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.RestAdapter;
import retrofit.android.AndroidLog;

public class FlickrFragment extends Fragment {

    private View rootView;
    private Activity activity;
    private Menu menu;

    @Bind(R.id.fabSearch)
    FloatingActionButton fabSearch;

    @Bind(R.id.editTextSearch)
    EditText editTextSearch;

    private List<String> images = new ArrayList<>();

    private boolean isInSearch = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.fragment_flickr, container, false);
        this.activity = this.getActivity();

        ButterKnife.bind(this, activity);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);

        this.activity.setTitle("Images depuis Flickr");

        fabSearch.setVisibility(View.VISIBLE);
        fabSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isInSearch) {
                    isInSearch = true;
                    menu.findItem(R.id.menu_option_valid_search).setVisible(true);
                    editTextSearch.setVisibility(View.VISIBLE);
                } else {
                    isInSearch = false;
                    menu.findItem(R.id.menu_option_valid_search).setVisible(false);
                    editTextSearch.setVisibility(View.GONE);
                }
            }
        });

        // Définit l'agencement des cellules, ici de façon verticale, comme une ListView
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));

        // Pour adapter en grille comme une RecyclerView, avec 1 cellules par ligne
        recyclerView.setLayoutManager(new GridLayoutManager(activity,1));

        // Puis créer un MyAdapter, lui fournir notre liste de villes.
        // Cet adapter servira à remplir notre recyclerview
        recyclerView.setAdapter(new CardsAdapter(images));

        return rootView;
    }


    public void addImageToCard(String urlPhoto) {
        images.add(urlPhoto);
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        this.menu = menu;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_option_valid_search :
                if (editTextSearch.getText().toString() != null)
                    new ListPhotoFlickrTask().execute(activity.getResources().getString(R.string.flickr_photos_search),
                            activity.getResources().getString(R.string.flickr_cle_api), editTextSearch.getText().toString(), "",
                            activity.getResources().getString(R.string.flickr_output),
                            activity.getResources().getString(R.string.flickr_no_json_callback));

                else
                    new ListPhotoFlickrTask().execute(activity.getResources().getString(R.string.flickr_photos_search),
                            activity.getResources().getString(R.string.flickr_cle_api), "papillon", "",
                            activity.getResources().getString(R.string.flickr_output),
                            activity.getResources().getString(R.string.flickr_no_json_callback));
                item.setVisible(false);
                editTextSearch.setVisibility(View.GONE);
                isInSearch = false;
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private class ListPhotoFlickrTask extends AsyncTask<String,Void,FlickSearch> {

        private FlickSearch flickSearch;

        @Override
        protected FlickSearch doInBackground(String...params) {
            FlickrService flickService = new RestAdapter.Builder()
                    .setEndpoint(FlickrService.ENDPOINT)
                    .setLog(new AndroidLog("retrofit"))
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .build()
                    .create(FlickrService.class);

            String method = params[0];
            String api_key = params[1];
            String tags = params[2];
            String text = params[3];
            String format = params[4];
            String nojsoncallback = params[5];

            flickSearch = flickService.getPhotos(method, api_key, tags, text, format, nojsoncallback);

            return flickSearch;
        }

        @Override
        protected void onPostExecute(FlickSearch flickSearch) {
            super.onPostExecute(flickSearch);
            // Exemple URL pour récupérer une photo
            // https://farm{farm-id}.staticflickr.com/{server-id}/{id}_{secret}.jpg

            if(flickSearch.getPhotos() != null) {
                Photo photo = null;
                String urlPhoto = null;

                for(int i = 0; i < 10; i++) {
                    photo = flickSearch.getPhotos().getPhoto().get(i);
                    urlPhoto = "https://farm"  + photo.getFarm() + ".staticflickr.com/" + photo.getServer() + "/" + photo.getId() + "_" + photo.getSecret() + ".jpg";
                    addImageToCard(urlPhoto);
                }
            }
        }
    }
}
