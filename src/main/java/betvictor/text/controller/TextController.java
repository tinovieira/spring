package betvictor.text.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import betvictor.text.concurrency.GibberishCallable;
import betvictor.text.dao.TextDao;
import betvictor.text.entity.Text;
import betvictor.text.pojo.ComputationResult;
import betvictor.text.pojo.Word;

/**
 * Rest Controller
 */
@RestController
public class TextController {

    private static final Logger log = LoggerFactory.getLogger(TextController.class);

    @Autowired
    private ObjectFactory<GibberishCallable> gibberishCallableFactory;

    @Autowired
    private TextDao textDao;

    @Value("${history.last}")
    private int historyResults;

    @RequestMapping("betvictor/text")
    public Text text(@RequestParam(value = "p_start") Integer pStart,
                     @RequestParam(value = "p_end") Integer pEnd,
                     @RequestParam(value = "w_count_min") Integer wCountMin,
                     @RequestParam(value = "w_count_max") Integer wCountMax) {

        long startTime = System.currentTimeMillis();

        if ((pStart <= 0 || pEnd < pStart) || (wCountMin <= 0 || wCountMax < wCountMin)) {
            return null;
        }

        // executor service
        ExecutorService executor = Executors.newFixedThreadPool(8);
        List<Future<ComputationResult>> results = new ArrayList<>(pEnd - pStart);

        // thread pool executor
        for (int p = pStart; p <= pEnd; p++) {
            GibberishCallable gibberishCallable = gibberishCallableFactory.getObject();
            gibberishCallable.setup(p, wCountMin, wCountMax);
            results.add(executor.submit(gibberishCallable));
        }

        // aggregate promises results
        int totalParagraphs = 0;
        int totalWords = 0;
        double sumAverageProcessingParagraph = 0.0;
        Map<String, Word> countMap = new HashMap<>();

        // iterate over promises
        for (Future<ComputationResult> future : results) {
            try {
                ComputationResult result = future.get();
                totalParagraphs += result.getNumParagraphs();
                totalWords += result.getTotalWords();
                sumAverageProcessingParagraph += result.getAverageParagraphProcessingTime();

                // aggregate word count results
                for (Word object : result.getCountMap().values()) {
                    String word = object.getWord();
                    Word wordObj = countMap.get(word);
                    if (wordObj == null) {
                        wordObj = new Word(word, 0);
                        countMap.put(word, wordObj);
                    }
                    wordObj.sumCount(object.getCount());
                }

            } catch (InterruptedException | ExecutionException e) {
                log.error(e.getMessage());
            }
        }

        SortedSet<Word> sortedWords = new TreeSet<>(countMap.values());

        List<Word> mostFrequentWords = new ArrayList<>();
        int maxWordCount = sortedWords.first().getCount();
        for (Word word : sortedWords) {
            if (word.getCount() == maxWordCount) {
                mostFrequentWords.add(word);
            } else {
                break;
            }
        }

        Text text = new Text(
                mostFrequentWords.size() > 1 ? "MULTIPLE" : mostFrequentWords.get(0).getWord(),
                String.valueOf(totalWords / (double) totalParagraphs),
                String.valueOf(sumAverageProcessingParagraph / totalParagraphs),
                String.valueOf(System.currentTimeMillis() - startTime));

        // persist entity
        textDao.save(text);

        return text;
    }\

    @RequestMapping("betvictor/history")
    public List<Text> history() {
        return textDao.findLast(historyResults);
    }
}
