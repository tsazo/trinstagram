package com.codepath.tsazo.trinstagram;

import android.content.Context;
import android.content.Intent;
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

import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    private static final String TAG = "PostsAdapter";
    private Context context;
    private List<Post> posts;

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
        private ImageView imageViewImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewUsername = itemView.findViewById(R.id.textViewUsername);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            imageViewImage = itemView.findViewById(R.id.imageViewImage);

            itemView.setOnClickListener(this);
        }

        public void bind(final Post post) {
            // Bind the post data to the view elements
            textViewDescription.setText(post.getDescription());
            textViewUsername.setText(post.getUser().getUsername());
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
