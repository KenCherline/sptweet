package com.codepath.apps.restclienttemplate;

import org.json.JSONException;
import org.json.JSONObject;

public class Tweet {
    // List out the attributes
    public String body;
    public  Long uid; // database ID the tweet
    public User user;
    public String creadedAt;

    // deserialized the JSON
    public static Tweet fromJSON(JSONObject  jsonObject) throws JSONException{
        Tweet tweet = new Tweet();
        // extract the values from JSON
        tweet.body = jsonObject.getString("text");
        tweet.uid = jsonObject.getLong("id");
        tweet.creadedAt = jsonObject.getString("created_at");
        tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
        return tweet;

    }
}
