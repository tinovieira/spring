package betvictor.text.pojo;

import java.util.HashMap;
import java.util.Map;

public final class ComputationResult {

    private int numParagraphs;

    private long totalTimeProcessingParagraphs;

    private int totalWords;

    private Map<String, Word> countMap;

    public ComputationResult() {
        this.numParagraphs = 0;
        this.totalWords = 0;
        this.countMap = new HashMap<>();
    }

    public int getNumParagraphs() {
        return numParagraphs;
    }

    public void setNumParagraphs(int numParagraphs) {
        this.numParagraphs = numParagraphs;
    }

    public double getAverageParagraphProcessingTime() {
        return totalTimeProcessingParagraphs / (double) numParagraphs;
    }

    public void setTotalTimeProcessingParagraphs(long totalTimeProcessingParagraphs) {
        this.totalTimeProcessingParagraphs = totalTimeProcessingParagraphs;
    }

    public int getTotalWords() {
        return totalWords;
    }

    public void sumWords(int numWords) {
        this.totalWords += numWords;
    }

    public Map<String, Word> getCountMap() {
        return countMap;
    }
}
