package helper;

import wsb.WSBPosts;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import static helper.FilePaths.*;

public class FileHelper {

    public static String generateJsonContent(String pathToPythonScript, String commandType, String subredditName, String filterBy, int nrOfPosts, String format) throws IOException, InterruptedException {
        try {
            String command = "python " + pathToPythonScript + " -" + commandType + " " + subredditName + " " + filterBy + " " + nrOfPosts + " --" + format;
            System.out.println("Executing command: " + command);
            Process p = Runtime.getRuntime().exec(command);

            file = new File(pathToProject +  subredditName + ".json");

            // Give the script 5 seconds to create the file.
            Thread.sleep(5000);

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

        // Create directory to move subreddit json to
        File dir = createDirectory(directoryForSubreddit);

        // Move the json file to the created directory
        subredditFile = moveFilesToDirectory(dir, file);

        return Files.readString(subredditFile.toPath());
    }

    public static File getFile() {
        return subredditFile;
    }

    public static void gatherCommentsFromPosts(ArrayList<WSBPosts> wsbPosts, String pathToPythonScript, int nrOfComments, String format) throws IOException {
        List<String> wsbCommentJsons = new ArrayList<>();
        File dir = createDirectory(directoryForComments);
        for (WSBPosts wsbPost : wsbPosts) {
            if (wsbPost.getCommentCount() < 1000) {
                try {
                    System.out.println("Post: " + wsbPost.getTitle());

                    // Call the python script with the -c (comments) argument and the post URL to generate a .json file containing the comments
                    String command = "python " + pathToPythonScript + " -c " + wsbPost.getURl() + " " + nrOfComments + " --" + format;
                    Process p = Runtime.getRuntime().exec(command);

                    String fileTitle = replaceSpecialCharacters(wsbPost.getTitle());

                    // Check if the file exists on disk, if not wait until the file gets created.
                    File file = new File(pathToProject + fileTitle + ".json");
                    //Thread.sleep(5000);

                    while (!file.exists()) {
                        System.out.println("File " + file + " doesn't exist yet, waiting for it to be generated...");
                        Thread.sleep(5000);
                    }

                    wsbCommentJsons.add(moveFilesToDirectory(dir, file).getAbsolutePath());
                    file.delete();
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

    // Create directory if it doesn't already exist
    private static File createDirectory(String directoryName) {
        File dir = new File(pathToProject + directoryName);
        boolean success = true;

        if (!dir.exists()) {
            success = dir.mkdir();
        }
        if (!success) {
            System.out.println("Error at creating directory");
            System.exit(-1);
        }

        return dir;
    }

    // Move json file to specific directory
    private static File moveFilesToDirectory(File directory, File fileToMove) throws IOException {
        File newFile = null;

        if (fileToMove.exists() && directory.exists()) {
            newFile = new File(directory.getAbsolutePath() + "\\" + fileToMove.getName());
            fileToMove.renameTo(newFile);
            fileToMove.delete();
        }

        return newFile;
    }
}
