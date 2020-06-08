package helper;


import java.io.File;
import java.util.Arrays;
import java.util.List;

public final class FilePaths {

    /**
     * Change these in your environment to use the app.
     * For documentation on how to use the python script: https://github.com/JosephLai241/Universal-Reddit-Scraper/blob/master/README.md
     */
    public final static String pathToPythonScript = "E:\\dev\\universalredditscraper\\Universal-Reddit-Scraper\\scraper.py";
    public final static String commandType = "r";
    public final static String subredditName = "wallstreetbets";
    public final static String filterBy = "h";
    public final static int nrOfPosts = 25;
    public final static int nrOfComments = 10;
    public final static String format = "json";
    public static final String pathToProject = "E:\\dev\\Projects\\WSB-Scraper\\";
    public static File file = null;
    public static File subredditFile = null;
    public static final List<String> specialCharacters = Arrays.asList("?", "%", "/", ":");
    public static final String directoryForSubreddit = "subreddit_json";
    public static final String directoryForComments = "comments_json";

    private FilePaths() { }
}