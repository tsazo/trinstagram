package com.codepath.tsazo.trinstagram.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.tsazo.trinstagram.Post;
import com.codepath.tsazo.trinstagram.PostsAdapter;
import com.codepath.tsazo.trinstagram.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostsFragment extends Fragment {

    public static final String TAG = "PostsFragment";
    private RecyclerView recyclerViewPosts;
    protected PostsAdapter adapter;
    protected List<Post> allPosts;

    public PostsFragment() {
        // Required empty public constructor
    }

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_posts, container, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Setup any handles to view objects here
        recyclerViewPosts = view.findViewById(R.id.recyclerViewPosts);

        allPosts = new ArrayList<>();
        adapter = new PostsAdapter(getContext(), allPosts);

        recyclerViewPosts.setAdapter(adapter);
        recyclerViewPosts.setLayoutManager(new LinearLayoutManager(getContext()));

        queryPosts();
    }

    protected void queryPosts() {
        // Specify which class to query
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.setLimit(20);
        query.addDescendingOrder(Post.KEY_CREATED_AT);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if(e != null){
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }

                for(Post post: posts) {
                    Log.i(TAG, "Post: " + post.getDescription() + ", username: " + post.getUser().getUsername());
                }

                allPosts.addAll(posts);
                adapter.notifyDataSetChanged();
            }
        });
    }
}