package com.codepath.tsazo.trinstagram.fragments;

import android.app.Application;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.tsazo.trinstagram.EndlessRecyclerViewScrollListener;
import com.codepath.tsazo.trinstagram.Post;
import com.codepath.tsazo.trinstagram.PostsAdapter;
import com.codepath.tsazo.trinstagram.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostsFragment extends Fragment {

    public static final String TAG = "PostsFragment";
    private static final int POST_LIMIT = 2;
    private EndlessRecyclerViewScrollListener scrollListener;
    private RecyclerView recyclerViewPosts;
    protected PostsAdapter adapter;
    protected List<Post> allPosts;
    private SwipeRefreshLayout swipeContainer;

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

        // RecyclerView setup: layout manager and the adapter
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerViewPosts.setLayoutManager(layoutManager);
        recyclerViewPosts.setAdapter(adapter);

        // Scroll listener for endless scrolling
        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Log.i(TAG, "onLoadMore: " + page);

                //  --> Send the request including an offset value (i.e `page`) as a query parameter.
                loadNextDataFromApi(page);
            }
        };

        // Adds the endless scroll listener to RecyclerView
        recyclerViewPosts.addOnScrollListener(scrollListener);

        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                Log.i(TAG, "Fetching new data!");
                fetchTimelineAsync(0);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        queryPosts();
    }

    // Append the next page of data into the adapter
    // This method sends out a network request and appends new data items to your adapter.
    public void loadNextDataFromApi(int page) {
        // Send an API request to retrieve appropriate paginated data
        Log.i(TAG, "Loading next data");
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.setLimit(POST_LIMIT);
        query.addDescendingOrder(Post.KEY_CREATED_AT);
        query.whereLessThan(Post.KEY_CREATED_AT, allPosts.get(POST_LIMIT * (page) - 1).getCreatedAt());
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }
                for (Post post : posts) {
                    Log.i(TAG, "Post: " + post.getDescription() + " Username: " + post.getUser().getUsername());
                }
                allPosts.addAll(posts);
                Log.i(TAG, "" + allPosts.size());
                adapter.notifyDataSetChanged();
                Log.i(TAG, "Query older posts finished");
            }
        });
    }

    protected void queryPosts() {
        // Specify which class to query
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.setLimit(POST_LIMIT);
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
                scrollListener.resetState();
            }
        });
    }

    // Send the network request to fetch the updated data from the Parse Database
    public void fetchTimelineAsync(int page) {
        adapter.clear();
        queryPosts();
        swipeContainer.setRefreshing(false);
    }
}