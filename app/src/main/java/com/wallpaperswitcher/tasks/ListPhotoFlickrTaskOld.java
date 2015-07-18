package com.wallpaperswitcher.tasks;

import android.os.AsyncTask;

import com.wallpaperswitcher.flickrService.FlickrService;
import com.wallpaperswitcher.flickrService.model.FlickSearch;
import com.wallpaperswitcher.flickrService.model.Photo;

import retrofit.RestAdapter;
import retrofit.android.AndroidLog;

public class ListPhotoFlickrTaskOld extends AsyncTask<String,Void,FlickSearch> {

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
                //MainFragment.addImageToCard(urlPhoto);
            }
        }
    }
}