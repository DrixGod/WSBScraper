package helper;

import enums.Ticker;
import wsb.WSBComment;
import wsb.WSBPost;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static helper.Generics.*;
import static java.lang.Thread.sleep;

public class FileHelper {

    private static File analyseDirectory;
    private static File file = null;

    public static File createAnalyseDirectory() {
        analyseDirectory = new File(pathToProject + directoryForAnalyse );
        if (!analyseDirectory.exists()) {
            analyseDirectory.mkdir();
        }
        return analyseDirectory;
    }

    public static String generateJsonContent(String pathToPythonScript, String commandType, String subredditName, String filterBy, int nrOfPosts, String format) throws IOException, InterruptedException {
        try {
            String command = "python " + pathToPythonScript + " -" + commandType + " " + subredditName + " " + filterBy + " " + nrOfPosts + " --" + format;
            System.out.println("Executing command: " + command);
            Process p = Runtime.getRuntime().exec(command);

            file = new File(pathToProject +  subredditName + ".json");

            // Give the app 5 seconds to create the file.
            sleep(5000);

            // Check if the file exists on disk, if not wait until the file gets created.
            while (!file.exists()) {
                System.out.println("File " + file + " doesn't exist yet, waiting for it to be generated...");
                sleep(5000);
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        // Create directory to move subreddit json to
        File dir = createDirectory(directoryForSubreddit);

        // Move the json file to the created directory
        File subredditFile = moveFilesToDirectory(dir, file);

        return Files.readString(subredditFile.toPath());
    }

    public static Path gatherCommentsFromPosts(ArrayList<WSBPost> wsbPosts, String pathToPythonScript, int nrOfComments, String format) throws IOException { ;
        File dir = createDirectory(directoryForComments);
        for (WSBPost wsbPost : wsbPosts) {
            try {
                if (wsbPost.getCommentCount() < 1000) {
                    System.out.println("Post: " + wsbPost.getTitle());
                    // Call the python script with the -c (comments) argument and the post URL to generate a .json file containing the comments
                    if (wsbPost.getURl().contains("www.reddit.com") && !wsbPost.getTitle().contains("Dow down over 500")) {
                        String command = "python " + pathToPythonScript + " -c " + wsbPost.getURl() + " " + nrOfComments + " --" + format;
                        Process p = Runtime.getRuntime().exec(command);

                        String fileTitle = replaceSpecialCharacters(wsbPost.getTitle());
                        File file = new File(pathToProject + fileTitle + ".json");

                        // Give the app 5 seconds to create the file.
                        sleep(5000);

                        // Check if the file exists on disk, if not wait until the file gets created.
                        while (!file.exists()) {
                            System.out.println("File " + file + " doesn't exist yet, waiting for it to be generated...");
                            sleep(5000);
                        }

                        moveFilesToDirectory(dir, file);

                    } else {
                        System.out.println("Post " + wsbPost.getTitle() + " is linking to some other site.");
                    }
                } else {
                    System.out.println("Post " + wsbPost.getTitle() + " has too many comments, skipping it.");
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                System.exit(-1);
            }
        }
        Path path = Paths.get(pathToProject + directoryForComments);
        deleteFilesOlderThanADay(path.toFile());
        System.out.println("Finished gathering comments in JSON format.");
        return path;
    }

    /**
     * Scan all files in the specified directory and returns the WSB comments from there.
     * @param pathToDir
     * @return
     * @throws IOException
     */
    public static ArrayList<ArrayList<WSBComment>>  gatherJsonFromComments(Path pathToDir) throws IOException {
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
        System.out.println("Finished parsing comments.");
        return wsbComments;
    }

    // Example for RCL
    public static void generateResults(ArrayList<WSBPost> wsbPosts, ArrayList<ArrayList<WSBComment>> wsbComments) throws IOException {
        System.out.println("Generating results...");
        WSBComment wsbComment = null;

        File resultsFileForPosts = new File(analyseDirectory + "\\" + resultsForPosts);
        resultsFileForPosts.createNewFile();
        Path pathToResultFileForPosts = resultsFileForPosts.toPath();

        File resultsFileForComments = new File(analyseDirectory + "\\" + resultsForComments);
        resultsFileForComments.createNewFile();
        Path pathToResultFile = resultsFileForComments.toPath();

        for (Map.Entry<String, Ticker> ticker : tickers.entrySet()) {

            for (WSBPost wsbPost : wsbPosts) {
                if (wsbPost.getTitle().contains(ticker.getKey())) {
                    System.out.println("Found token: " + wsbPost.getTitle());
                    Files.writeString(pathToResultFileForPosts, wsbPost.getTitle() + "\n", StandardOpenOption.APPEND);
                }
                if (wsbPost.getText().contains(ticker.getKey())) {
                    Files.writeString(pathToResultFileForPosts, wsbPost.getText(), StandardOpenOption.APPEND);
                }
            }

            for (ArrayList<WSBComment> comment : wsbComments) {
                for (WSBComment value : comment) {
                    wsbComment = value;
                    if (wsbComment.getCommentText().contains(ticker.getKey())) {
                        System.out.println("Found token: " + wsbComment.getCommentText());
                        Files.writeString(pathToResultFile, wsbComment.getCommentText() + " Upvotes: " + wsbComment.getCommentUpvotes() + "\n", StandardOpenOption.APPEND);
                    }
                }
            }
        }

        System.out.println("Results generated!");
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
     * have very old posts that we have to manually delete. Calling this method will delete
     * any file that wasn't modified in 1 day.
     * @param directory: the directory to check files
     */
    private static void deleteFilesOlderThanADay(File directory){
        List<Path> filePaths = new ArrayList<>();
        try (Stream<Path> paths = Files.walk(Paths.get(String.valueOf(directory.toPath())))) {
            filePaths =  paths
                    .filter(Files::isRegularFile)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Path path: filePaths) {
            long diff = new Date().getTime() - path.toFile().lastModified();

            if (diff > 24 * 60 * 60 * 1000) {
                System.out.println("File " + path.toFile().getName() + " is too old, deleting it.");
                path.toFile().delete();
            }
        }
    }
}
