package betvictor.text.cuncurrency;

import java.util.SortedSet;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.concurrent.Callable;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import betvictor.text.pojo.ComputationResult;
import betvictor.text.pojo.Word;
import betvictor.text.service.RandomTextService;

@Component
@Scope(value = "prototype")
public class GibberishCallable implements Callable<ComputationResult> {

    private static final Logger log = LoggerFactory.getLogger(GibberishCallable.class);

	@Autowired
	private RandomTextService randomTextService;

    private int paragraph;
    private int wCountMin;
    private int wCountMax;

	public void setup(int paragraph, Integer wCountMin, Integer wCountMax) {
        this.paragraph = paragraph;
        this.wCountMin = wCountMin;
        this.wCountMax = wCountMax;
    }

    @Override
    public ComputationResult call() throws Exception {

        ComputationResult result = new ComputationResult();

        long startTimeParagraph = System.currentTimeMillis();
		Elements paragraphs = randomTextService.getGibberish(paragraph, wCountMin, wCountMax);

        for (Element p : paragraphs) {
            String sentence = p.text().replace(".", "").toLowerCase();
            log.debug("P" + paragraph + " processing: " + sentence);

            StringTokenizer st = new StringTokenizer(sentence);
            // sum number of words in p
            result.sumWords(st.countTokens());

            while (st.hasMoreTokens()) {
                String word = st.nextToken();
                Word wordObj = result.getCountMap().get(word);
                if (wordObj == null) {
                    wordObj = new Word(word, 0);
                    result.getCountMap().put(word, wordObj);
                }
                wordObj.incrementCount();
            }
        }

        result.setNumParagraphs(paragraphs.size());

        log.debug("P" + paragraph + " Time processing " + paragraphs.size() + " paragraph(s): " + result.getNumParagraphs());
        log.debug("P" + paragraph + " Total words processed: " + result.getTotalWords());

        SortedSet<Word> sortedWords = new TreeSet<>(result.getCountMap().values());

        log.debug("P" + paragraph + " Most frequent word: " + sortedWords.first().getWord() +
                " repeated " + sortedWords.first().getCount() + " time(s)");

        result.setTotalTimeProcessingParagraphs(System.currentTimeMillis() - startTimeParagraph);

        return result;
    }
}
