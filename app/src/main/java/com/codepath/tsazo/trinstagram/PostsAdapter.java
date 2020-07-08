package com.codepath.tsazo.trinstagram;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.tsazo.trinstagram.activities.PostDetailsActivity;
import com.parse.ParseFile;

import org.parceler.Parcels;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    private static final String TAG = "PostsAdapter";
    private Context context;
    private List<Post> posts;

    // TIME CONSTANTS
    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    public PostsAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    // Method to connect the item_post to the PostsFragment ViewHolder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    // Bind the item_post to the RecyclerView
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView textViewUsername;
        private TextView textViewDescription;
        private TextView textViewTime;
        private ImageView imageViewImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewUsername = itemView.findViewById(R.id.textViewUsername);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            imageViewImage = itemView.findViewById(R.id.imageViewImage);
            textViewTime = itemView.findViewById(R.id.textViewTime);

            itemView.setOnClickListener(this);
        }

        public void bind(final Post post) {
            // Bind the post data to the view elements
            textViewDescription.setText(post.getDescription());
            textViewUsername.setText(post.getUser().getUsername());
            textViewTime.setText(getRelativeTimeAgo(post.getCreatedAt()));
            ParseFile image = post.getImage();
            if(image != null)
                Glide.with(context).load(post.getImage().getUrl()).fitCenter().into(imageViewImage);
        }

        @Override
        public void onClick(View v) {
            Log.i(TAG, "Post clicked");
            // Gets item position
            int position = getAdapterPosition();

            // Make sure the position is valid i.e actually exists in the view
            if(position != RecyclerView.NO_POSITION) {
                // Get the post at the position, this won't work if the class is static
                Post post = posts.get(position);

                // Create intent for the new activity
                Intent intent = new Intent(context, PostDetailsActivity.class);

                // Serialize the post using the parceler, use its short name as a key
                intent.putExtra(Post.class.getSimpleName(), Parcels.wrap(post));

                // Show the activity
                context.startActivity(intent);
            }
        }
    }

    // Returns relative time a post was created
    public static String getRelativeTimeAgo(Date date) {
        String format = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(format, Locale.ENGLISH);
        sf.setLenient(true);

        String timespan = "";
        long dateMillis = date.getTime();
        timespan = DateUtils.getRelativeTimeSpanString(dateMillis, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();

        return timespan;
    }

    // Clean all elements of the recycler
    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Post> list) {
        posts.addAll(list);
        notifyDataSetChanged();
    }
}
