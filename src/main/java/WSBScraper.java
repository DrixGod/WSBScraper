import helper.FileHelper;
import helper.JSONParser;
import helper.SentimentAnalysisHelper;
import wsb.WSBComment;
import wsb.WSBPost;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

import static helper.Generics.*;

public class WSBScraper {

    public static void main(String... args) throws IOException, InterruptedException {
        System.out.println("Preparing environment");
        FileHelper.createAnalyseDirectory();
        System.out.println("Reading posts data...");
        String jsonText = FileHelper.generateJsonContent(pathToPythonScript, commandType, subredditName, filterBy, nrOfPosts, format);
        System.out.println("Parsing posts data...");
        ArrayList<WSBPost> wsbPosts = JSONParser.parseSubredditJson(jsonText);
        System.out.println("Reading comments data...");
        Path pathToDir = FileHelper.gatherCommentsFromPosts(wsbPosts, pathToPythonScript, nrOfComments, format);
        System.out.println("Parsing comments data...");
        ArrayList<ArrayList<WSBComment>> wsbComments = FileHelper.gatherJsonFromComments(pathToDir);
        System.out.println("Generating results data...");
        FileHelper.generateResults(wsbPosts, wsbComments);
        System.out.println("Performing sentiment analysis...");
        SentimentAnalysisHelper.performSentimentAnalysis();
    }
}
