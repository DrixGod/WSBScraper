package nlp;

import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static helper.FilePaths.subredditFile;

public class SentimentAnalysis {

    private static int negative = 0;
    private static int neutral = 0;
    private static int positive = 0;
    private StanfordCoreNLP stanfordCoreNLP;

    public SentimentAnalysis(StanfordCoreNLP stanfordCoreNLP) {
        this.stanfordCoreNLP = stanfordCoreNLP;
    }

    public void performSentimentAnalysis(File file) throws IOException {
        String str = Files.readString(file.toPath());
        Map resultsMap = performSentimentAnalysisOnText(str);
        analyseResultMap(resultsMap);
        System.out.println("RCL Sentiment: \n");
        System.out.println("Negative: " + negative + "\nPositive: " + positive + "\nNeutral " + neutral);
    }

    private Map<String, String> performSentimentAnalysisOnText(String text) {
        Map<String, String> sentenceSentiment = new HashMap<>();
        String sentiment = null;

        CoreDocument coreDocument = new CoreDocument(text);
        stanfordCoreNLP.annotate(coreDocument);
        List<CoreSentence> sentences = coreDocument.sentences();

        for (CoreSentence sentence : sentences) {
            sentiment = sentence.sentiment();
            sentenceSentiment.put(sentence.text(), sentiment);
        }

        return sentenceSentiment;
    }

    private void analyseResultMap(Map resultsMap) {
        for (Object o : resultsMap.entrySet()) {
            Map.Entry pair = (Map.Entry) o;
            if (pair.getValue() == "Negative") {
                negative++;
            } else if (pair.getValue() == "Neutral") {
                neutral++;
            } else {
                positive++;
            }
        }
    }
}
