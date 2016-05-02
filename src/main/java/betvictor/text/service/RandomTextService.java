package betvictor.text.service;

import java.text.MessageFormat;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import betvictor.text.pojo.Gibberish;

@Service
public class RandomTextService {

    private static final Logger log = LoggerFactory.getLogger(RandomTextService.class);

    public Elements getGibberish(int paragraph, Integer wCountMin, Integer wCountMax) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.USER_AGENT, "Spring REST");

        String url = MessageFormat.format("http://www.randomtext.me/api/giberish/p-{0}/{1}-{2}", paragraph, wCountMin, wCountMax);
        log.debug("GET " + url);
        ResponseEntity<Gibberish> gibberish = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>("parameters", headers), Gibberish.class);

        // parsing html elements using Jsoup
        return Jsoup.parse(gibberish.getBody().getTextOut()).select("p");
    }
}
