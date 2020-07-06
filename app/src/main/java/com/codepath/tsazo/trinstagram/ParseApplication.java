package com.codepath.tsazo.trinstagram;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Register your parse models
        ParseObject.registerSubclass(Post.class);
        // Add your initialization code here
        //Parse.initialize(this, "7zBztvyG4hYQ9XghgfqYxfRcL3SMBYWAj0GUL", "iZWhgJRu6yKm3iNMbTaguLcNCV3qedijWL");

        // set applicationId, and server server based on the values in the Heroku settings.
        // clientKey is not needed unless explicitly configured
        // any network interceptors must be added with the Configuration Builder given this syntax
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("trinity-insta") // should correspond to APP_ID env variable
                .clientKey("CodepathFacebookInstagram")  // set explicitly unless clientKey is explicitly configured on Parse server
                .server("https://trinity-insta.herokuapp.com/parse/").build());
    }
}
