package helper;

import enums.Result;
import enums.Ticker;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public final class Generics {

    /**
     * Paths to the python script and to your project.
     * Change these in your environment to use the app.
     */
    public static final String pathToPythonScript = "E:\\dev\\universalredditscraper\\Universal-Reddit-Scraper\\scraper.py";
    public static final String pathToProject = "E:\\dev\\Projects\\WSB-Scraper\\";

    /**
     * Parameters for the python script that gathers the content.
     * For documentation on how to use the python script: https://github.com/JosephLai241/Universal-Reddit-Scraper/blob/master/README.md
     */
    public static final String commandType = "r";
    public static final String subredditName = "wallstreetbets";
    public static final String filterBy = "h";
    public static final int nrOfPosts = 25;
    public static final int nrOfComments = 10;
    public static final String format = "json";

    /**
     * Special characters that will get replaced when generating the file on disk.
     */
    public static final List<String> specialCharacters = Arrays.asList("?", "%", "/", ":");

    /**
     * Directory names
     */
    public static final String directoryForSubreddit = "subreddit_json";
    public static final String directoryForComments = "comments_json";
    public static final String directoryForAnalyse = "analyse";

    /**
     * Result files name
     */
    public static final String resultsForPosts = "result_posts.txt";
    public static final String resultsForComments = "result_comments.txt";

    /**
     * Results Map
     */
    public static final Map<String, Result> resultsMap = Map.of(
            "Positive", Result.POSITIVE,
            "Negative", Result.NEGATIVE,
            "Neutral", Result.NEUTRAL);

    /**
     * Tickers map
     */
    public static final Map<String, Ticker> tickers = Map.of(
            "AAPL", Ticker.AAPL,
            "TSLA", Ticker.TSLA);

    private Generics() { }
}