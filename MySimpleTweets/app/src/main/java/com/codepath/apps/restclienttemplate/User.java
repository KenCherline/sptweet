package com.codepath.apps.restclienttemplate;

import com.facebook.stetho.inspector.jsonrpc.JsonRpcException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ResourceBundle;

/**
 * created by rhu on 5/23/17.
 */
public class User {
    // list the attributes
    public String name;
    public Long uid;
    public String screenName;
    public String profileImageUrl;

    // deserialize the JSON
    public static User fromJSON(JSONObject json) throws JSONException {
        User user = new User();
        // extract and fill the values
        user.name = json.getString("name");
        user.uid = json.getLong("id");
        user.screenName = json.getString("screen_name");
        user.profileImageUrl = json.getString("profile_image_url");
        return user;
    }





}
