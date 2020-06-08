import helper.FileHelper;
import helper.JSONParser;
import wsb.WSBComment;
import wsb.WSBPost;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static helper.FilePaths.*;


public class WSBScraper {

    private final static boolean deleteJsonFromDisk = true;

    public static void main(String... args) throws IOException, InterruptedException {

        System.out.println("Reading data...");
        String jsonText = FileHelper.generateJsonContent(pathToPythonScript, commandType, subredditName, filterBy, nrOfPosts, format);
        System.out.println("Parsing data...");
        ArrayList<WSBPost> wsbPosts = JSONParser.parseSubredditJson(jsonText);
        System.out.println("Generating comments data...");
        Path pathToDir = FileHelper.gatherCommentsFromPosts(wsbPosts, pathToPythonScript, nrOfComments, format);

        //TODO Move this from main
        ArrayList<ArrayList<WSBComment>> wsbComments = new ArrayList<>();
        if (pathToDir.toFile().isDirectory()) {
            List<Path> files = new ArrayList<>();
            try (Stream<Path> paths = Files.walk(Paths.get(String.valueOf(pathToDir)))) {
                files =  paths
                        .filter(Files::isRegularFile)
                        .collect(Collectors.toList());
            } catch (IOException e) {
                e.printStackTrace();
            }
            for (Path path: files) {

                wsbComments.add(JSONParser.parseCommentJson(Files.readString(path)));
            }
        }
    }
}
