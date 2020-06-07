package helper;

import wsb.WSBPosts;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class FileHelper {

    public static String getJsonContent(String path) throws IOException {
        return Files.readString(Path.of(path));
    }

    public static void gatherCommentsFromPosts(ArrayList<WSBPosts> wsbPosts) throws IOException {
        List<String> urlList = new ArrayList<>();
        urlList.add("https://www.reddit.com/r/wallstreetbets/comments/gxxn8u/bears_bears_bears/");
        urlList.add("https://www.reddit.com/r/wallstreetbets/comments/gxzofl/chwy_earnings_play_week_of_june_8th/");
        List<String> titleList = new ArrayList<>();
        titleList.add("bears bears bears");
        titleList.add("$CHWY Earnings Play, Week of June 8th");
        System.out.println("Gathering comments...");

    }
}
