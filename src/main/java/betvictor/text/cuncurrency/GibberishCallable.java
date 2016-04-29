package betvictor.text.cuncurrency;

import betvictor.text.pojo.ComputationResult;
import betvictor.text.pojo.Gibberish;
import betvictor.text.pojo.Word;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.SortedSet;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.concurrent.Callable;

public class GibberishCallable implements Callable<ComputationResult> {

    private static final Logger log = LoggerFactory.getLogger(GibberishCallable.class);

    private String url;
    private int paragraph;


    public GibberishCallable(int paragraph, String url) {
        this.url = url;
        this.paragraph = paragraph;
    }

    @Override
    public ComputationResult call() throws Exception {

        ComputationResult result = new ComputationResult();

        long startTimeParagraph = System.currentTimeMillis();
        // Consuming random text api rest service
        RestTemplate restTemplate = new RestTemplate();
        // avoid 403 error
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.USER_AGENT, "Spring REST");

        log.info("GET " + url);
        ResponseEntity<Gibberish> gibberish =
                restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>("parameters", headers), Gibberish.class);

        // parsing html elements using Jsoup
        Elements paragraphs = Jsoup.parse(gibberish.getBody().getTextOut()).select("p");

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

        log.info("P" + paragraph + " Time processing " + paragraphs.size() + " paragraph(s): " + result.getNumParagraphs());
        log.info("P" + paragraph + " Total words processed: " + result.getTotalWords());

        SortedSet<Word> sortedWords = new TreeSet<>(result.getCountMap().values());

        log.info("P" + paragraph + " Most frequent word: " + sortedWords.first().getWord() +
                " repeated " + sortedWords.first().getCount() + " time(s)");

        result.setTotalTimeProcessingParagraphs(System.currentTimeMillis() - startTimeParagraph);

        return result;
    }
}
