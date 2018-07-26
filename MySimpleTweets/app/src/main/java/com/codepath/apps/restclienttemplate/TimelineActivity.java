package com.codepath.apps.restclienttemplate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static com.loopj.android.http.AsyncHttpClient.log;

public class TimelineActivity extends AppCompatActivity {
    private TwitterClient client;
    TweetAdapter tweetAdapter;
    ArrayList<Tweet> tweets;
    RecyclerView rvTweets;
    EndlessRecyclerViewScrollListener endlessRvs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        client = TwitterApp.getRestClient(getBaseContext());
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        // find the Recyclerview
        rvTweets = (RecyclerView) findViewById(R.id.rvTweet);
        // init the arrayList(data source)
        tweets = new ArrayList<>();
        // construct the adapter from this datasource
        tweetAdapter = new TweetAdapter(tweets);
        //RecyclerView setup (layout manager, use adapter)
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvTweets.setLayoutManager(linearLayoutManager);
        //  the set adapter
        rvTweets.setAdapter(tweetAdapter);


        endlessRvs = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            //            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadNextDataFromApi(page);
            }
        } ;
//          Add the scrollListener to the recyclerView
        rvTweets.addOnScrollListener(endlessRvs);
//        populateTimeLineActivity();
        popupLateTimeLine();

    }

    private void popupLateTimeLine() {
        client.getHomeTimeLine(new JsonHttpResponseHandler() {
                                   @Override
                                   public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                       Log.d("TwitterClient", response.toString());

                                   }

                                   @Override
                                   public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                                       Log.d("TwitterClient" ,response.toString());
                                       // iterate through the JSON array
                                       // for each entry ,deserialise the JSON object
                                       for (int i = 0; i < response.length(); i++) {
                                           // convert each object to a Tweet model
                                           // add that Tweet model to our data source
                                           // notify  the adapter that we've an item
                                           try {

                                               Tweet tweet = Tweet.fromJSON(response.getJSONObject(i));
                                               tweets.add(tweet);
                                               tweetAdapter.notifyItemInserted(tweets.size() - 1);
                                           } catch (JSONException e) {
                                               e.printStackTrace();
                                           }
                                       }


                                   }

                                   @Override
                                   public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                       Log.d("TwitterClient", responseString);
                                       throwable.printStackTrace();
                                   }

                                   @Override
                                   public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                       Log.d("TwitterClient", errorResponse.toString());
                                       throwable.printStackTrace();

                                   }

                                   @Override
                                   public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                                       log.d("TwitterClient", errorResponse.toString());
                                       throwable.printStackTrace();

                                   }


                                    }
        );
    }


    //Loading the data on scroll indefinitely
    public void loadNextDataFromApi(int offset) {
        Toast.makeText(this, "more", Toast.LENGTH_SHORT).show();
        client.Connect(offset , new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                //iterate through the JSON array
                // For each entry, deserialize the json
                for (int i = 0; i < response.length(); i++) {
                    // convert each object ti a Tweet model
                    // Add that tweet model to our data source
                    //notify the adapter that we added an item
                    try {
                        Tweet tweet = Tweet.fromJSON(response.getJSONObject(i));
                        int position = tweets.size();
                        tweets.add(tweet);
                        tweetAdapter.notifyItemRangeInserted(position, 1);
                        //tweetAdapter.notifyItemInserted(tweets.size() - 1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("TwitterClient",responseString);
                throwable.printStackTrace();
            }
        } );

    }


}
