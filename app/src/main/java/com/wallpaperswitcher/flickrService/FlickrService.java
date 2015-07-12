package com.wallpaperswitcher.flickrService;

import com.wallpaperswitcher.flickrService.model.FlickSearch;

import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by FlorianXPS on 08/07/2015.
 */
public interface FlickrService {
    // URL
    // https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=0be06ecdf3fa1ac784e8fd10c279790c&text=papillon&format=json&nojsoncallback=1

    public static final String ENDPOINT = "https://api.flickr.com";

    @GET("/services/rest/")
    FlickSearch getPhotos(@Query("method") String method,
                          @Query("api_key") String api_key,
                          @Query("tags") String tags,
                          @Query("text") String text,
                          @Query("format") String format,
                          @Query("nojsoncallback") String nojsoncallback);
}
