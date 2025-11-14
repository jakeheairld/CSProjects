package assign06;

import java.net.URL;
import java.util.NoSuchElementException;

/**
 * This class is a Web Browser for traversing web sites and history.
 * 
 * @author Jake Heairld and Anton Smolyanyy
 * @version Feb 29, 2024
 */
public class WebBrowser {

	private LinkedListStack<URL> forwardStack;
	private LinkedListStack<URL> backwardStack;
	private URL currentWebsite;

	/**
	 * Creates a new web browser.
	 */
	public WebBrowser() {
		forwardStack = new LinkedListStack<URL>();
		backwardStack = new LinkedListStack<URL>();
	}
	
	/**
	 * Creates a new web browser with previously existing history.
	 * 
	 * @param history - The previous browsing history.
	 */
	public WebBrowser(SinglyLinkedList<URL> history) {
		forwardStack = new LinkedListStack<URL>();
		backwardStack = new LinkedListStack<URL>();
		for (int i = history.size() - 1; i >= 0; i--) {
			visit(history.get(i));
		}	
	}
	
	/**
	 * Visits a given web page.
	 * 
	 * @param webpage - The web page to visit.
	 */
	public void visit(URL webpage) {
		backwardStack.push(currentWebsite);
		currentWebsite = webpage;
		forwardStack.clear();
	}
	
	/**
	 * Backwards traversal for the web browser.
	 * 
	 * @return - The URL being traveled to.
	 * @throws NoSuchElementException - If there is no web site to go back to.
	 */
	public URL back() throws NoSuchElementException {
		if (backwardStack.isEmpty()) {
			throw new NoSuchElementException("There are no previously visited URLs.");
		}
		forwardStack.push(currentWebsite);
		currentWebsite = backwardStack.pop();
		return currentWebsite;
	}
	
	/**
	 * Forward traversal for the web browser.
	 * 
	 * @return - The URL being traveled to.
	 * @throws NoSuchElementException - IF there is no web site to go back to.
	 */
	public URL forward() throws NoSuchElementException {
		if (forwardStack.isEmpty()) {
			throw new NoSuchElementException("There is no URL to visit next.");
		}
		backwardStack.push(currentWebsite);
		currentWebsite = forwardStack.pop();
		return currentWebsite;
	}
	
	/**
	 * Finds the history for the current web browser.
	 * 
	 * @return - The history of previous URLs visited.
	 */
	public SinglyLinkedList<URL> history() {
		SinglyLinkedList<URL> historyURLs = new SinglyLinkedList<URL>();
		LinkedListStack<URL> temp = new LinkedListStack<URL>();
		int bSize = backwardStack.size();
		for (int i = 1; i < bSize; i++) {
			temp.push(backwardStack.pop());
		}
		for(int i = 1; i < bSize; i++) {
			URL u = temp.pop();
			backwardStack.push(u);
			historyURLs.insertFirst(u);
		}
		historyURLs.insertFirst(currentWebsite);
		return historyURLs;
	}
}