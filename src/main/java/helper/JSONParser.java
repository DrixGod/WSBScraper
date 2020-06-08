package helper;

import wsb.WSBComment;
import wsb.WSBPost;
import org.json.*;

import java.util.ArrayList;

public class JSONParser {

    public static ArrayList<WSBPost> parseSubredditJson(String jsonString) {
        ArrayList<WSBPost> wsbList = new ArrayList<>();
        WSBPost wsb = null;
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
                wsb = new WSBPost(
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

    public static ArrayList<WSBComment> parseCommentJson(String jsonString) {
        ArrayList<WSBComment> wsbList = new ArrayList<>();
        WSBComment wsb = null;
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
                wsb = new WSBComment(
                        post.getString("Parent ID"),
                        post.getString("Comment ID"),
                        post.getString("Author"),
                        post.getString("Date Created"),
                        post.getInt("Upvotes"),
                        post.getString("Text"),
                        post.getBoolean("Edited?"),
                        post.getBoolean("Is Submitter?"),
                        post.getBoolean("Stickied?"));
                wsbList.add(wsb);
            }
        }

        return wsbList;
    }
}
