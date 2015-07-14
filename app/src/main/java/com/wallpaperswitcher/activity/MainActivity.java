package com.wallpaperswitcher.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.net.wifi.ScanResult;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Fade;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.github.pwittchen.networkevents.library.ConnectivityStatus;
import com.github.pwittchen.networkevents.library.NetworkEvents;
import com.github.pwittchen.networkevents.library.NetworkHelper;
import com.github.pwittchen.networkevents.library.event.ConnectivityChanged;
import com.github.pwittchen.networkevents.library.event.WifiSignalStrengthChanged;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.wallpaperswitcher.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    // @Bind permet d'associer un composant à son id (librairie ButterKnife)
    // Permet de ne pas l'associer dans le oncreate
    @Bind(R.id.navigationView)
    NavigationView navigationView;

    @Bind(R.id.drawerLayout)
    DrawerLayout drawerLayout;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    private ActionBarDrawerToggle drawerToggle;

    private Bus bus;
    private NetworkEvents networkEvents;

    private ConnectivityStatus connectivityStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        bus = new Bus();
        networkEvents = new NetworkEvents(this, bus);

        if (savedInstanceState == null) {
            displayFragment(0);
        }

        /*
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
        */

        /*buttonRecherche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTextRecherche.getText().toString() != null)
                    new ListPhotoFlickrTask().execute("flickr.photos.search", "61d8670199834d7d7d48bcf570071a7a", editTextRecherche.getText().toString(), "", "json", "1");
                else
                    new ListPhotoFlickrTask().execute("flickr.photos.search", "61d8670199834d7d7d48bcf570071a7a", "papillon", "", "json", "1");
            }
        });*/
    }

    private void init(){
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

        initDrawerLayout();
    }

    private void initDrawerLayout(){
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // Mon action
                        drawerLayout.closeDrawers();
                        displayFragment(menuItem.getItemId());
                        return true;
                    }
                });
    }

    /**
     * Diplaying fragment view for selected nav drawer list item
     * */
    private void displayFragment(int id) {
        // Update the main content by replacing fragments
        Fragment fragment = null;
        switch (id) {
            case 0 :
                fragment = new MainFragment();
                break;
            case R.id.nav_flickr :
                fragment = new FlickrFragment();
                break;
            case R.id.nav_unsplash :
                fragment = new UnsplashFragment();
                break;
            case R.id.nav_settings :
                fragment = new SettingsFragment();
                break;
            case R.id.nav_about :
                fragment = new AboutFragment();
                break;
            default :
                break;
        }

        if (fragment != null) {
            // Success in creating fragment
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).addToBackStack("tag").commit();

        } else {
            // Error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }
    }
/*
    private void addImageToCard(String urlImage) {
        images.add(urlImage);
    }
*/
    private void setupWindowAnimations() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Explode explode = new Explode();
            getWindow().setExitTransition(explode);
            Fade fade = new Fade();
            getWindow().setReenterTransition(fade);
        }
    }

    public ConnectivityStatus getConnectivityStatus() {
        return connectivityStatus;
    }

    @Override
    protected void onResume() {
        super.onResume();
        bus.register(this);
        networkEvents.register();
    }

    @Override
    protected void onPause() {
        super.onPause();
        bus.unregister(this);
        networkEvents.unregister();
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            this.finish();
        } else {
            getFragmentManager().popBackStack();
        }
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

    @Subscribe
    public void onConnectivityChanged(ConnectivityChanged event) {
        if(event.getConnectivityStatus().toString().equalsIgnoreCase(String.valueOf(ConnectivityStatus.WIFI_CONNECTED_HAS_INTERNET)) ||
                event.getConnectivityStatus().toString().equalsIgnoreCase(String.valueOf(ConnectivityStatus.MOBILE_CONNECTED))) {
            Snackbar.make(drawerLayout, "Vous êtes bien connecté à Internet", Snackbar.LENGTH_LONG).setAction("Fermer", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            }).show();
        } else {
            Snackbar.make(drawerLayout, "Vous n'êtes pas connecté à Internet", Snackbar.LENGTH_LONG).setAction("Fermer", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            }).show();
        }
    }
}
