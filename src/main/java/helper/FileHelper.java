package helper;

import wsb.WSBPost;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static helper.FilePaths.*;

public class FileHelper {

    public static String generateJsonContent(String pathToPythonScript, String commandType, String subredditName, String filterBy, int nrOfPosts, String format) throws IOException, InterruptedException {
        try {
            String command = "python " + pathToPythonScript + " -" + commandType + " " + subredditName + " " + filterBy + " " + nrOfPosts + " --" + format;
            System.out.println("Executing command: " + command);
            Process p = Runtime.getRuntime().exec(command);

            file = new File(pathToProject +  subredditName + ".json");

            // Give the app 5 seconds to create the file.
            Thread.sleep(5000);

            // Check if the file exists on disk, if not wait until the file gets created.
            while(!file.exists()) {
                System.out.println("File " + file + " doesn't exist yet, waiting for it to be generated...");
                Thread.sleep(5000);
            }

        } catch (IOException | InterruptedException e) {
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

    public static Path gatherCommentsFromPosts(ArrayList<WSBPost> wsbPosts, String pathToPythonScript, int nrOfComments, String format) throws IOException { ;
        File dir = createDirectory(directoryForComments);
        for (WSBPost wsbPost : wsbPosts) {
            if (wsbPost.getCommentCount() < 1000) {
                try {
                    System.out.println("Post: " + wsbPost.getTitle());

                    // Call the python script with the -c (comments) argument and the post URL to generate a .json file containing the comments
                    String command = "python " + pathToPythonScript + " -c " + wsbPost.getURl() + " " + nrOfComments + " --" + format;
                    Process p = Runtime.getRuntime().exec(command);

                    String fileTitle = replaceSpecialCharacters(wsbPost.getTitle());
                    File file = new File(pathToProject + fileTitle + ".json");

                    // Give the app 5 seconds to create the file.
                    Thread.sleep(5000);

                    // Check if the file exists on disk, if not wait until the file gets created.
                    while (!file.exists()) {
                        System.out.println("File " + file + " doesn't exist yet, waiting for it to be generated...");
                        Thread.sleep(5000);
                    }
                    moveFilesToDirectory(dir, file);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                    System.exit(-1);
                }
            }
            else {
                System.out.println("Skipping post " + wsbPost.getTitle() + " because it has " + wsbPost.getCommentCount() + " comments.");
            }
        }
        Path path = Paths.get(pathToProject + directoryForComments);
        deleteFilesOlderThanADay(path.toFile());
        System.out.println("Finished gathering comments in JSON format.");
        return path;
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
        String pathToFile = directory.getAbsolutePath() + "\\" + fileToMove.getName();
        if (fileToMove.exists() && directory.exists()) {
            Files.move(Paths.get(String.valueOf(fileToMove.toPath())), Paths.get(pathToFile), StandardCopyOption.REPLACE_EXISTING);
            newFile = new File(pathToFile);
        }

        return newFile;
    }

    /**
     * Since we are getting json files from comments, if we run this app every day we will
     * have very old posts tha we have to manually delete. Calling this method will delete
     * any file that wasn't modified in 1 day.
     * @param directory: the directory to check files
     */
    private static void deleteFilesOlderThanADay(File directory){
        List<Path> files = new ArrayList<>();
        try (Stream<Path> paths = Files.walk(Paths.get(String.valueOf(directory.toPath())))) {
            files =  paths
                    .filter(Files::isRegularFile)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Path path: files) {

            long diff = new Date().getTime() - path.toFile().lastModified();

            if (diff > 24 * 60 * 60 * 1000) {
                System.out.println("File " + path.toFile().getName() + " is too old, deleting it.");
                path.toFile().delete();
            }
        }
    }
}
