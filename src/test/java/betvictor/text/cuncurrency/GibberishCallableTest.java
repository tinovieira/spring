package betvictor.text.cuncurrency;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import betvictor.text.pojo.ComputationResult;
import betvictor.text.service.RandomTextService;

@RunWith(MockitoJUnitRunner.class)
public class GibberishCallableTest {

	@Mock
	private RandomTextService service;

	@InjectMocks
	private GibberishCallable callable;

	@Test
	public void testCall() throws Exception {
		Elements elements = new Elements();
		Element element = Mockito.mock(Element.class);
		Mockito.when(element.text()).thenReturn("The house the.");
		elements.add(element);
		Mockito.when(service.getGibberish(Mockito.anyInt(), Mockito.anyInt(), Mockito.any())).thenReturn(elements);
		ComputationResult result = callable.call();
		Assert.assertEquals(3, result.getTotalWords());
		Assert.assertEquals(2, result.getCountMap().size());
	}
}
