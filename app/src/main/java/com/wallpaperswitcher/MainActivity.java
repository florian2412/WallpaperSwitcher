package com.wallpaperswitcher;

import android.app.SearchManager;
import android.app.WallpaperManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Fade;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.wallpaperswitcher.cards.CardsAdapter;
import com.wallpaperswitcher.flickrService.model.FlickSearch;
import com.wallpaperswitcher.flickrService.FlickrService;
import com.wallpaperswitcher.flickrService.model.Photo;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.RestAdapter;
import retrofit.android.AndroidLog;

public class MainActivity extends AppCompatActivity {

    //
    // Permet de ne pas l'associer dans le oncreate et de gagner du temps et de la place au niveau du code
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.drawerLayout)
    DrawerLayout drawerLayout;

    @Bind(R.id.navigationView)
    NavigationView navigationView;

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    @Bind(R.id.fabLoadImage)
    FloatingActionButton fabLoadImage;

    @Bind(R.id.fabChangeWallpaper)
    FloatingActionButton fabChangeWallpaper;

    @Bind(R.id.imageView)
    ImageView imageView;

    @Bind(R.id.editTextRecherche)
    EditText editTextRecherche;

    @Bind(R.id.buttonRecherche)
    Button buttonRecherche;

    private ActionBarDrawerToggle drawerToggle;
    private List<String> images = new ArrayList<>();

    private FlickSearch flickSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ButterKnife, à initialiser après le setContentView de l'activity
        ButterKnife.bind(this);

        setupWindowAnimations();

        drawerToggle = new ActionBarDrawerToggle(this,this.drawerLayout,0,0);
        drawerLayout.setDrawerListener(this.drawerToggle);

        // Definir notre toolbar en tant qu'actionBar
        setSupportActionBar(toolbar);

        // Afficher le bouton retour
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // Mon action
                        drawerLayout.closeDrawers();

                        switch(menuItem.getItemId()) {
                            case R.id.nav_flickr :
                                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                                //intent.putExtra(SearchManager.QUERY, getActionBar().getTitle());

                                if (intent != null) {
                                    startActivity(intent);
                                    Toast.makeText(getApplicationContext(), "Changement d'activité réussi", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Changement d'activité foireuse", Toast.LENGTH_LONG).show();
                                }
                                return true;
                            default:
                                return true;
                        }
                        //return true;
                    }
                });

        //définit l'agencement des cellules, ici de façon verticale, comme une ListView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //pour adapter en grille comme une RecyclerView, avec 1 cellules par ligne
        recyclerView.setLayoutManager(new GridLayoutManager(this,1));

        //puis créer un MyAdapter, lui fournir notre liste de villes.
        //cet adapter servira à remplir notre recyclerview
        recyclerView.setAdapter(new CardsAdapter(images));

        fabLoadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Picasso.with(getApplicationContext()).load("https://unsplash.it/1280/1024/?random").into(imageView);

                Snackbar.make(fabLoadImage, "Image en cours de chargement...", Snackbar.LENGTH_LONG).setAction("Ok", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                }).show();

                fabChangeWallpaper.setVisibility(View.VISIBLE);
            }
        });

        fabChangeWallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                WallpaperManager myWallpaperManager = WallpaperManager.getInstance(getApplicationContext());
                try {
                    myWallpaperManager.setBitmap(bitmap);
                    Snackbar.make(fabChangeWallpaper, "Fond d'écran en cours de changement...", Snackbar.LENGTH_LONG).setAction("Ok", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {}
                    }).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


        buttonRecherche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTextRecherche.getText().toString() != null)
                    new ListPhotoTask().execute("flickr.photos.search", "61d8670199834d7d7d48bcf570071a7a", editTextRecherche.getText().toString(), "", "json", "1");
                else
                    new ListPhotoTask().execute("flickr.photos.search", "61d8670199834d7d7d48bcf570071a7a", "papillon", "", "json", "1");
            }
        });
    }

    class ListPhotoTask extends AsyncTask<String,Void,FlickSearch> {
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
                    urlPhoto = "https://farm" + photo.getFarm() + ".staticflickr.com/" + photo.getServer() + "/" + photo.getId() + "_" + photo.getSecret() + ".jpg";
                    addImageToCard(urlPhoto);
                }
            }
        }
    }


    private void addImageToCard(String urlImage) {
        images.add(urlImage);
    }


    private void setupWindowAnimations() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Explode explode = new Explode();
            getWindow().setExitTransition(explode);
            Fade fade = new Fade();
            getWindow().setReenterTransition(fade);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return drawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Synchroniser le drawerToggle après la restauration via onRestoreInstanceState
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }
}
