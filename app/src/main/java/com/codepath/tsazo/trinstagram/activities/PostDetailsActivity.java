package com.codepath.tsazo.trinstagram.activities;

        import androidx.appcompat.app.ActionBar;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.fragment.app.FragmentManager;

        import android.content.Intent;
        import android.graphics.drawable.ColorDrawable;
        import android.os.Bundle;
        import android.view.Menu;
        import android.view.View;
        import android.widget.ImageView;
        import android.widget.TextView;

        import com.bumptech.glide.Glide;
        import com.codepath.tsazo.trinstagram.Post;
        import com.codepath.tsazo.trinstagram.PostsAdapter;
        import com.codepath.tsazo.trinstagram.R;
        import com.codepath.tsazo.trinstagram.databinding.ActivityPostDetailsBinding;
        import com.parse.ParseFile;

        import org.parceler.Parcels;

public class PostDetailsActivity extends AppCompatActivity {

    private static final String TAG = "PostDetailsActivity";
    private static ActivityPostDetailsBinding binding;
    final FragmentManager fragmentManager = getSupportFragmentManager();

    // the Post to display
    private Post post;

    // the view objects
    private TextView textViewUsername;
    private TextView textViewDescription;
    private ImageView imageViewImage;
    private TextView textViewTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        textViewUsername = findViewById(R.id.textViewUsername);
        imageViewImage = findViewById(R.id.imageViewImage);
        textViewDescription = findViewById(R.id.textViewDescription);
        textViewTime = findViewById(R.id.textViewTime);

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
        textViewUsername.setText(post.getUser().getUsername());
        textViewTime.setText(PostsAdapter.getRelativeTimeAgo(post.getCreatedAt()));
        ParseFile image = post.getImage();
        if(image != null)
            Glide.with(this).load(post.getImage().getUrl()).fitCenter().into(imageViewImage);
    }
}