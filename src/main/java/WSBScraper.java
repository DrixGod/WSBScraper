import helper.FileHelper;
import helper.JSONParser;
import wsb.WSBComment;
import wsb.WSBPost;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

import static helper.FilePaths.*;


public class WSBScraper {

    public static void main(String... args) throws IOException, InterruptedException {
        System.out.println("Reading data...");
        String jsonText = FileHelper.generateJsonContent(pathToPythonScript, commandType, subredditName, filterBy, nrOfPosts, format);
        System.out.println("Parsing data...");
        ArrayList<WSBPost> wsbPosts = JSONParser.parseSubredditJson(jsonText);
        System.out.println("Generating comments data...");
        Path pathToDir = FileHelper.gatherCommentsFromPosts(wsbPosts, pathToPythonScript, nrOfComments, format);
        System.out.println("Parsing comments...");
        ArrayList<ArrayList<WSBComment>> wsbComments = FileHelper.gatherJsonFromComments(pathToDir);
    }
}
