package com.codepath.tsazo.trinstagram;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;

import org.w3c.dom.Text;

import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

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
    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewUsername;
        private TextView textViewDescription;
        private ImageView imageViewImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewUsername = itemView.findViewById(R.id.textViewUsername);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            imageViewImage = itemView.findViewById(R.id.imageViewImage);
        }

        public void bind(Post post) {
            // Bind the post data to the view elements
            textViewDescription.setText(post.getDescription());
            textViewUsername.setText(post.getUser().getUsername());
            ParseFile image = post.getImage();
            if(image != null)
                Glide.with(context).load(post.getImage().getUrl()).fitCenter().into(imageViewImage);
        }
    }
}
