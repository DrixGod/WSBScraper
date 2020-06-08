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

    // Change path to file on your environment
    private static final String pathToFile = "E:\\dev\\Projects\\WSB-Scraper\\";
    private static File file = null;
    private static final List<String> specialCharacters = Arrays.asList("?", "%", "/", ":");

    public static String generateJsonContent(String pathToPythonScript, String commandType, String subredditName, String filterBy, int nrOfPosts, String format) throws IOException {
        try {
            String command = "python " + pathToPythonScript + " -" + commandType + " " + subredditName + " " + filterBy + " " + nrOfPosts + " --" + format;
            System.out.println("Executing command: " + command);
            Process p = Runtime.getRuntime().exec(command);

            file = new File(pathToFile + subredditName + ".json");

            // Give the script 5 seconds to create the file.
            //Thread.sleep(5000);

            // Check if the file exists on disk, if not wait until the file gets created.
            while(!file.exists()) {
                System.out.println("File " + file + " doesn't exist yet, waiting for it to be generated...");
                Thread.sleep(5000);
            }

        } catch (IOException | InterruptedException e) {
            System.out.println("exception happened - here's what I know: ");
            e.printStackTrace();
            System.exit(-1);
        }

        return Files.readString(file.toPath());
    }

    public static File getFile() {
        return file;
    }

    public static List<String> gatherCommentsFromPosts(ArrayList<WSBPosts> wsbPosts, String pathToPythonScript, int nrOfComments, String format) throws IOException {
        List<String> wsbCommentJsons = new ArrayList<>();
        for (WSBPosts wsbPost : wsbPosts) {
            if (wsbPost.getCommentCount() < 1000) {
                try {
                    System.out.println("Post: " + wsbPost.getTitle());

                    // Call the python script with the -c (comments) argument and the post URL to generate a .json file containing the comments
                    String command = "python " + pathToPythonScript + " -c " + wsbPost.getURl() + " " + nrOfComments + " --" + format;
                    Process p = Runtime.getRuntime().exec(command);

                    String fileTitle = replaceSpecialCharacters(wsbPost.getTitle());

                    // Check if the file exists on disk, if not wait until the file gets created.
                    File file = new File(pathToFile + fileTitle + ".json");
                    Thread.sleep(5000);

                    while (!file.exists()) {
                        System.out.println("File " + file + " doesn't exist yet, waiting for it to be generated...");
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
    private static String replaceSpecialCharacters(String postTitle) {
        for (String specialCharacter : specialCharacters) {
            if (postTitle.contains(specialCharacter)) {
                postTitle = postTitle.replace(specialCharacter, "_");
            }
        }
        return postTitle;
    }
}
