package helper;

import wsb.WSBPosts;
import org.json.*;

import java.util.ArrayList;

public class JSONParser {

    public static ArrayList<WSBPosts> parseJson(String jsonString) {
        ArrayList<WSBPosts> wsbList = new ArrayList<>();
        WSBPosts wsb = null;
        JSONObject jsonObject = new JSONObject(jsonString);
        ArrayList<JSONObject> posts = new ArrayList<>();
        JSONArray arr = null;
        int i = 1;

        // 25 posts on the frontpage
        while (i < 25) {
            JSONObject post = jsonObject.getJSONObject("Post " + i);
            posts.add(post);
            ++i;
        }

        // convert JSONObject to WSB
        for(JSONObject post : posts) {
            // skip the daily/weekend discussion thread because it has too many comments
            if (!post.getString("Title").contains("Discussion Thread")) {
                // TODO add isNull check for every element
                wsb = new WSBPosts(
                        post.getString("Title"),
                        post.isNull("Flair")  ? "" : post.getString("Flair"),
                        post.getString("Date Created"),
                        post.getInt("Upvotes"),
                        post.getDouble("Upvote Ratio"),
                        post.getString("ID"),
                        post.getString("Edited?"),
                        post.getBoolean("Is Locked?"),
                        post.getBoolean("NSFW?"),
                        post.getBoolean("Is Spoiler?"),
                        post.getBoolean("Stickied?"),
                        post.getString("URL"),
                        post.getInt("Comment Count"),
                        post.getString("Text"));
                wsbList.add(wsb);
            }
        }

        return wsbList;
    }
}
