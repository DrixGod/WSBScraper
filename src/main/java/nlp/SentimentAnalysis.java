package nlp;

import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import enums.Ticker;
import helper.Generics;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SentimentAnalysis {

    private int negative;
    private int neutral;
    private int positive;
    private final StanfordCoreNLP stanfordCoreNLP;

    public SentimentAnalysis(StanfordCoreNLP stanfordCoreNLP) {
        this.stanfordCoreNLP = stanfordCoreNLP;
    }

    public void performSentimentAnalysis(File file, Ticker ticker) throws IOException {
        negative = 0;
        neutral = 0;
        positive = 0;
        String str = Files.readString(file.toPath());
        Map resultsMap = performSentimentAnalysisOnText(str);
        analyseResultMap(resultsMap);
        showResults(ticker);
    }

    private Map<String, String> performSentimentAnalysisOnText(String text) {
        Map<String, String> sentenceSentiment = new HashMap<>();
        CoreDocument coreDocument = new CoreDocument(text);
        stanfordCoreNLP.annotate(coreDocument);
        List<CoreSentence> sentences = coreDocument.sentences();

        String sentiment = null;
        for (CoreSentence sentence : sentences) {
            sentiment = sentence.sentiment();
            sentenceSentiment.put(sentence.text(), sentiment);
        }

        return sentenceSentiment;
    }

    private void analyseResultMap(Map results) {
        for (Object o : results.entrySet()) {
            Map.Entry pair = (Map.Entry) o;
            switch (Generics.resultsMap.get(pair.getValue())) {
                case POSITIVE -> positive++;
                case NEGATIVE -> negative++;
                case NEUTRAL -> neutral++;
            }
        }
    }

    private void showResults(Ticker ticker) {
        System.out.println(ticker.toString() + " sentiment:\n");
        System.out.println("Positive: " + positive + "\nNegative: " + negative + "\nNeutral: " + neutral);
    }
}
