import helper.FileHelper;
import helper.JSONParser;
import wsb.WSBPosts;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class WSBScraper {

    public static void main(String... args) throws IOException, InterruptedException {
        System.out.println("Reading data...");
        String jsonText = FileHelper.generateJsonContent();
        System.out.println("Parsing data...");
        ArrayList<WSBPosts> wsbposts = JSONParser.parseJson(jsonText);
        System.out.println("Generating comments data...");
        FileHelper.gatherCommentsFromPosts(wsbposts);
    }
}
