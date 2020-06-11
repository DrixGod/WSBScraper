package helper;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import enums.Ticker;
import nlp.Pipeline;
import nlp.SentimentAnalysis;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import static helper.Generics.*;

public class SentimentAnalysisHelper {

    public static void performSentimentAnalysis() throws IOException {
        StanfordCoreNLP stanfordCoreNLP = Pipeline.getPipeline();
        SentimentAnalysis sentimentAnalysis = new SentimentAnalysis(stanfordCoreNLP);

        for (Map.Entry<String, Ticker> ticker : tickers.entrySet()) {
            File titles = new File(pathToProject + directoryForAnalyse + "\\" + resultsForPosts);
            sentimentAnalysis.performSentimentAnalysis(titles, ticker.getValue());

            File comments = new File(pathToProject + directoryForAnalyse + "\\" + resultsForComments);
            sentimentAnalysis.performSentimentAnalysis(comments, ticker.getValue());
        }
    }
}
