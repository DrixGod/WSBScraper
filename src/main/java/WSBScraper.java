import helper.FileHelper;
import helper.JSONParser;
import wsb.WSBPosts;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import static helper.FilePaths.*;


public class WSBScraper {

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
