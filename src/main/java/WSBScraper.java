import helper.FileHelper;
import helper.JSONParser;
import wsb.WSBPosts;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;



public class WSBScraper {

    /**
     * Change these in your environment to use the app.
     * For documentation on how to use the python script: https://github.com/JosephLai241/Universal-Reddit-Scraper/blob/master/README.md
     */
    private final static String pathToPythonScript = "E:\\dev\\universalredditscraper\\Universal-Reddit-Scraper\\scraper.py";
    private final static String commandType = "r";
    private final static String subredditName = "wallstreetbets";
    private final static String filterBy = "h";
    private final static int nrOfPosts = 25;
    private final static int nrOfComments = 10;
    private final static String format = "json";

    private final static boolean deleteJsonFromDisk = true;

    public static void main(String... args) throws IOException, InterruptedException {
        System.out.println("Reading data...");
        String jsonText = FileHelper.generateJsonContent(pathToPythonScript, commandType, subredditName, filterBy, nrOfPosts, format);
        System.out.println("Parsing data...");
        ArrayList<WSBPosts> wsbposts = JSONParser.parseJson(jsonText);
        System.out.println("Generating comments data...");
        FileHelper.gatherCommentsFromPosts(wsbposts, pathToPythonScript, nrOfComments, format);

        // Delete json from disk when program ends
        if (deleteJsonFromDisk) {
            File file = FileHelper.getFile();
            if (file != null && file.exists()) {
                file.delete();
                System.out.println("File deleted successfully from disk.");
            }
        }
    }
}
