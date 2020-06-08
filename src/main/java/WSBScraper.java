import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import helper.FileHelper;
import helper.JSONParser;
import nlp.Pipeline;
import nlp.SentimentAnalysis;
import wsb.WSBComment;
import wsb.WSBPost;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

import static helper.FilePaths.*;


public class WSBScraper {

    public static void main(String... args) throws IOException, InterruptedException {
        System.out.println("Preparing environment");
        FileHelper.createAnalyseDirectory();
        System.out.println("Reading data...");
        String jsonText = FileHelper.generateJsonContent(pathToPythonScript, commandType, subredditName, filterBy, nrOfPosts, format);
        System.out.println("Parsing data...");
        ArrayList<WSBPost> wsbPosts = JSONParser.parseSubredditJson(jsonText);
        System.out.println("Generating comments data...");
        Path pathToDir = FileHelper.gatherCommentsFromPosts(wsbPosts, pathToPythonScript, nrOfComments, format);
        System.out.println("Parsing comments...");
        ArrayList<ArrayList<WSBComment>> wsbComments = FileHelper.gatherJsonFromComments(pathToDir);
        FileHelper.generateResults(wsbPosts, wsbComments);
        StanfordCoreNLP stanfordCoreNLP = Pipeline.getPipeline();
        SentimentAnalysis sentimentAnalysis = new SentimentAnalysis(stanfordCoreNLP);
        File titles = new File(pathToProject  + directoryForAnalyse + "\\results_titles.txt");
        sentimentAnalysis.performSentimentAnalysis(titles);
        File comments = new File(pathToProject  + directoryForAnalyse + "\\results_comments.txt");
        sentimentAnalysis.performSentimentAnalysis(comments);
    }
}
