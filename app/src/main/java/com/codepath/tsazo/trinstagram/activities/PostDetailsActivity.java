package com.codepath.tsazo.trinstagram.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.tsazo.trinstagram.Post;
import com.codepath.tsazo.trinstagram.PostsAdapter;
import com.codepath.tsazo.trinstagram.R;
import com.codepath.tsazo.trinstagram.databinding.ActivityPostDetailsBinding;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.parse.ParseFile;

import org.parceler.Parcels;

public class PostDetailsActivity extends AppCompatActivity {

    private static final String TAG = "PostDetailsActivity";

    //Google places client
    private PlacesClient placesClient;
    private static final String API_KEY = "AIzaSyB33o9qfsYo0BoA_oBOVAxN4XmQaamWIv4";

    // the Post to display
    private Post post;

    // the view objects
    private TextView textViewUsername;
    private TextView textViewLocation;
    private TextView textViewDescription;
    private ImageView imageViewImage;
    private TextView textViewTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        textViewUsername = findViewById(R.id.textViewUsername);
        textViewLocation = findViewById(R.id.textViewLocation);
        imageViewImage = findViewById(R.id.imageViewImage);
        textViewDescription = findViewById(R.id.textViewDescription);
        textViewTime = findViewById(R.id.textViewTime);

        // Initialize Places.
        Places.initialize(getApplicationContext(), API_KEY);

        // Create a new Places client instance.
        placesClient = Places.createClient(this);
        Log.i(TAG, String.valueOf(placesClient));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        //placesClient.findCurrentPlace();

        // Unwrap the movie passed in via intent, using its simple name as a key
        post = (Post) Parcels.unwrap(getIntent().getParcelableExtra(Post.class.getSimpleName()));

        setValues();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));
        getSupportActionBar().setLogo(R.drawable.nav_logo_whiteout);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        return true;
    }

    // Method to set the values into the views
    private void setValues() {
        textViewDescription.setText(post.getDescription());

        // TODO get up a location value to a post object
        //textViewLocation.setText(post.getLocation);
        textViewUsername.setText(post.getUser().getUsername());
        textViewTime.setText(PostsAdapter.getRelativeTimeAgo(post.getCreatedAt()));
        ParseFile image = post.getImage();
        if(image != null)
            Glide.with(this).load(post.getImage().getUrl()).fitCenter().into(imageViewImage);
    }
}