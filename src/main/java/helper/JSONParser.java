package helper;

import wsb.WSBComment;
import wsb.WSBPost;
import org.json.*;

import java.util.*;

import static helper.Generics.nrOfPosts;

public class JSONParser {

    public static ArrayList<WSBPost> parseSubredditJson(String jsonString) {
        ArrayList<WSBPost> wsbList = new ArrayList<>();
        ArrayList<JSONObject> posts = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(jsonString);
        WSBPost wsb = null;

        int i = 1;
        while (i < nrOfPosts) {
            JSONObject post = jsonObject.getJSONObject("Post " + i);
            posts.add(post);
            ++i;
        }

        // convert JSONObject to WSB
        for(JSONObject post : posts) {
            wsb = new WSBPost(
                    post.isNull("Title") ? "" : post.getString("Title"),
                    post.isNull("Flair") ? "" : post.getString("Flair"),
                    post.isNull("Date Created\"")  ? "" : post.getString("Date Created"),
                    post.isNull("Upvotes") ? 0 : post.getInt("Upvotes"),
                    post.isNull("Upvote Ratio") ? 0 : post.getDouble("Upvote Ratio"),
                    post.isNull("ID") ? "" : post.getString("ID"),
                    post.isNull("Edited?") ? "" : post.getString("Edited?"),
            !post.isNull("Is Locked?") && post.getBoolean("Is Locked?"),
             !post.isNull("NSFW?") && post.getBoolean("NSFW?"),
            !post.isNull("Is Spoiler?") && post.getBoolean("Is Spoiler?"),
            !post.isNull("Stickied?") && post.getBoolean("Stickied?"),
                    post.getString("URL"),
                    post.isNull("Comment Count") ? 0 : post.getInt("Comment Count"),
                    post.isNull("Text") ? "" : post.getString("Text"));
            wsbList.add(wsb);
        }

        return wsbList;
    }

    public static ArrayList<WSBComment> parseCommentJson(String jsonString) {
        ArrayList<WSBComment> wsbList = new ArrayList<>();
        WSBComment wsb = null;
        JSONObject jsonObject = new JSONObject(jsonString);
        ArrayList<JSONArray> comments = new ArrayList<>();

        for (Map.Entry<String, Object> stringObjectEntry : jsonObject.toMap().entrySet()) {
            String value = ((Map.Entry) stringObjectEntry).getValue().toString();
            comments.add(jsonObject.getJSONArray(getCommentID(value)));
        }

        // convert JSONArray to WSB
        for(JSONArray comment: comments) {
            JSONArray jr1 = comment.getJSONArray(0);
            ArrayList jr2 = (ArrayList) jr1.toList();
            HashMap hashMap = (HashMap) jr2.get(0);
            Iterator mapIt = hashMap.entrySet().iterator();
            List<Object> myValues = new ArrayList<>();
            while (mapIt.hasNext()) {
                Map.Entry pair = (Map.Entry) mapIt.next();
                myValues.add(pair.getValue());
            }
            wsb = new WSBComment(
                    (String) myValues.get(0),
                    (String) myValues.get(1),
                    (String) myValues.get(3),
                    (String) myValues.get(7),
                    (int) myValues.get(8),
                    (String) myValues.get(4),
                    !myValues.get(2).toString().equals("false"),
                    (boolean) myValues.get(5),
                    (boolean) myValues.get(6)
            );
            wsbList.add(wsb);
        }
        return wsbList;
    }

    // Split the given string and get the ID
    private static String getCommentID(String str) {
        String[] arr = str.split("Comment ID=");
        return (arr[1].split(","))[0];
    }
}
