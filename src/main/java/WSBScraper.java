import helper.FileHelper;
import helper.JSONParser;
import wsb.WSBPost;

import java.io.IOException;
import java.util.ArrayList;

import static helper.FilePaths.*;


public class WSBScraper {

    private final static boolean deleteJsonFromDisk = true;

    public static void main(String... args) throws IOException, InterruptedException {
        System.out.println("Reading data...");
        String jsonText = FileHelper.generateJsonContent(pathToPythonScript, commandType, subredditName, filterBy, nrOfPosts, format);
        System.out.println("Parsing data...");
        ArrayList<WSBPost> wsbposts = JSONParser.parseSubredditJson(jsonText);
        System.out.println("Generating comments data...");
        FileHelper.gatherCommentsFromPosts(wsbposts, pathToPythonScript, nrOfComments, format);
    }
}
