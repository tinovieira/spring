package betvictor.text.controller;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.ObjectFactory;

import betvictor.text.concurrency.GibberishCallable;
import betvictor.text.dao.TextDao;
import betvictor.text.entity.Text;
import betvictor.text.pojo.ComputationResult;
import betvictor.text.pojo.Word;

@RunWith(MockitoJUnitRunner.class)
public class TextControllerTest {

    @Mock
    private TextDao textDao;

    @Mock
    private ObjectFactory<GibberishCallable> gibberishCallableFactory;

    @InjectMocks
    private TextController controller;

    private ComputationResult results = null;

    @Before
    public void setup() throws Exception {
        GibberishCallable callable = Mockito.mock(GibberishCallable.class);
        String word = "betVictor";
        Word wordObj = new Word(word, 1);
        results = new ComputationResult();
        results.getCountMap().put(word, wordObj);
        Mockito.when(callable.call()).thenReturn(results).thenReturn(new ComputationResult());
        Mockito.when(gibberishCallableFactory.getObject()).thenReturn(callable);
    }

    @Test
    public void testTextSingleParagrapgh() {
        Text returnedText = controller.text(1, 1, 1, 4);
        Assert.assertEquals("betVictor", returnedText.getMostFrequentWord());
        Mockito.verify(gibberishCallableFactory).getObject();
        Mockito.verify(textDao).save(returnedText);
    }

    @Test
    public void testTextNoSingleMostFrequentWord() {
        String secondWord = "interview";
        results.getCountMap().put(secondWord, new Word(secondWord, 1));
        Text returnedText = controller.text(2, 4, 1, 4);
        Assert.assertEquals("MULTIPLE", returnedText.getMostFrequentWord());
    }
}
