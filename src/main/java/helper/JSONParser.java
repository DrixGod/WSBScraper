package helper;

import wsb.WSBComment;
import wsb.WSBPost;
import org.json.*;

import java.lang.reflect.Array;
import java.util.*;

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
        ArrayList<JSONArray> comments = new ArrayList<>();

        for (Map.Entry<String, Object> stringObjectEntry : jsonObject.toMap().entrySet()) {
            String value = ((Map.Entry) stringObjectEntry).getValue().toString();
            String[] helperArr = value.split("Comment ID=");
            String[] myValue = helperArr[1].split(",");
            comments.add(jsonObject.getJSONArray(myValue[0]));
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
}
