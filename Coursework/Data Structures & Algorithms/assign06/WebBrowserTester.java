package assign06;

import static org.junit.jupiter.api.Assertions.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * This is a tester class for WebBrowser.
 * 
 * @author Jake Heairld and Anton Smolyanyy
 * @version Feb 29, 2024
 */  
class WebBrowserTester {

	private WebBrowser browser, historyBrowser;
	SinglyLinkedList<URL> history;
	
	@BeforeEach
	void setup() throws MalformedURLException {
		browser = new WebBrowser();
		history = new SinglyLinkedList<URL>();
		history.insertFirst(new URL("https://www.google.com"));
		history.insert(1, new URL("https://www.youtube.com"));
		history.insert(2, new URL("https://www.facebook.com"));
		history.insert(3, new URL("https://www.firefox.com"));
		historyBrowser = new WebBrowser(history);
	}
	
	@Test 
	void testHistoryConstructor() {
		SinglyLinkedList<URL> actual = historyBrowser.history();
		String[] expected = {"https://www.google.com", "https://www.youtube.com", "https://www.facebook.com", "https://www.firefox.com"};
		assertEquals(Arrays.toString(expected), Arrays.toString(actual.toArray()));		
		assertEquals(4, actual.size());
	}
	
	@Test 
	void testVisit() throws MalformedURLException {
		browser.visit(new URL("https://www.google.com"));
		SinglyLinkedList<URL> actual = browser.history();
		assertEquals("[https://www.google.com]", Arrays.toString(actual.toArray()));		
	}
	
	@Test
	void testBackException() throws MalformedURLException {
		assertThrows(NoSuchElementException.class, ()-> browser.back());
	}
	
	@Test
	void testForwardException() throws MalformedURLException {
		browser.visit(new URL("https://www.google.com"));
		assertThrows(NoSuchElementException.class, ()-> browser.forward());
	}
	
	@Test
	void testBack() throws MalformedURLException {
		browser.visit(new URL("https://www.google.com"));
		browser.visit(new URL("https://www.youtube.com"));
		assertEquals(new URL("https://www.google.com"), browser.back());
	}
	
	@Test
	void testForward() throws MalformedURLException {
		browser.visit(new URL("https://www.google.com"));
		browser.visit(new URL("https://www.youtube.com"));
		assertEquals(new URL("https://www.google.com"), browser.back());
		assertEquals(new URL("https://www.youtube.com"), browser.forward());		
	}
	
	@Test
	void testBackAndForwardBrowser() throws MalformedURLException {
		browser.visit(new URL("https://www.yahoo.com"));
		browser.visit(new URL("https://www.google.com"));
		browser.visit(new URL("https://www.youtube.com"));
		browser.visit(new URL("https://www.wikipedia.org"));
		
		assertEquals(new URL("https://www.youtube.com"), browser.back());
		assertEquals(new URL("https://www.google.com"), browser.back());
		assertEquals(new URL("https://www.yahoo.com"), browser.back());

		assertEquals(new URL("https://www.google.com"), browser.forward());
		assertEquals(new URL("https://www.youtube.com"), browser.forward());
		assertEquals(new URL("https://www.wikipedia.org"), browser.forward());
	}
	
	@Test
	void testHistoryManyPages() throws MalformedURLException {
		browser.visit(new URL("https://www.yahoo.com"));
		browser.visit(new URL("https://www.google.com"));
		browser.visit(new URL("https://www.youtube.com"));
		browser.visit(new URL("https://www.wikipedia.org"));
		
		SinglyLinkedList<URL> actual = browser.history();
		SinglyLinkedList<URL> expected = new SinglyLinkedList<URL>();
		
		expected.insertFirst(new URL("https://www.wikipedia.org"));
		expected.insert(1, new URL("https://www.youtube.com"));
		expected.insert(2, new URL("https://www.google.com"));
		expected.insert(3, new URL("https://www.yahoo.com"));
		assertEquals(Arrays.toString(expected.toArray()), Arrays.toString(actual.toArray()));
	}
}