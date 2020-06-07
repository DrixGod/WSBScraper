package helper;

import wsb.WSBPosts;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class contains hardcoded paths, am autistic and will change them later.
 */

public class FileHelper {

    public static String generateJsonContent() throws IOException {
        File file = null;
        String s = null;

        try {
            // Get the first 25 posts from /r/wallstreetbets
            String command = "python E:\\dev\\universalredditscraper\\Universal-Reddit-Scraper\\scraper.py -r wallstreetbets h 25 --json";
            Process p = Runtime.getRuntime().exec(command);

            // Check if the file exists on disk, if not wait until the file gets created.
            file = new File("E:\\dev\\Projects\\WSB-Scraper\\wallstreetbets.json");
            while(!file.exists()) {
                System.out.println("File doesn't exist yet!");
                Thread.sleep(5000);
            }

        } catch (IOException | InterruptedException e) {
            System.out.println("exception happened - here's what I know: ");
            e.printStackTrace();
            System.exit(-1);
        }

        return Files.readString(file.toPath());
    }

    public static List<String> gatherCommentsFromPosts(ArrayList<WSBPosts> wsbPosts) throws IOException {
        String s = null;
        List<String> wsbCommentJsons = new ArrayList<>();
        for (WSBPosts wsbPost : wsbPosts) {
            if (wsbPost.getCommentCount() < 1000) {
                try {
                    System.out.println("Post: " + wsbPost.getTitle());
                    // Call the python script with the -c (comments) argument and the post URL to generate a .json file containing the comments
                    String command = "python E:\\dev\\universalredditscraper\\Universal-Reddit-Scraper\\scraper.py -c " + wsbPost.getURl() + " 10 --json";
                    Process p = Runtime.getRuntime().exec(command);

                    // Check if the file exists on disk, if not wait until the file gets created.
                    String fileTitle = replaceSpecialCharacters(wsbPost.getTitle());
                    File file = new File("E:\\dev\\Projects\\WSB-Scraper\\" + fileTitle + ".json");
                    while (!file.exists()) {
                        System.out.println("File " + fileTitle + " doesn't exist yet");
                        Thread.sleep(5000);
                    }
                    wsbCommentJsons.add(file.getAbsolutePath());
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                    System.exit(-1);
                }
            }
            else {
                System.out.println("Skipping post " + wsbPost.getTitle() + " because it has " + wsbPost.getCommentCount() + " comments.");
            }
        }
        System.out.println("Finished gathering comments in JSON format.");
        return wsbCommentJsons;
    }

    // Files generated on disk can't have special characters, they get replaced by '_'
    private static String replaceSpecialCharacters(String input) {
        String output = input;
        if (input.contains("%")) {
            output = input.replace("%","_");
            return output;
        }
        if (input.contains("?")) {
            output = input.replace("?", "_");
            return output;
        }
        if (input.contains("/")) {
            output = input.replace("/", "_");
            return output;
        }
        return output;
    }
}
